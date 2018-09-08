import StateTest.*
import org.mechdancer.statemachine.another.IState
import org.mechdancer.statemachine.another.StateMachine

var i = 0

enum class StateTest : IState {
	Init {
		override val loop = false
		override fun invoke() = run { i = 0 }
	},
	Add {
		override val loop = false
		override fun invoke() = run { ++i; Unit }
		override fun before() = i < 20
	},
	Print {
		override val loop = false
		override fun invoke() = run { println(i) }
	}
}

fun main(args: Array<String>) {
	val `for` = StateMachine(Init)
	`for` register (Init to Print)
	`for` register (Print to Add)
	`for` register (Add to Print)

	while (!`for`.done) `for`()
}
