package org.mechdancer.statemachine.legacy

import org.mechdancer.statemachine.legacy.waiter.Waiter

interface IStateMachine {
	val waiters: List<Waiter>

	fun run(state: Int)
}