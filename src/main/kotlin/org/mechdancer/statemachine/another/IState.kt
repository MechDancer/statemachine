package org.mechdancer.statemachine.another

/** 状态 */
interface IState {
	/** 是否允许自动自循环 */
	val loop: Boolean

	/** 检查进入条件 */
	fun before() = true

	/** 检查退出条件 */
	fun after() = true

	/** 状态动作 */
	fun doing()
}
