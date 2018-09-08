package org.mechdancer.statemachine.another

class StateMachine<T : IState>(origin: T) {
	private val lock = Any()

	//构造状态转移事件
	private fun event(pair: Pair<T, T>) = {
		synchronized(lock) {
			(current === pair.first && pair.first.sufCheck() && pair.second.preCheck())
				.also { if (it) current = pair.second }
		}
	}

	/** 当前状态 */
	var current: T? = origin
		private set

	/** 目标状态表 */
	private val targets = mutableMapOf<T, MutableSet<T>>()

	/** 执行完毕 */
	val done get() = current === null

	/**
	 * 注册事件通路
	 * @param pair 事件对
	 * @return 转移事件
	 */
	infix fun register(pair: Pair<T, T>) =
		event(pair).also {
			if (pair.first === pair.second) return@also
			targets[pair.first]?.add(pair.second)
				?: run { targets[pair.first] = mutableSetOf(pair.second) }
			targets[pair.second]
				?: run { targets[pair.second] = mutableSetOf() }
		}

	/**
	 * 驱动状态机运行一个周期
	 * 运行结束后若外部状态未引发状态转移则触发一个状态转移事件
	 */
	operator fun invoke() {
		if (current == null) return
		val current = current!!
		current()
		synchronized(lock) {
			if (this.current === current)
				this.current = targets[current]
					?.firstOrNull { event(current to it)() }
			//TODO ?: current.takeIf { event(it to it)() } //直接去掉有bug，死循环
		}
	}
}
