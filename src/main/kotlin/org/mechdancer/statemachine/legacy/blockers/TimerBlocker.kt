package org.mechdancer.statemachine.legacy.blockers

import java.util.concurrent.TimeUnit

/**
 * 计时阻塞器
 * @param delay 时长
 * @param timeUnit 时长单位
 * @param name 阻塞器名称
 */
open class TimerBlocker(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
                        name: String = "TimerBlocker") : StateBlocker(name) {

	//通用计时阻塞器
	companion object : TimerBlocker(0,name = "CommonTimerBlocker")

	private var target = TimeUnit.MILLISECONDS.convert(delay, timeUnit)
		set(value) {
			field = System.currentTimeMillis() + value
		}


	override fun sync() {
		finished = System.currentTimeMillis() > target
	}

	/**
	 * 重置计时器
	 * @param delay 时长
	 * @param timeUnit 时长单位
	 */
	fun reset(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS) {
		target = TimeUnit.MILLISECONDS.convert(delay, timeUnit)
		reset()
	}
}