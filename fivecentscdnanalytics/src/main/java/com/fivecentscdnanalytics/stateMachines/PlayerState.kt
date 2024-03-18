package com.fivecentscdnanalytics.stateMachines

interface PlayerState<T> {
    val name: String
    fun onEnterState(machine: PlayerStateMachine, data: T?)
    fun onExitState(machine: PlayerStateMachine, elapsedTime: Long, destinationPlayerState: PlayerState<*>)
}
