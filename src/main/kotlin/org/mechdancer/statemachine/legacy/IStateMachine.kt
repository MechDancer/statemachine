package org.mechdancer.statemachine.legacy

import org.mechdancer.statemachine.legacy.blockers.StateBlocker

/**
 * 状态机接口
 */
interface IStateMachine {
	//状态阻塞器
	val stateBlockers: List<StateBlocker>

	//将状态定义在其中，由状态机调度
	fun action(state: Int)
}