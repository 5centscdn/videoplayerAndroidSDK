package com.fivecentscdnanalytics.data

import com.fivecentscdnanalytics.retryBackend.OnFailureCallback


interface CallbackBackend {
    fun send(eventData: EventData, callback: OnFailureCallback?)
    fun sendAd(eventData: AdEventData, callback: OnFailureCallback?)
}
