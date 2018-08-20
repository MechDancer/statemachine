import org.mechdancer.statemachine.common.*

typealias S = Pair<Int, Int>

fun main(args: Array<String>) {

	val init: State<S, Unit> = { (0 to 0) to Unit }
	val check: State<S, Boolean> = { it to (it.first <= 100) }
	val update: State<S, Unit> = { (it.first + 1 to it.second) to Unit }
	val body: State<S, Unit> = {
		(it.first + it.second).let { sum ->
			println(sum)
			(it.first to sum) to Unit
		}
	}
	val final = Final<S>()

	val engine = Engine(
		init to move(check),
		check to predication(body, final),
		body to move(update),
		update to move(check),
		final to stay()
	)

	engine.startOn(StateMachine(init, 0 to 0))
	while (engine.drive());
}
