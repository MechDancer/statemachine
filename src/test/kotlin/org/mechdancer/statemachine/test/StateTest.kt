package org.mechdancer.statemachine.test

import org.mechdancer.statemachine.StateMachine
import org.mechdancer.statemachine.delay
import org.mechdancer.statemachine.state
import java.util.concurrent.TimeUnit.SECONDS

fun main(args: Array<String>) {
	var i = 0

	val init = state {
		doing = { i = 0 }
	}

	val `for` = StateMachine(init)

	val print = state {
		doing = { println(i) }
	}

	val add = state {
		before = { i < 20 }
		doing = {
			i++
			delay(1L to SECONDS) { `for`.transfer(print) }
		}
	}

	`for` register (init to add)
	`for` register (print to add)

	while (i < 20) {
		`for`.execute()
		Thread.sleep(100)
	}
}
