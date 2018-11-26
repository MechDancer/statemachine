package org.mechdancer.statemachine.legacy.blockers

/**
 * 条件阻塞器
 * 传入一个 `boolean`，直接作为阻塞器状态。
 * @param boolean 阻塞器状态
 */
open class ConditionBlocker(private var boolean: Boolean,
                            name: String = "ConditionBlocker") : StateBlocker(name) {

    companion object : ConditionBlocker(true, "CommonConditionBlocker")

    override fun sync() {
        finished = boolean
    }

    /**
     * 重置条件
     * @param condition 条件
     */
    fun reset(condition: Boolean) {
        boolean = condition
        reset()
    }
}