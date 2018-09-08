package org.mechdancer.statemachine.test

import org.mechdancer.statemachine.StateMachine
import org.mechdancer.statemachine.state

fun main(args: Array<String>) {
	var i = 0

	val init = state {
		doing = { i = 0 }
	}

	val add = state {
		before = { i < 20 }
		doing = { i++ }
	}

	val print = state {
		doing = { println(i) }
	}

	val `for` = StateMachine(init)

	`for` register (init to add)
	`for` register (add to print)
	`for` register (print to add)

	while (!`for`.done) {
		`for`.execute()
		Thread.sleep(100)
	}
}
