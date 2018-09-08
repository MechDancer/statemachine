package org.mechdancer.statemachine.test

import org.mechdancer.statemachine.IState
import org.mechdancer.statemachine.StateMachine
import org.mechdancer.statemachine.state

var i = 0

enum class StateTest : IState {
	Init {
		override val loop = false
		override fun doing() = run { i = 0 }
	},
	Add {
		override val loop = false
		override fun doing() = run { ++i; Unit }
		override fun before() = i < 20
	},
	Print {
		override val loop = false
		override fun doing() = run { println(i) }
	}
}

fun main(args: Array<String>) {

	val init = state {
		doing { i = 0 }
	}

	val add = state {
		doing { i++ }
		before { i < 20 }
	}

	val print = state {
		doing { println(i) }
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
