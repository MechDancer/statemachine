package org.mechdancer.statemachine.core.builder

import org.mechdancer.statemachine.core.Ending
import org.mechdancer.statemachine.core.IStateHandler
import org.mechdancer.statemachine.core.engine.IEngine
import org.mechdancer.statemachine.core.engine.delayMachine
import java.util.concurrent.TimeUnit


/**
 * 线性连接
 */
class LinearStateMachineBuilder : IStateMachineBuilder {
	private val states = mutableListOf<() -> Boolean>()

	/**
	 * 添加状态
	 */
	fun next(block: () -> Boolean): LinearStateMachineBuilder = apply {
		states.add(block)
	}

	/**
	 * 添加状态（只执行一次）
	 */
	fun once(block: () -> Unit): LinearStateMachineBuilder = apply {
		states.add { block(); true }
	}

	/**
	 * 添加状态（执行指定次数）
	 */
	fun repeat(t: Int, block: () -> Unit): LinearStateMachineBuilder = apply {
		var remain = t
		states.add {
			block()
			--remain <= 0
		}
	}

	/**
	 * 添加状态（反复执行）
	 */
	fun forever(block: () -> Unit): LinearStateMachineBuilder = apply {
		states.add { block(); false }
	}

	/**
	 * 驱动引擎（高级功能）
	 */
	fun drive(engine: IEngine): LinearStateMachineBuilder = apply {
		states.add { engine.run() }
	}

	fun delay(limit: Double, timeUnit: TimeUnit) = apply {
		drive(delayMachine(limit, timeUnit))
	}

	private infix fun (() -> Boolean).link(
			next: IStateHandler) =
			object : IStateHandler {
				override fun run() =
						if (this@link()) next
						else this
			}

	/**
	 * 构造状态链
	 */
	override fun build(): IStateHandler {
		var temp: IStateHandler = Ending.Destination
		for (i in states.indices.reversed()) {
			val out = temp
			temp = object : IStateHandler {
				override fun run() =
						states[i] link out
			}
		}
		return temp
	}
}
