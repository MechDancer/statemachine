package org.mechdancer.statemachine.dsl

import org.mechdancer.statemachine.IState
import org.mechdancer.statemachine.StateMachine

/** 状态机缓存 */
class StateMachineDsl {
	private var _machine: StateMachine<IState>? = null

	private fun IState.init() = also { if (_machine == null) _machine = StateMachine(it) }

	/** 获取实例 */
	val machine get() = _machine!!

	/**
	 * 构造状态
	 * 第一个构造的状态视作初始状态
	 */
	fun state(block: StateDsl.() -> Unit) =
		org.mechdancer.statemachine.dsl.state(block).init()

	/**
	 * 构造延时状态
	 * 第一个构造的状态视作初始状态
	 */
	fun delay(block: TimeDsl.() -> Unit) =
		org.mechdancer.statemachine.dsl.delay(block).init()

	/** 从默认状态机构造事件 */
	fun event(pair: Pair<IState, IState>) = machine.event(pair)

	/** 向默认状态机注册事件 */
	fun register(pair: Pair<IState, IState>) = machine.register(pair)

	/** 默认状态机转移 */
	fun transfer(target: IState) = machine.transfer(target)

	/** 默认状态机恢复初始状态 */
	fun reset() = machine.reset()
}

/** 构造状态机 */
fun stateMachine(block: StateMachineDsl.() -> Unit) =
	StateMachineDsl().apply(block).machine
