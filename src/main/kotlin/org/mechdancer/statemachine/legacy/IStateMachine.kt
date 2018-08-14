package org.mechdancer.statemachine.legacy

interface IStateMachine {
	val waiters: Array<IWaiter>

	fun run(state: Int)
}