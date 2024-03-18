package com.fivecentscdnanalytics.license

interface AuthenticationCallback {
    fun authenticationCompleted(success: Boolean, featureConfigs: FeatureConfigContainer?)
}
