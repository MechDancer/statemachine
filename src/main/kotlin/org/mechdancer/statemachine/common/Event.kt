package org.mechdancer.statemachine.common

//所谓事件，指的是更新状态机内部状态，并驱动状态机跳转状态的函数
typealias Event<TState, TValue> = (StateMachine<TState>, TValue) -> StateMachine<TState>

//默认事件：无条件转移
fun <TState> move(next: State<TState, *>): Event<TState, Unit> =
    { it, _ -> StateMachine(next, it.state) }

//默认事件：无条件转移
fun <TState> stay(): Event<TState, Unit> = { it, _ -> it }

//二分转移
fun <TState> predication(t: State<TState, *>, f: State<TState, *>): Event<TState, Boolean> =
    { it, condition -> StateMachine(if (condition) t else f, it.state) }
