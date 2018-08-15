package org.mechdancer.statemachine.legacy.waiter

open class Condition(private var boolean: Boolean, name: String = "Condition") : Waiter(name) {

	companion object : Condition(true, "DefaultCondition")

	override fun sync() {
		finished = boolean
	}

	fun reset(condition: Boolean) {
		boolean = condition
		reset()
	}
}