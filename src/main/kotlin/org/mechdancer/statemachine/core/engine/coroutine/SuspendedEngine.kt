package org.mechdancer.statemachine.core.engine.coroutine

import org.mechdancer.statemachine.core.Ending
import org.mechdancer.statemachine.core.IStateHandler
import kotlin.coroutines.experimental.suspendCoroutine

class SuspendedEngine(origin: IStateHandler) : ISuspendedEngine {

	private var current = origin

	override suspend fun run(): Boolean = suspendCoroutine {
		try {
			current = current.run()
			it.resume(current == Ending.Destination)
		} catch (e: Exception) {
			it.resumeWithException(e)
		}
	}

}