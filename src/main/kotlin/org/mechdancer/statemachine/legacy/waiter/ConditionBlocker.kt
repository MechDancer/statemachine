package org.mechdancer.statemachine.legacy.waiter

open class ConditionBlocker(private var boolean: Boolean,
                            name: String = "ConditionBlocker") : StateBlocker(name) {

	companion object : ConditionBlocker(true, "CommonConditionBlocker")

	override fun sync() {
		finished = boolean
	}

	fun reset(condition: Boolean) {
		boolean = condition
		reset()
	}
}