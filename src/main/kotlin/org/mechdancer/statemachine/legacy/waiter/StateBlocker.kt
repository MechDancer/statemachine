package org.mechdancer.statemachine.legacy.waiter

abstract class StateBlocker(val name: String) {
	internal var isBlocking: Boolean = false
	private var isRunning: Boolean = false
	protected var finished: Boolean = false

	internal fun refresh() {
		sync()
		isBlocking = if (isRunning)
			!finished
		else false
	}

	protected abstract fun sync()

	protected fun reset() {
		isBlocking = false
		isRunning = false
		finished = false
	}

	fun enable() {
		isRunning = true
	}

	fun disable() {
		isRunning = false
	}
}
