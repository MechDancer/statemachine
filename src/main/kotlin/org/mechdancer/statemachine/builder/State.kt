package org.mechdancer.statemachine.builder

import org.mechdancer.statemachine.core.IState

/** dsl 建造者 */
data class StateBuilderDsl(
    var before: () -> Boolean = { true },
    var doing: () -> Unit = {},
    var after: () -> Boolean = { true },
    var loop: Boolean = false
)

/** dsl 构造状态 */
fun state(block: StateBuilderDsl.() -> Unit) =
    StateBuilderDsl().apply(block).let {
        object : IState {
            override val loop = it.loop
            override fun doing() = it.doing()
            override fun after() = it.after()
            override fun before() = it.before()
        }
    }
