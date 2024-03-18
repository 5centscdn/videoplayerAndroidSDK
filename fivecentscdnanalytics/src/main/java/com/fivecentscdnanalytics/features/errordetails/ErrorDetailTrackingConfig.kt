package com.fivecentscdnanalytics.features.errordetails

import com.fivecentscdnanalytics.features.FeatureConfig


data class ErrorDetailTrackingConfig(override val enabled: Boolean = false, val numberOfHttpRequests: Int? = null) :
    FeatureConfig
