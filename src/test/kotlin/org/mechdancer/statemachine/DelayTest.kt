package org.mechdancer.statemachine

import org.junit.Test
import org.mechdancer.statemachine.builder.linearStateMachine
import kotlin.system.measureNanoTime

class DelayTest {

    @Test
    fun test() {
        val stateMachine = linearStateMachine {
            once { } //TODO
            delay { time = 3000 }
            once {
                println("finish")
            }
        }

        println(measureNanoTime {
            while (!stateMachine.isIdle)
                stateMachine()
        } / 1E9)
    }
}