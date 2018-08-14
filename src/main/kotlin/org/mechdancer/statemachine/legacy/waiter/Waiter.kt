package org.mechdancer.statemachine.legacy.waiter

abstract class Waiter {
	internal var isWaiting: Boolean = false
	protected var isRunning: Boolean = false
	protected var finished: Boolean = false

	internal fun refresh() {
		sync()
		isWaiting = if (isRunning)
			!finished
		else false
	}

	protected abstract fun sync()

	fun start() {
		isRunning = true
	}

}
