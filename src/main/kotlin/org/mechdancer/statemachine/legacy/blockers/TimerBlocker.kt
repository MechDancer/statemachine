package org.mechdancer.statemachine.legacy.blockers

import java.util.concurrent.TimeUnit

open class TimerBlocker(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, name: String = "TimerBlocker") : StateBlocker(name) {

	companion object : TimerBlocker(0,name = "CommonTimerBlocker")

	private var target = TimeUnit.MILLISECONDS.convert(delay, timeUnit)
		set(value) {
			field = System.currentTimeMillis() + value
		}


	override fun sync() {
		finished = System.currentTimeMillis() > target
	}

	fun reset(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS) {
		target = TimeUnit.MILLISECONDS.convert(delay, timeUnit)
		reset()
	}
}