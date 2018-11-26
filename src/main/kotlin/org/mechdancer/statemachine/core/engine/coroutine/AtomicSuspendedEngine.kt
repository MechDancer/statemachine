package org.mechdancer.statemachine.core.engine.coroutine

import org.mechdancer.statemachine.core.IStateHandler


class AtomicSuspendedEngine(origin: IStateHandler) : ISuspendedEngine {

    private val engine = SuspendedEngine(origin)

    override suspend fun run(): Boolean {
        while (!engine.run());
        return true
    }
}