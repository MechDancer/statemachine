import org.junit.Test
import org.mechdancer.statemachine.legacy.StateMachineEngine

class Tester {
	@Test
	fun test() {
		val engine = StateMachineEngine(FooStateMachine())

		while (true) {
			engine.run()
			Thread.sleep(100)
		}


	}
}