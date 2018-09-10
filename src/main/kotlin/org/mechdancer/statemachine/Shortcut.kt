package org.mechdancer.statemachine

/** “事件”是操作状态转移并返回是否发生转移的函数 */
typealias Event = () -> Boolean

/** 阻塞并执行状态机，直到结束 */
fun <T : IState> StateMachine<T>.run() {
	while (!done) execute()
}
