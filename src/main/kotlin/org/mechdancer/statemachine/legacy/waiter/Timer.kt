package org.mechdancer.statemachine.legacy.waiter

import java.util.concurrent.TimeUnit

open class Timer(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, name: String = "Timer") : Waiter(name) {

	companion object : Timer(0,name = "DefaultTimer")

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