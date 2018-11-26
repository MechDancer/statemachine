package org.mechdancer.statemachine.core

/**
 * 状态成员
 * 执行后指定下一跳
 */
interface IStateHandler {
    fun run(): IStateHandler

    object Nothing : IStateHandler {
        override fun run() = this
    }
}