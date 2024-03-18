package com.fivecentscdnanalytics.features.errordetails

import android.content.Context
import com.fivecentscdnanalytics.config.FCCDNAnalyticsConfig
import com.fivecentscdnanalytics.interfaces.ImpressionIdProvider
import com.fivecentscdnanalytics.interfaces.Observable
import com.fivecentscdnanalytics.features.Feature
import com.fivecentscdnanalytics.features.httprequesttracking.HttpRequestTracking
import com.fivecentscdnanalytics.license.FeatureConfigContainer
import com.fivecentscdnanalytics.utils.Util

// TODO we also need to track errors from other sources, not just the player.
// Should be streamlined and go through the 5CentsCDNInsights class

class ErrorDetailTracking(private val context: Context, private val insightsConfig: FCCDNAnalyticsConfig, private val impressionIdProvider: ImpressionIdProvider, private val backend: ErrorDetailBackend, private val httpRequestTracking: HttpRequestTracking?, private vararg val observables: Observable<OnErrorDetailEventListener>) :
        Feature<FeatureConfigContainer, ErrorDetailTrackingConfig>(),
        OnErrorDetailEventListener {
    private var errorIndex: Long = 0
    init {
        observables.forEach { it.subscribe(this) }
    }

    override fun extractConfig(featureConfigs: FeatureConfigContainer) = featureConfigs.errorDetails

    override fun configured(authenticated: Boolean, config: ErrorDetailTrackingConfig?) {
        val maxRequests = config?.numberOfHttpRequests ?: 0
        httpRequestTracking?.configure(maxRequests)
        backend.limitHttpRequestsInQueue(maxRequests)
    }

    override fun enabled() {
        backend.enabled = true
        backend.flush()
    }

    override fun disabled() {
        httpRequestTracking?.disable()
        backend.clear()
        observables.forEach { it.unsubscribe(this) }
    }

    override fun reset() {
        httpRequestTracking?.reset()
        errorIndex = 0
    }

    override fun onError(code: Int?, message: String?, errorData: ErrorData?) {
        if (!isEnabled) {
            return
        }
        val httpRequests = httpRequestTracking?.httpRequests?.toMutableList()
        val errorIndex = errorIndex
        this.errorIndex++
        val platform = Util.getPlatform(Util.isTVDevice(context))
        val timestamp = Util.getTimestamp()

        val errorDetails = ErrorDetail(platform,
//            insightsConfig.key,
            Util.getDomain(context), impressionIdProvider.impressionId, errorIndex, timestamp, code, message, errorData ?: ErrorData(), httpRequests)
        backend.send(errorDetails)
    }
}
