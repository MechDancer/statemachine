package org.mechdancer.statemachine.core.engine.coroutine

import org.mechdancer.statemachine.core.Ending
import org.mechdancer.statemachine.core.IStateHandler
import org.mechdancer.statemachine.core.engine.coroutine.ISuspendedEngine
import kotlin.coroutines.experimental.suspendCoroutine

class SuspendedEngine(origin: IStateHandler) : ISuspendedEngine {

	private var current = origin

	override suspend fun run(): Boolean = suspendCoroutine {
		current = current.run()
		it.resume(current == Ending.Destination)
	}

}