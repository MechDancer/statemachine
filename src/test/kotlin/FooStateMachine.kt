import org.mechdancer.statemachine.legacy.SwitchableStateMachine
import org.mechdancer.statemachine.legacy.blockers.ConditionBlocker
import org.mechdancer.statemachine.legacy.blockers.TimerBlocker

class FooStateMachine : SwitchableStateMachine() {


//	private val condition = ConditionBlocker(false)
//	private val timer = TimerBlocker(3000, TimeUnit.MILLISECONDS)
//
//	init {
//		stateBlockers.add(condition)
//		stateBlockers.add(timer)
//	}

	override fun action(state: Int) {
		when (state) {
			0 -> {
				TimerBlocker.reset(4000)
				TimerBlocker.enable()
				println("timer enable!")
			}
			1 -> {
				println("loop 1")
			}
			2 -> {
				ConditionBlocker.reset(false)
				ConditionBlocker.enable()
				println("condition enable!")
			}
			3 -> {
				println("action 2")
				ConditionBlocker.reset(true)
			}
			4 -> {
				TimerBlocker.reset(6000)
				TimerBlocker.enable()
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
