package org.mechdancer.statemachine.legacy

import org.mechdancer.statemachine.legacy.waiter.StateBlocker

interface IStateMachine {
	val stateBlockers: List<StateBlocker>

	fun action(state: Int)
}