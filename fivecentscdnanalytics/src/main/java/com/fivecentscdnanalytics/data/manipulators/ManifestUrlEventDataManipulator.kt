package com.fivecentscdnanalytics.data.manipulators

import com.fivecentscdnanalytics.config.FCCDNAnalyticsConfig
import com.fivecentscdnanalytics.adapters.PlayerAdapter
import com.fivecentscdnanalytics.data.EventData

/**
 * Decorates the event data with the m3u8 and mpd url if they are set in the insights configuration.
 */
open class ManifestUrlEventDataManipulator(
    private val playerAdapter: PlayerAdapter,
    private val insightsConfig: FCCDNAnalyticsConfig
) : EventDataManipulator {
    override fun manipulate(data: EventData) {
        val currentSourceMetadata = playerAdapter.currentSourceMetadata
        if (currentSourceMetadata != null) {
            if (currentSourceMetadata.m3u8Url != null) {
                data.m3u8Url = currentSourceMetadata.m3u8Url
            }
            if (currentSourceMetadata.mpdUrl != null) {
                data.mpdUrl = currentSourceMetadata.mpdUrl
            }
        } else {
            if (insightsConfig.m3u8Url != null) {
                data.m3u8Url = insightsConfig.m3u8Url
            }
            if (insightsConfig.mpdUrl != null) {
                data.mpdUrl = insightsConfig.mpdUrl
            }
        }
    }
}
