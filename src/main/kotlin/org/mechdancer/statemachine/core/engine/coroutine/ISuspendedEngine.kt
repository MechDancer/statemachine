package org.mechdancer.statemachine.core.engine.coroutine

interface ISuspendedEngine {

    suspend fun run(): Boolean

}