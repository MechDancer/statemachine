package org.mechdancer.statemachine.builder

import org.mechdancer.statemachine.ACCEPT
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS

/** 延时缓存 */
data class DelayBuilderDsl(
		var time: Long = 0,
		var unit: TimeUnit = MILLISECONDS
) {
	val nano get() = unit.toNanos(time)
}

/** dsl构造延时状态 */
fun delay(block: DelayBuilderDsl.() -> Unit) =
		state {
			var start = 0L
			loop = true
			before = { start = System.nanoTime(); ACCEPT }
			after = DelayBuilderDsl().apply(block).nano
					.let { { (System.nanoTime() - start) > it } }
		}
