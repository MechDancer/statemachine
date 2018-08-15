import org.mechdancer.statemachine.legacy.SwitchableStateMachine
import org.mechdancer.statemachine.legacy.waiter.Condition
import org.mechdancer.statemachine.legacy.waiter.Timer

class FooStateMachine : SwitchableStateMachine() {


//	private val condition = Condition(false)
//	private val timer = Timer(3000, TimeUnit.MILLISECONDS)
//
//	init {
//		waiters.add(condition)
//		waiters.add(timer)
//	}

	override fun action(state: Int) {
		when (state) {
			0 -> {
				Timer.reset(3000)
				Timer.start()
				println("timer start!")
			}
			1 -> {
				println("loop 1")
			}
			2 -> {
				Condition.reset(false)
				Condition.start()
				println("condition start!")
			}
			3 -> {
				println("loop 2")
				Condition.reset(true)
			}
			4 -> {
				Timer.reset(2000)
				Timer.start()
				println("timer reset")
			}
			5 -> {
				println("loop 3")
			}
			6 -> {
				println("finished")
				terminate()
			}
		}
	}

}