package com.fivecentscdnanalytics.collectors

import com.fivecentscdnanalytics.FCCDNAnalytics
import com.fivecentscdnanalytics.config.FCCDNAnalyticsConfig


interface Collector<TPlayer> {
    val impressionId: String
    val config: FCCDNAnalyticsConfig

    fun attachPlayer(player: TPlayer)
    fun detachPlayer()

    fun addDebugListener(listener: FCCDNAnalytics.DebugListener)
    fun removeDebugListener(listener: FCCDNAnalytics.DebugListener)
}
