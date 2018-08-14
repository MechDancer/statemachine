package org.mechdancer.statemachine.core

enum class Ending : IStateHandler {
	/**
	 * 完结状态
	 */
	Destination,
	/**
	 * 超时
	 */
	TimeOut;

	override fun run() = this
}