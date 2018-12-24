package org.mechdancer.statemachine

import org.mechdancer.statemachine.core.IInvokable
import org.mechdancer.statemachine.core.IState
import org.mechdancer.statemachine.core.StandardMachine

const val ACCEPT = true  //接受：同意转移
const val REJECT = false //驳回：拒绝转移

/** “事件”是操作状态转移并返回是否发生转移的函数 */
typealias Event = () -> Boolean

/** 阻塞并执行状态机，直到结束 */
fun IInvokable<*>.run() {
    while (!isIdle()) this()
}

/** 构造基础版本状态机 */
fun <T : IState> stateMachine(block: StandardMachine<T>.() -> Unit) =
    StandardMachine<T>().apply(block)
