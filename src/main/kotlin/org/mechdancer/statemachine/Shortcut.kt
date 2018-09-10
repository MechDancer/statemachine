package org.mechdancer.statemachine

import org.mechdancer.statemachine.core.IState
import org.mechdancer.statemachine.core.IStateMachine

/** “事件”是操作状态转移并返回是否发生转移的函数 */
typealias Event = () -> Boolean

/** 阻塞并执行状态机，直到结束 */
fun IStateMachine<*>.run() {
	while (!isCompleted) this()
}

/** 构造基础版本状态机 */
fun <T : IState> stateMachine(block: IStateMachine<T>.() -> Unit): IStateMachine<T> =
	IStateMachine.create<T>().apply(block)
