package org.mechdancer.statemachine.legacy.waiter

abstract class Waiter(val name: String) {
	internal var isWaiting: Boolean = false
	private var isRunning: Boolean = false
	protected var finished: Boolean = false

	internal fun refresh() {
		sync()
		isWaiting = if (isRunning)
			!finished
		else false
	}

	protected abstract fun sync()

	protected fun reset() {
		isWaiting = false
		isRunning = false
		finished = false
	}

	fun start() {
		isRunning = true
	}

	fun teardown() {
		isRunning = false
	}
}
