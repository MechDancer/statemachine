package org.mechdancer.statemachine

/**
 * 状态机
 * @param T 状态类型。不是强制的，但为了安全环保，建议定义一个枚举。
 * @param origin 初始状态
 */
class StateMachine<T : IState>(private val origin: T) {
	companion object {
		const val ACCEPT = true
		const val REJECT = false
	}

	//状态转移互斥锁
	private val lock = Any()

	//按检查结果转移
	private infix fun (() -> Boolean).trans(target: T?) =
		synchronized(lock) { this().also { if (it) current = target } }

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
	fun event(pair: Pair<T, T>) =
		{ { current === pair.first && pair.first.after() && pair.second.before() } trans pair.second }

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
		current?.let { last ->
			last.doing();
			{ current === last } trans
				(targets[last]
					?.firstOrNull { event(last to it)() }
					?: last.takeIf { last.loop })
		}
	}

	/**
	 * 无源跳转
	 * 不判断当前状态，直接去往目标状态
	 * @param target 目标状态
	 * @return 是否发生转移
	 */
	infix fun transfer(target: T?) =
		{ current?.after() != REJECT && target?.before() != REJECT } trans target


	fun reset() = transfer(origin)
}
