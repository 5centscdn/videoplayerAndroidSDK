package com.fivecentscdnanalytics.data

interface Backend {
    fun send(eventData: EventData)
    fun sendAd(eventData: AdEventData)
}
