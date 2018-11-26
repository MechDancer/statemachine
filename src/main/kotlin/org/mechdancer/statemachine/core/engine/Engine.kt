package org.mechdancer.statemachine.core.engine

import org.mechdancer.statemachine.core.Ending
import org.mechdancer.statemachine.core.IStateHandler

/**
 * 驱动器
 * 调用时返回下一跳
 */
class Engine(origin: IStateHandler) : IEngine {
    private var current = origin

    /**
     * 执行一个状态并向后跳转
     */
    override fun run(): Boolean {
        current = current.run()
        return current == Ending.Destination
    }
}