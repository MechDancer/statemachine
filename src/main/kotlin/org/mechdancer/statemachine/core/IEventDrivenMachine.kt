package org.mechdancer.statemachine.core

import org.mechdancer.statemachine.Event

/**
 * 事件驱动状态机
 * 可以构造事件
 */
interface IEventDrivenMachine<T : IState>
	: IStateMachine<T> {
	/**
	 * 构造事件
	 * @param pair 事件对
	 * @return 转移事件
	 */
	fun event(pair: Pair<T, T>): Event
}
