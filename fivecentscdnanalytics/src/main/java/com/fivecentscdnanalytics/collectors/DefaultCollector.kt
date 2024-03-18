package com.fivecentscdnanalytics.collectors

import android.content.Context
import com.fivecentscdnanalytics.FCCDNAnalytics
import com.fivecentscdnanalytics.config.FCCDNAnalyticsConfig
import com.fivecentscdnanalytics.adapters.PlayerAdapter
import com.fivecentscdnanalytics.data.DeviceInformationProvider

abstract class DefaultCollector<TPlayer> protected constructor(private val insights: FCCDNAnalytics) {

    val impressionId: String
        get() = insights.impressionId

    val config: FCCDNAnalyticsConfig
        get() = insights.config

    protected abstract fun createAdapter(player: TPlayer, insights: FCCDNAnalytics): PlayerAdapter

    fun attachPlayer(player: TPlayer) {
        val adapter = createAdapter(player, insights)
        insights.attach(adapter)
    }

    fun detachPlayer() {
        insights.detachPlayer()
    }


    fun addDebugListener(listener: FCCDNAnalytics.DebugListener) {
        insights.addDebugListener(listener)
    }

    fun removeDebugListener(listener: FCCDNAnalytics.DebugListener) {
        insights.removeDebugListener(listener)
    }

    companion object {
        fun createAnalytics(insightsConfig: FCCDNAnalyticsConfig, context: Context, userAgent: String): FCCDNAnalytics {
            val deviceInformationProvider = DeviceInformationProvider(context, userAgent)
            return FCCDNAnalytics(
                insightsConfig,
                context,
                deviceInformationProvider
            )
        }
    }
}
