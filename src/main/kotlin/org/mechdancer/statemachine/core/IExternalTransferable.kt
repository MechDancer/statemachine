package org.mechdancer.statemachine.core

/**
 * 外部驱动状态机
 * 可以从外部直接迁移状态
 */
interface IExternalTransferable<T : IState>
	: IStateMachine<T> {
	/**
	 * 无源跳转
	 * 从任意状态直接去往目标状态
	 * @param target 目标状态
	 * @return 是否发生转移
	 */
	infix fun transfer(target: T?): Boolean

	/**
	 * > 不安全 <
	 * 强制跳转
	 * 不检查当前状态，直接去往目标状态
	 * @param target 目标状态
	 * @return 是否发生转移
	 */
	infix fun goto(target: T?): Boolean
}
