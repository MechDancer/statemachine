package org.mechdancer.statemachine.legacy.waiter

class Condition(private var boolean: Boolean) : Waiter() {
	override fun sync() {
		finished = boolean
	}

	fun reset(condition: Boolean) {
		boolean = condition
		teardown()
	}
}