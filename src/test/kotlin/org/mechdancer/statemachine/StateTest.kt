package org.mechdancer.statemachine

import org.junit.Test
import org.mechdancer.statemachine.builder.delay
import org.mechdancer.statemachine.builder.state
import org.mechdancer.statemachine.core.IState
import org.mechdancer.statemachine.core.Watchdog
import org.mechdancer.statemachine.stateMachine
import java.util.concurrent.TimeUnit.SECONDS

class StateTest {

    @Test
    fun test() {
        val `for` = stateMachine<IState> {
            var i = 0

            val init = state {
                val dog = Watchdog(this@stateMachine, null, null, 5, SECONDS)
                before = { dog.start(); ACCEPT }
                doing = { i = 0 }
            }
            val delay = delay {
                time = 1
                unit = SECONDS
            }
            val print = state {
                doing = { println(i) }
            }
            val add = state {
                doing = { i++ }
                after = { i < 20 }
            }
            register(init to add)
            register(add to print)
            register(print to delay)
            register(delay to add)

            startFrom(init)
        }

        `for`.run()
    }
}
