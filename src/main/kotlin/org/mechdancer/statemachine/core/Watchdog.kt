package org.mechdancer.statemachine.core

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

/**
 * 看门狗
 * 允许状态机在指定时间后直接跳转到指定状态
 * @param machine 可迁移状态机
 * @param source 源状态（null 表示不限）
 * @param target 目标状态
 * @param time 狗叫延时
 * @param unit 时间单位
 */
class Watchdog<T : IState>(
    private val machine: IExternalTransferable<T>,
    private val source: T?,
    private val target: T?,
    private val time: Long,
    private val unit: TimeUnit) {

    //一条狗只能注册一次叫
    private val lock = ReentrantLock()

    /** 命令狗准备叫 */
    fun start() =
        lock.tryLock().also {
            if (it)
                scheduler.schedule({
                    if (source in setOf(null, machine.current))
                        machine goto target
                    lock.unlock()
                }, time, unit)
        }

    private companion object {
        /** 默认调度器 */
        val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(0)
    }
}
