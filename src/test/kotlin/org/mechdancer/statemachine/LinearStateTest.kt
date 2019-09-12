package org.mechdancer.statemachine

import org.junit.Test
import org.mechdancer.statemachine.builder.linearStateMachine
import java.util.concurrent.TimeUnit.SECONDS

class LinearStateTest {

    @Test
    fun test() {
        val `for` = linearStateMachine {
            var i = 0
            once {
                i = 0
            }
            call(20) {
                println(++i)
            }
            forever({
                        i % 99 == 12
                    }) {
                println(++i)
            }
            delay {
                time = 1
                unit = SECONDS
            }
            once {
                println("bye~")
            }
        }

        `for`.run()
    }
}
