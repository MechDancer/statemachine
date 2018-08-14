package org.mechdancer.statemachine.core.builder

import org.mechdancer.statemachine.core.IStateHandler

/**
 * 状态机构建器
 */
interface IStateMachineBuilder {
	/**
	 * 构建状态机
	 */
	fun build(): IStateHandler
}