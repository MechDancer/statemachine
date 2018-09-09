package org.mechdancer.statemachine.dsl

import org.mechdancer.statemachine.StateMachine
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS

/** 延时缓存 */
data class TimeDsl(
	var time: Long = 0,
	var unit: TimeUnit = MILLISECONDS
) {
	val nano get() = unit.toNanos(time)
}

/** dsl构造延时状态 */
fun delay(block: TimeDsl.() -> Unit) =
	state {
		var start = 0L
		loop = true
		before = { start = System.nanoTime(); StateMachine.ACCEPT }
		after = TimeDsl().apply(block).nano
			.let { { (System.nanoTime() - start) > it } }
	}
