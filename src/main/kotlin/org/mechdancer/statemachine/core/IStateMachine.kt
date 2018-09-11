package org.mechdancer.statemachine.core

/** 状态机 */
interface IStateMachine<T : IState> {
	/** 当前状态 */
	val current: T?

	/** 驱动状态机运行一个周期 */
	operator fun invoke()
}
