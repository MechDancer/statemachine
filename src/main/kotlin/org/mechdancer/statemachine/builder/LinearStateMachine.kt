package org.mechdancer.statemachine.builder

import org.mechdancer.statemachine.core.IState
import org.mechdancer.statemachine.core.StateMachine
import org.mechdancer.statemachine.core.StateMachine.Companion.ACCEPT
import java.util.concurrent.atomic.AtomicInteger

/** 线性状态机缓存 */
class LinearStateMachineBuilderDsl {
	private var machine = StateMachine.create<LinearState>()
	private var last: LinearState? = null
	private fun LinearState.add() = also {
		machine.startFrom(it)
		last?.let { last -> machine register (last to it) }
		last = it
	}

	/**
	 * 构造状态，循环 time 次
	 * 第一个构造的状态视作初始状态
	 */
	fun call(times: Int, block: () -> Unit) =
		object : LinearState(times) {
			override val loop = true

			override fun before(): Boolean {
				ttl.set(times)
				return ACCEPT
			}

			override fun doing() {
				ttl.decrementAndGet()
				block()
			}

			override fun after() =
				ttl.get() <= 0
		}.add()

	/** 构造状态，只循环 1 次 */
	fun once(block: () -> Unit) = call(1, block)

	/** 构造状态，无限循环 */
	fun forever(block: () -> Unit) = call(Int.MAX_VALUE, block)

	/** 获取时添加末状态以便退出 */
	fun build(): StateMachine<LinearState> {
		object : LinearState(0) {
			override val loop = false
			override fun before() = true
			override fun doing() = Unit
			override fun after() = true
		}.add()
		return machine
	}

	/** 基于次数执行的线性状态 */
	abstract class LinearState(times: Int) : IState {
		var ttl = AtomicInteger(times)
	}
}

/** 构造线性状态机 */
fun linearStateMachine(block: LinearStateMachineBuilderDsl.() -> Unit) =
	LinearStateMachineBuilderDsl().apply(block).build()
