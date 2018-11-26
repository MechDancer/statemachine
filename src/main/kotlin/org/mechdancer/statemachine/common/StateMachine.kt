package org.mechdancer.statemachine.common

/**
 * 状态机是一个状态和一个在所有可达状态间共享的内部状态
 * @param T 内部状态类型
 */
data class StateMachine<T>(
    val current: State<T, *>,
    val state: T
) {
    operator fun invoke() =
        current(state).let { StateMachine(current, it.first) to it.second }
}
