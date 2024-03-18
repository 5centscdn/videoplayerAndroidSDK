package com.fivecentscdnanalytics.adapters

import com.fivecentscdnanalytics.interfaces.Observable
import com.fivecentscdnanalytics.data.AdModuleInformation
import com.fivecentscdnanalytics.listener.AdAnalyticsEventListener

interface AdAdapter : Observable<AdAnalyticsEventListener> {
    fun release()
    val isLinearAdActive: Boolean
    val moduleInformation: AdModuleInformation
    val isAutoplayEnabled: Boolean?
}
