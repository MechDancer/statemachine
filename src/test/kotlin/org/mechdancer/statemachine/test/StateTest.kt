package org.mechdancer.statemachine.test

import org.mechdancer.statemachine.IState
import org.mechdancer.statemachine.StateMachine.Companion.ACCEPT
import org.mechdancer.statemachine.WatchDog
import org.mechdancer.statemachine.dsl.delay
import org.mechdancer.statemachine.dsl.state
import org.mechdancer.statemachine.machine
import org.mechdancer.statemachine.run
import java.util.concurrent.TimeUnit.SECONDS

fun main(args: Array<String>) {
	val `for` = machine<IState> {
		var i = 0

		val init = state {
			val dog = WatchDog(this@machine, null, null, 5, SECONDS)
			before = { dog.start(); ACCEPT }
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

		startFrom(init)
	}

	`for`.run()
}
