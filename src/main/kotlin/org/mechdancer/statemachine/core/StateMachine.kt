package org.mechdancer.statemachine.core

import org.mechdancer.statemachine.Event
import org.mechdancer.statemachine.core.impl.StateMachineImpl

interface StateMachine<T : IState> {

	/** 当前状态 */
	val current: T?

	/** 执行完毕检查 */
	val isCompleted: Boolean

	/**
	 * 注册事件通路
	 * @param pair 事件对
	 * @return 转移事件
	 */
	infix fun register(pair: Pair<T, T>): Event

	/**
	 * 无源跳转
	 * 不判断当前状态，直接去往目标状态
	 * @param target 目标状态
	 * @return 是否发生转移
	 */
	infix fun transfer(target: T?): Boolean

	/**
	 * 构造事件，但不注册到事件通路
	 * @param pair 事件对
	 * @return 转移事件
	 */
	fun event(pair: Pair<T, T>): Event

	/**
	 * 强制跳转
	 * 不考虑当前状态，直接去往目标状态
	 * @param target 目标状态
	 * @return 是否发生转移
	 */
	infix fun goto(target: T?): Boolean

	/**
	 * 重置
	 * 直接跳转到初始状态
	 * @return 跳转是否成功
	 */
	fun reset(): Boolean

	/**
	 * 指定新源节点，并跳转到此节点
	 * @return 指定是否成功
	 */
	fun startFrom(newOrigin: T): Boolean

	/**
	 * 驱动状态机运行一个周期
	 * 运行结束后若外部状态未引发状态转移则触发一个状态转移事件
	 */
	operator fun invoke()

	companion object {
		const val ACCEPT = true  //接受：同意转移
		const val REJECT = false //驳回：拒绝转移

		fun <T : IState> create(init: T? = null): StateMachine<T> = StateMachineImpl(init)
	}
}