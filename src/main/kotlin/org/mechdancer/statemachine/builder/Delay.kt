package org.mechdancer.statemachine.builder

import org.mechdancer.statemachine.ACCEPT
import org.mechdancer.statemachine.then
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS

/** 延时缓存 */
data class DelayBuilderDsl(
    var time: Long = 0,
    var unit: TimeUnit = MILLISECONDS
) {
    val nano get() = unit.toNanos(time)
}

/** dsl 构造延时状态 */
fun delay(block: DelayBuilderDsl.() -> Unit) =
    state {
        val delay = DelayBuilderDsl().apply(block).nano
        var start = 0L
        var busy = false

        loop = true
        doing = {
            if (!busy) {
                start = System.nanoTime()
                busy = true
            }
        }

        before = { ACCEPT }
        after = { ((System.nanoTime() - start) > delay).then { busy = false } }
    }
