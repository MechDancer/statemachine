package org.mechdancer.statemachine.another

class StateBuilderDsl {
	private var before = { true }

	private var doing = {}

	private var after = { true }

	var loop = false

	fun before(block: () -> Boolean) {
		before = block
	}

	fun doing(block: () -> Unit) {
		doing = block
	}

	fun after(block: () -> Boolean) {
		after = block
	}

	internal fun build() = object : IState {
		override val loop: Boolean = this@StateBuilderDsl.loop
		override fun doing() =
				this@StateBuilderDsl.doing()

		override fun after(): Boolean =
				this@StateBuilderDsl.after()

		override fun before(): Boolean =
				this@StateBuilderDsl.before()

	}
}

fun state(block: StateBuilderDsl.() -> Unit) =
		StateBuilderDsl().apply(block).build()