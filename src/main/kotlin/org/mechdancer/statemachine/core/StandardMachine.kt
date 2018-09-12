package org.mechdancer.statemachine.core

import org.mechdancer.statemachine.Event
import org.mechdancer.statemachine.REJECT
import org.mechdancer.statemachine.otherwise
import org.mechdancer.statemachine.then

/**
 * 标准状态机
 * 状态机的一种正确实现，事件驱动，可自动执行，可外部迁移状态，可做其他状态机的子状态
 * @param T 状态类型。不是强制的，但为了安全环保，建议定义一个枚举
 * @param origin 初始状态
 */
class StandardMachine<T : IState>(origin: T? = null)
	: IEventDrivenInvokable<T>,
	IMutableOrigin<T>,
	IExternalTransferable<T>,
	IExternalAutoTransferable<T>,
	IState {

	//状态转移互斥锁
	private val lock = Any()

	//按检查结果转移
	private infix fun Event.thenTransfer(target: T?) =
		synchronized(lock) { this().then { current = target } }

	//检查能否发生某个转移
	private fun check(pair: Pair<T?, T?>) =
		pair.first?.after() != REJECT && pair.second?.before() != REJECT

	// 目标状态表
	private val targets = mutableMapOf<T, MutableSet<T>>()

	override var origin: T? = origin
		private set

	override var current: T? = origin
		private set

	override val isCompleted
		get() = current === null

	override fun event(pair: Pair<T, T>) =
		{ { current === pair.first && check(pair) } thenTransfer pair.second }

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
			{ current === last } thenTransfer
				(targets[last]
					?.firstOrNull { check(last to it) }
					?: last.takeIf { last.loop })
		}
	}

	override infix fun transfer(target: T?) =
		{ check(current to target) } thenTransfer target

	override infix fun goto(target: T?) =
		{ check(null to target) } thenTransfer target

	override fun reset() = transfer(origin)

	override fun startFrom(newOrigin: T) =
		isCompleted.then {
			origin = newOrigin
			reset()
		}

	override fun stop() {
		goto(null)
	}

	override fun transferNow() =
		current?.let { last ->
			current = targets[last]
				?.firstOrNull { check(current to it) }
				?: last.takeIf { last.loop }
			true
		} ?: false

	override fun gotoNext() =
		current?.let { last ->
			current = targets[last]
				?.firstOrNull { check(null to it) }
			true
		} ?: false

	override val loop = false
	override fun before() = !isCompleted
	override fun after() = isCompleted
	override fun doing() = invoke()
}
