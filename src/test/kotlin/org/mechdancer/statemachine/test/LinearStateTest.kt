package org.mechdancer.statemachine.test

import org.mechdancer.statemachine.builder.linearStateMachine
import org.mechdancer.statemachine.run
import java.util.concurrent.TimeUnit.SECONDS

fun main(args: Array<String>) {
	val `for` = linearStateMachine {
		var i = 0
		once { i = 0 }
		call(20) { println(++i) }
		delay { time = 1; unit = SECONDS }
		once { println("hello world!") }
	}

	`for`.run()
}
