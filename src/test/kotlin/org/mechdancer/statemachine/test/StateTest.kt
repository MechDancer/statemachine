package org.mechdancer.statemachine.test

import org.mechdancer.statemachine.dsl.stateMachine
import org.mechdancer.statemachine.run
import java.util.concurrent.TimeUnit.SECONDS

fun main(args: Array<String>) {
	val `for` = stateMachine {
		var i = 0

		val init = state {
			doing = { i = 0 }
		}
		val delay = delay {
			time = 1
			unit = SECONDS
		}
		val print = state {
			doing = { println(i) }
		}
		val add = state {
			doing = { i++ }
			after = { i < 20 }
		}
		register(init to add)
		register(add to print)
		register(print to delay)
		register(delay to add)
	}

	`for`.run()
}
