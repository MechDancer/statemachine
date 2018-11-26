import org.junit.Test

class Tester {
    @Test
    fun test() {
        val foo = FooStateMachine()

        while (foo.shouldRunning) {
            foo.run()
            Thread.sleep(100)
        }

    }
}