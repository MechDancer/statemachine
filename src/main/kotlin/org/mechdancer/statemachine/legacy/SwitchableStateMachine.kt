package org.mechdancer.statemachine.legacy

import org.mechdancer.statemachine.legacy.waiter.Condition
import org.mechdancer.statemachine.legacy.waiter.Timer
import org.mechdancer.statemachine.legacy.waiter.Waiter

abstract class SwitchableStateMachine : IStateMachine, Runnable {

	private var current = 0
	private var last = 0
	private var isFinished = false
	var shouldRunning = true
	private set

	override val waiters: MutableList<Waiter> = mutableListOf(Condition, Timer)

	override fun run() {
		if (!shouldRunning) return
		action(current)
		isFinished = waiters.all { !it.isWaiting }
		waiters.forEach(Waiter::refresh)
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