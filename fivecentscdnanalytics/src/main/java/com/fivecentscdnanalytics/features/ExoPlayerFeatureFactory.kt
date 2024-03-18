package com.fivecentscdnanalytics.features

import com.fivecentscdnanalytics.FCCDNAnalytics
import com.fivecentscdnanalytics.license.FeatureConfigContainer
import com.fivecentscdnanalytics.features.errordetails.ErrorDetailBackend
import com.fivecentscdnanalytics.features.errordetails.ErrorDetailTracking
import com.fivecentscdnanalytics.features.httprequesttracking.HttpRequestTracking
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer

class ExoPlayerFeatureFactory(private val insights: FCCDNAnalytics, private val player: ExoPlayer) : FeatureFactory {
    override fun createFeatures(): Collection<Feature<FeatureConfigContainer, *>> {
        val features = mutableListOf<Feature<FeatureConfigContainer, *>>()
        var httpRequestTracking: HttpRequestTracking? = null
        if (player is SimpleExoPlayer) {
            val httpRequestTrackingAdapter = ExoPlayerHttpRequestTrackingAdapter(player, insights.onAnalyticsReleasingObservable)
            httpRequestTracking = HttpRequestTracking(httpRequestTrackingAdapter)
        }
        val errorDetailsBackend = ErrorDetailBackend(insights.config.config, insights.context)
        val errorDetailTracking = ErrorDetailTracking(insights.context, insights.config, insights, errorDetailsBackend, httpRequestTracking, insights.onErrorDetailObservable)
        features.add(errorDetailTracking)
        return features
    }
}
