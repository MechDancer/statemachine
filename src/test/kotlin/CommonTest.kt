import org.mechdancer.statemachine.common.Event
import org.mechdancer.statemachine.common.State
import org.mechdancer.statemachine.common.StateMachine

fun main(args: Array<String>) {
	val state1: State<Pair<Int, Int>, Unit> = { (0 to 0) to Unit }
	val state2: State<Pair<Int, Int>, Boolean> = { it to (it.first <= 100) }
	val state3: State<Pair<Int, Int>, Unit> = { (it.first + 1 to it.second) to Unit }
	val state4: State<Pair<Int, Int>, Unit> = {
		(it.first + it.second).let { sum ->
			println(sum)
			(it.first to sum) to Unit
		}
	}
	val finish: State<Pair<Int, Int>, Unit> = { (0 to 0) to Unit }

	val done1: Event<Pair<Int, Int>, Unit> = { m, _ ->
		StateMachine(state2, m.state)
	}
	val done2: Event<Pair<Int, Int>, Boolean> = { m, v ->
		StateMachine(if (v) state4 else finish, m.state)
	}
	val done3: Event<Pair<Int, Int>, Unit> = { m, _ ->
		StateMachine(state2, m.state)
	}
	val done4: Event<Pair<Int, Int>, Unit> = { m, _ ->
		StateMachine(state3, m.state)
	}

	val map = mapOf<State<Pair<Int, Int>, *>, Event<Pair<Int, Int>, *>>(
		state1 to done1,
		state2 to done2,
		state3 to done3,
		state4 to done4
	)

	var machine = StateMachine(state1, 0 to 0)
	while (!(machine.current === finish))
		machine().let { machine = map[machine.current]!!(it.first, it.second) }
}
