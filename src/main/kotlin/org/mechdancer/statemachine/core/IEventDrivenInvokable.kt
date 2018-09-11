package org.mechdancer.statemachine.core

import org.mechdancer.statemachine.Event

/**
 * 事件驱动自动状态机
 * 必须预先指定事件通路
 */
interface IEventDrivenInvokable<T : IState>
	: IEventDrivenMachine<T>, IInvokable<T> {
	/**
	 * 注册事件通路
	 * @param pair 事件对
	 * @return 转移事件
	 */
	infix fun register(pair: Pair<T, T>): Event
}
