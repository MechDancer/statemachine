package org.mechdancer.statemachine.legacy.blockers

/**
 * 状态阻塞器
 * 开启后将在下一个状态执行后根据条件阻塞状态机的运行，条件由该
 * 抽象类的子类根据具体实现决定。状态阻塞器定义为可复用，故所有子类
 * 应实现 `reset` 函数。
 * @param name
 */
abstract class StateBlocker(val name: String) {

	//阻塞器状态，由状态机获取
	internal var isBlocking: Boolean = false
		private set

	//由子类表示任务是否完成，即是否应阻塞
	protected var finished: Boolean = false

	//阻塞器是否启用
	private var isRunning: Boolean = false

	internal fun refresh() {
		sync()
		isBlocking = if (isRunning)
			!finished
		else false
	}

	protected abstract fun sync()

	protected fun reset() {
		isBlocking = false
		isRunning = false
		finished = false
	}

	fun enable() {
		isRunning = true
	}

	fun disable() {
		isRunning = false
	}
}
