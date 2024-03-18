package com.fivecentscdnanalytics.data.manipulators

import com.fivecentscdnanalytics.data.EventData


interface EventDataManipulator {
    fun manipulate(data: EventData)
}
