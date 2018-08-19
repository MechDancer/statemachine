package org.mechdancer.statemachine.common

//所谓状态，指的是更新内部状态并返回结论的函数
typealias State<TState, TReturn> = (TState) -> Pair<TState, TReturn>

//所谓事件，指的是更新状态机内部状态，并驱动状态机跳转状态的函数
typealias Event<TState, TValue> = (StateMachine<TState>, TValue) -> StateMachine<TState>
