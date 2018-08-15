package org.mechdancer.statemachine.legacy

import org.mechdancer.statemachine.legacy.blockers.ConditionBlocker
import org.mechdancer.statemachine.legacy.blockers.StateBlocker
import org.mechdancer.statemachine.legacy.blockers.TimerBlocker

abstract class SwitchableStateMachine : IStateMachine, Runnable {

	private var current = 0
	private var last = 0
	private var isFinished = false
	var shouldRunning = true
		private set

	override val stateBlockers: MutableList<StateBlocker> = mutableListOf(ConditionBlocker, TimerBlocker)

	override fun run() {
		if (!shouldRunning) return
		action(current)
		isFinished = stateBlockers.all { !it.isBlocking }
		stateBlockers.forEach(StateBlocker::refresh)
		if (isFinished)
			jump(current.inc())//使用 `++` 会直接改变 current 值
	}

	fun runToFinish() {
		while (shouldRunning)
			run()
	}

	fun jump(target: Int) {
		last = current
		current = target
	}

	fun jumpBack() {
		jump(last)
	}

	fun terminate() {
		shouldRunning = false
	}

}