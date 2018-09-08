package org.mechdancer.statemachine

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private val scheduler = Executors.newScheduledThreadPool(0)

fun delay(time: Pair<Long, TimeUnit>, event: () -> Boolean) =
	scheduler.schedule(event, time.first, time.second)
