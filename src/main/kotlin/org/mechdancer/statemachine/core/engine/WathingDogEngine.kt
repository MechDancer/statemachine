package org.mechdancer.statemachine.core.engine

import org.mechdancer.statemachine.core.Ending
import org.mechdancer.statemachine.core.IStateHandler
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.*


/**
 * 计时驱动器
 * 执行到时限则返回done
 */
class WatchDogEngine(limit: Double,
                     timeUnit: TimeUnit,
                     origin: IStateHandler)
	: IEngine {

	private companion object {
		const val HOUR = 1.0 / (60 * 60)
		const val MINUTE = 1.0 / 60
		const val SECOND = 1.0
		const val MS = 1.0E3
		const val US = 1.0E6
		const val NS = 1.0E9
	}

	private var current = origin
	private var firstInvoke: Long? = null

	private val limit = when (timeUnit) {
		SECONDS      -> limit * SECOND
		MILLISECONDS -> limit * MS
		DAYS         -> limit * HOUR * 24
		HOURS        -> limit * HOUR
		MICROSECONDS -> limit * US
		MINUTES      -> limit * MINUTE
		NANOSECONDS  -> limit * NS
	}

	/**
	 * 执行一个状态并向后跳转，超时或结束后返回done
	 */
	override fun run(): Boolean = run {
		if (firstInvoke == null)
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
		get() = (System.nanoTime() - firstInvoke!!) * 1E-9 > limit
}

fun delayMachine(limit: Double, timeUnit: TimeUnit) =
		WatchDogEngine(limit, timeUnit, IStateHandler.Nothing)