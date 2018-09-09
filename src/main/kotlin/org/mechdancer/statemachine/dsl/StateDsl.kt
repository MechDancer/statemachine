package org.mechdancer.statemachine.dsl

import org.mechdancer.statemachine.IState

/** DSL缓存 */
data class StateDsl(
	var before: () -> Boolean = { true },
	var doing: () -> Unit = {},
	var after: () -> Boolean = { true },
	var loop: Boolean = false
)

/** dsl构造状态 */
fun state(block: StateDsl.() -> Unit) =
	StateDsl().apply(block).let {
		object : IState {
			override val loop = it.loop
			override fun doing() = it.doing()
			override fun after() = it.after()
			override fun before() = it.before()
		}
	}
