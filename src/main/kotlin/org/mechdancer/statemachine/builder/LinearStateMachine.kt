package org.mechdancer.statemachine.builder

import org.mechdancer.statemachine.ACCEPT
import org.mechdancer.statemachine.core.IExternalAutoTransferable
import org.mechdancer.statemachine.core.IState
import org.mechdancer.statemachine.core.StandardMachine
import org.mechdancer.statemachine.then
import java.util.concurrent.atomic.AtomicInteger

/** 线性状态机缓存 */
class LinearStateMachineBuilderDsl {
    private val machine = StandardMachine<LinearState>()
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
    fun call(times: Int, until: (Int) -> Boolean = { true }, block: () -> Unit) =
        addState(times, LinearStateBuilderDsl(block, until))

    private fun addState(times: Int, builder: LinearStateBuilderDsl) =
        (object : LinearState(times) {
            override val loop = true

            override fun before(): Boolean {
                ttl.set(times)
                return ACCEPT
            }

            override fun doing() {
                ttl.decrementAndGet()
                builder.todo()
            }

            override fun after() =
                ttl.get().let { rest -> rest <= 0 || builder.until(times - rest) }
        }).add()

    /** 构造状态，只循环 1 次 */
    fun once(until: (Int) -> Boolean = { true }, block: () -> Unit) =
        call(1, until, block)

    /** 构造状态，无限循环 */
    fun forever(until: (Int) -> Boolean = { true }, block: () -> Unit) =
        call(Int.MAX_VALUE, until, block)

    /** 延时 */
    fun delay(block: DelayBuilderDsl.() -> Unit) =
        object : LinearState(Int.MAX_VALUE) {
            val delay = DelayBuilderDsl().apply(block).nano
            var start = 0L
            var busy = false

            override val loop = true
            override fun doing() {
                if (!busy) {
                    start = System.nanoTime()
                    busy = true
                }
            }

            override fun before() = ACCEPT
            override fun after() = ((System.nanoTime() - start) > delay).then { busy = false }
        }.add()

    /** 获取时添加末状态以便退出 */
    fun build(): IExternalAutoTransferable<LinearState> {
        object : LinearState(0) {
            override val loop = false
            override fun before() = true
            override fun doing() = Unit
            override fun after() = true
        }.add()
        return machine
    }

    /** 基于次数执行的线性状态 */
    abstract class LinearState(val times: Int) : IState {
        /** 剩余次数 */
        var ttl = AtomicInteger(times)
    }

    /** 用于线性状态机的状态配置 */
    data class LinearStateBuilderDsl(
        var todo: () -> Unit = {},

        /** 跳出条件 := 执行次数 -> 能否跳出 */
        var until: (Int) -> Boolean = { true }
    )
}

/** 构造线性状态机 */
fun linearStateMachine(block: LinearStateMachineBuilderDsl.() -> Unit) =
    LinearStateMachineBuilderDsl().apply(block).build()
