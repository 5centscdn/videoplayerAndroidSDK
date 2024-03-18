package com.fivecentscdnanalytics.data.manipulators

interface EventDataManipulatorPipeline {
    fun clearEventDataManipulators()
    fun registerEventDataManipulator(manipulator: EventDataManipulator)
}
