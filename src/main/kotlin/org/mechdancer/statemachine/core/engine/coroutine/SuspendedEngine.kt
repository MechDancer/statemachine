package org.mechdancer.statemachine.core.engine.coroutine

import org.mechdancer.statemachine.core.Ending
import org.mechdancer.statemachine.core.IStateHandler
import kotlin.coroutines.suspendCoroutine

class SuspendedEngine(origin: IStateHandler) : ISuspendedEngine {

    private var current = origin

    override suspend fun run(): Boolean = suspendCoroutine {
        runCatching {
            current = current.run()
            current == Ending.Destination
        }.let(it::resumeWith)
    }

}