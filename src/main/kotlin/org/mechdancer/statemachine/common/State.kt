package org.mechdancer.statemachine.common

//所谓状态，指的是更新内部状态并返回结论的函数
typealias State<TState, TReturn> = (TState) -> Pair<TState, TReturn>

//状态机常常有个“出口”，或者说停机状态
class Final<TState> : State<TState, Unit> {
	override fun invoke(it: TState) = it to Unit
}
