package org.mechdancer.statemachine

/** 阻塞并执行状态机，直到结束 */
fun <T : IState> StateMachine<T>.run() {
	while (!done) execute()
}
