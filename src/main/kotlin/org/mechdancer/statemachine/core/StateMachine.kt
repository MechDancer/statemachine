package org.mechdancer.statemachine.core

import org.mechdancer.statemachine.Event
import org.mechdancer.statemachine.core.IStateMachine.Companion.REJECT
import org.mechdancer.statemachine.otherwise
import org.mechdancer.statemachine.then

/**
 * 状态机
 * @param T 状态类型。不是强制的，但为了安全环保，建议定义一个枚举。
 * @param origin 初始状态
 */
class StateMachine<T : IState>
internal constructor(private var origin: T?)
	: IStateMachine<T> {
	//状态转移互斥锁
	private val lock = Any()

	//按检查结果转移
	private infix fun Event.trans(target: T?) =
		synchronized(lock) { this().then { current = target } }

	// 目标状态表
	private val targets = mutableMapOf<T, MutableSet<T>>()

	override var current: T? = origin
		private set

	override val isCompleted: Boolean
		get() = current === null

	override fun event(pair: Pair<T, T>) =
		{ { current === pair.first && pair.first.after() && pair.second.before() } trans pair.second }

	override infix fun register(pair: Pair<T, T>) =
		event(pair).also {
			(pair.first === pair.second).otherwise {
				targets[pair.first]?.add(pair.second)
					?: run { targets[pair.first] = mutableSetOf(pair.second) }
				targets[pair.second]
					?: run { targets[pair.second] = mutableSetOf() }
			}
		}

	override operator fun invoke() {
		current?.let { last ->
			last.doing();
			{ current === last } trans
				(targets[last]
					?.firstOrNull { event(last to it)() }
					?: last.takeIf { last.loop })
		}
	}

	override infix fun transfer(target: T?) =
		{ current?.after() != REJECT && target?.before() != REJECT } trans target

	override infix fun goto(target: T?) =
		{ target?.before() != REJECT } trans target

	override fun reset() = transfer(origin)

	override fun startFrom(newOrigin: T) =
		isCompleted.then {
			origin = newOrigin
			reset()
		}
}
