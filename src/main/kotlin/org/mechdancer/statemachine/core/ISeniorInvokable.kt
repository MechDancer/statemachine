package org.mechdancer.statemachine.core

/**
 * 可从外部修改源节点或跳转到无状态
 */
interface ISeniorInvokable<T : IState>
	: IInvokable<T> {
	/**
	 * 指定新源节点，并跳转到此节点
	 * @return 指定是否成功
	 */
	fun startFrom(newOrigin: T): Boolean

	/** 跳转到无状态 */
	fun stop()
}
