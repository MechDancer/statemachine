package org.mechdancer.statemachine.another

/**
 * 状态机
 * @param T 状态类型。不是强制的，但为了安全环保，建议定义一个枚举。
 * @param origin 初始状态
 */
class StateMachine<T : IState>(private val origin: T) {
	//状态转移互斥锁
	private val lock = Any()

	/** 当前状态 */
	var current: T? = origin
		private set

	/** 目标状态表 */
	private val targets = mutableMapOf<T, MutableSet<T>>()

	/** 执行完毕检查 */
	val done get() = current === null

	/**
	 * 构造事件，但不注册到事件通路
	 * @param pair 事件对
	 * @return 转移事件
	 */
	fun event(pair: Pair<T, T>) = {
		synchronized(lock) {
			(current === pair.first && pair.first.after() && pair.second.before())
					.also { if (it) current = pair.second }
		}
	}

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
	fun execute() {
		if (current == null) return
		val current = current!!
		current.doing()
		synchronized(lock) {
			if (this.current === current)
				this.current = targets[current]
						?.firstOrNull { event(current to it)() }
						?: current.takeIf { current.loop && event(it to it)() }
		}
	}

	/**
	 * 无源跳转
	 * 不判断当前状态，直接去往目标状态
	 */
	fun transfer(target: T?) {
		synchronized(lock) {
			if (current?.after() != false && target?.before() != false) current = target
		}
	}

	/**
	 * 无源跳转到初始状态
	 */
	fun reset() = transfer(origin)
}
