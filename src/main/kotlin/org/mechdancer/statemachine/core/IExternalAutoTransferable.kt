package org.mechdancer.statemachine.core

interface IExternalAutoTransferable<T : IState>
	: IInvokable<T> {
	/**
	 * 不驱动状态机运行，但立即触发一个状态转移事件，就好像当前状态的动作运行结束了一样
	 */
	fun transferNow(): Boolean

	/**
	 * 立即离开当前状态去往可达的下一个状态
	 */
	fun gotoNext(): Boolean
}
