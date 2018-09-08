package org.mechdancer.statemachine.another

interface IState {
	fun preCheck(): Boolean
	operator fun invoke()
	fun sufCheck(): Boolean
}
