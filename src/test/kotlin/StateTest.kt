import StateTest.*
import org.mechdancer.statemachine.another.IState
import org.mechdancer.statemachine.another.StateMachine

var i = 0

enum class StateTest : IState {
	Init {
		override fun invoke() = run { i = 0 }
		override fun preCheck() = true
		override fun sufCheck() = true
	},
	Add {
		override fun invoke() = run { ++i; Unit }
		override fun preCheck() = i < 20
		override fun sufCheck() = true
	},
	Print {
		override fun invoke() = run { println(i) }
		override fun preCheck() = true
		override fun sufCheck() = true
	}
}

fun main(args: Array<String>) {
	val _for = StateMachine(Init)
	_for register (Init to Print)
	_for register (Print to Add)
	_for register (Add to Print)
	while (!_for.done) _for()
}
