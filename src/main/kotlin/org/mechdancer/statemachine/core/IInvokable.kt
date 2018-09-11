package org.mechdancer.statemachine.core

/**
 * 自动状态机
 * 包含指定源节点，自动执行并在执行完毕后跳转
 */
interface IInvokable<T : IState>
	: IStateMachine<T> {
	/** 源节点 */
	val origin: T?

	/** 是否执行完毕 */
	val isCompleted: Boolean

	/**
	 * 指定新源节点，并跳转到此节点
	 * @return 指定是否成功
	 */
	fun startFrom(newOrigin: T): Boolean

	/**
	 * 重置
	 * 直接跳转到初始状态
	 * @return 跳转是否成功
	 */
	fun reset(): Boolean

	/**
	 * 驱动状态机运行一个周期
	 * 运行结束后若外部状态未引发状态转移则触发一个状态转移事件
	 */
	override operator fun invoke()
}
