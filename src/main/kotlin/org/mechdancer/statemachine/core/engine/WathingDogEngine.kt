package org.mechdancer.statemachine.core.engine

import org.mechdancer.statemachine.core.Ending
import org.mechdancer.statemachine.core.IStateHandler
import java.util.concurrent.TimeUnit


/**
 * 计时驱动器
 * 执行到时限则返回done
 */
class WatchDogEngine(limit: Long,
                     timeUnit: TimeUnit,
                     origin: IStateHandler)
	: IEngine {


	private var current = origin
	private var firstInvoke: Long = -1

	private val limit = TimeUnit.NANOSECONDS.convert(limit, timeUnit)

	/**
	 * 执行一个状态并向后跳转，超时或结束后返回done
	 */
	override fun run(): Boolean = run {
		if (firstInvoke == -1L)
			firstInvoke = System.nanoTime()

		current is Ending ||
				run {
					current = if (timeOut) Ending.TimeOut else current.run()
					current is Ending
				}
	}

	/**
	 * 超时标记
	 */
	val timeOut
		get() = (System.nanoTime() - firstInvoke) > limit
}

fun delayMachine(limit: Long, timeUnit: TimeUnit) =
		WatchDogEngine(limit, timeUnit, IStateHandler.Nothing)