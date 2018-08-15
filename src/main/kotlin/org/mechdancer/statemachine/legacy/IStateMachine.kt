package org.mechdancer.statemachine.legacy

import org.mechdancer.statemachine.legacy.blockers.StateBlocker

interface IStateMachine {
	val stateBlockers: List<StateBlocker>

	fun action(state: Int)
}