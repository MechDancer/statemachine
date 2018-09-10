package org.mechdancer.statemachine.core.impl

import org.mechdancer.statemachine.core.IState
import org.mechdancer.statemachine.core.StateMachine
import java.util.concurrent.atomic.AtomicInteger

class LinearStateMachineImpl(machine: StateMachine<LinearState>)
	: StateMachine<LinearStateMachineImpl.LinearState> by machine {

	/** 基于次数执行的线性状态 */
	abstract class LinearState(times: Int) : IState {
		var ttl = AtomicInteger(times)
	}


}