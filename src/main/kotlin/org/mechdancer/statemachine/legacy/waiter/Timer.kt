package org.mechdancer.statemachine.legacy.waiter

import java.util.concurrent.TimeUnit

class Timer(delay: Long, timeUnit: TimeUnit) : Waiter() {
	private var target = System.currentTimeMillis() +
			TimeUnit.MILLISECONDS.convert(delay, timeUnit)


	override fun sync() {
		finished = System.currentTimeMillis() > target
	}

	fun reset(delay: Long, timeUnit: TimeUnit) {
		target = System.currentTimeMillis() +
				TimeUnit.MILLISECONDS.convert(delay, timeUnit)
		teardown()
	}
}