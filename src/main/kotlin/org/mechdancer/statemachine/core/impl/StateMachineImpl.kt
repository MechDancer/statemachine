package org.mechdancer.statemachine.core.impl

import org.mechdancer.statemachine.Event
import org.mechdancer.statemachine.core.IState
import org.mechdancer.statemachine.core.StateMachine
import org.mechdancer.statemachine.core.StateMachine.Companion.REJECT

/**
 * 状态机
 * @param T 状态类型。不是强制的，但为了安全环保，建议定义一个枚举。
 * @param origin 初始状态
 */
class StateMachineImpl<T : IState> internal constructor(private var origin: T?) : StateMachine<T> {


	//状态转移互斥锁
	private val lock = Any()

	//按检查结果转移
	private infix fun Event.trans(target: T?) =
			synchronized(lock) { this().also { if (it) _current = target } }

	private var _current: T? = origin

	/** 目标状态表 */
	private val targets = mutableMapOf<T, MutableSet<T>>()

	private val _isCompleted get() = _current === null


	override val current: T?
		get() = _current

	override val isCompleted: Boolean
		get() = _isCompleted


	override fun event(pair: Pair<T, T>) =
			{ { _current === pair.first && pair.first.after() && pair.second.before() } trans pair.second }


	override infix fun register(pair: Pair<T, T>) =
			event(pair).also {
				if (pair.first === pair.second) return@also
				targets[pair.first]?.add(pair.second)
						?: run { targets[pair.first] = mutableSetOf(pair.second) }
				targets[pair.second]
						?: run { targets[pair.second] = mutableSetOf() }
			}


	override operator fun invoke() {
		_current?.let { last ->
			last.doing();
			{ _current === last } trans
					(targets[last]
							?.firstOrNull { event(last to it)() }
							?: last.takeIf { last.loop })
		}
	}


	override infix fun transfer(target: T?) =
			{ _current?.after() != REJECT && target?.before() != REJECT } trans target


	override infix fun goto(target: T?) =
			{ target?.before() != REJECT } trans target


	override fun reset() = transfer(origin)


	override fun startFrom(newOrigin: T) =
			_isCompleted.also {
				if (!it) return@also
				origin = newOrigin
				reset()
			}
}
