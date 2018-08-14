package org.mechdancer.statemachine.core.engine

import org.mechdancer.statemachine.core.IStateHandler

/**
 * 原子驱动器
 * 将状态链一次执行完
 */
class AtomicEngine(origin: IStateHandler) : IEngine {
	private val engine = Engine(origin)

	/**
	 * 执行到状态链末尾
	 */
	override fun run() =
			run { while (!engine.run()); true }
}