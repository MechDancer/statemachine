package org.mechdancer.statemachine.legacy

import org.mechdancer.statemachine.legacy.waiter.Waiter

@Deprecated("emmmm", ReplaceWith("org.mechdancer." +
		"statemachine.legacy.SwitchableStateMachine"))
class StateMachineEngine(private val stateMachine: IStateMachine) {


	private var current = 0

	private var isFinished = false


	fun run() = with(stateMachine) {
		action(current)
		isFinished = waiters.all { !it.isWaiting }
		waiters.forEach(Waiter::refresh)
		if (isFinished)
			current++
		Unit
	}
}