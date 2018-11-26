package org.mechdancer.statemachine.common

class Engine<T>(
    vararg maps: Pair<State<T, *>, Event<T, *>>
) {
    private val structure: Map<State<T, *>, Event<T, *>> = maps.toMap()
    private lateinit var current: StateMachine<T>

    fun startOn(env: StateMachine<T>) {
        current = env
    }

    fun drive(): Boolean {
        current().let {
            current = structure[current.current]!!(it.first, it.second)
        }
        return current.current !is Final
    }
}
