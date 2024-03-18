package com.fivecentscdnanalytics.calls

import com.fivecentscdnanalytics.data.AdEventData
import com.fivecentscdnanalytics.data.EventData


interface DebugCallback {
    fun dispatchEventData(data: EventData)
    fun dispatchAdEventData(data: AdEventData)
    fun message(message: String)
}
