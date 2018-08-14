import org.mechdancer.statemachine.legacy.IStateMachine
import org.mechdancer.statemachine.legacy.waiter.Condition
import org.mechdancer.statemachine.legacy.waiter.Timer
import org.mechdancer.statemachine.legacy.waiter.Waiter
import java.util.concurrent.TimeUnit

class FooStateMachine : IStateMachine {

	private val condition = Condition(false)
	private val timer = Timer(3000, TimeUnit.MILLISECONDS)

	override val waiters: MutableList<Waiter> = mutableListOf(condition, timer)

	override fun run(state: Int) {
		when (state) {
			0 -> {
				timer.start()
				println("timer start!")
			}
			1 -> {
				println("loop 1")
			}
			2 -> {
				condition.start()
				println("condition start!")
			}
			3 -> {
				println("loop 2") //执行 2次
				condition.reset(true)
			}
			4 -> {
				timer.reset(2000, TimeUnit.MILLISECONDS)
				timer.start()
				println("timer reset")
			}
			5 -> {
				println("loop 3")
			}
			6 -> {
				println("finished")
			}
		}
	}

}