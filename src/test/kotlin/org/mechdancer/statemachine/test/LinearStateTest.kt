package org.mechdancer.statemachine.test

import org.mechdancer.statemachine.builder.linearStateMachine
import org.mechdancer.statemachine.run
import java.util.concurrent.TimeUnit.SECONDS

fun main(args: Array<String>) {
	val `for` = linearStateMachine {
		var i = 0
		once {
			todo = { i = 0 }
		}
		call(20) {
			todo = { println(++i) }
		}
		forever {
			todo = { println(++i) }
			until = { i % 99 == 12 }
		}
		delay {
			time = 1
			unit = SECONDS
		}
		once {
			todo = { println("bye~") }
		}
	}

	`for`.run()
}
