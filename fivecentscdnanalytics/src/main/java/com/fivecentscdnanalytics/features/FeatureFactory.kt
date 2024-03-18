package com.fivecentscdnanalytics.features

import com.fivecentscdnanalytics.license.FeatureConfigContainer


interface FeatureFactory {
    fun createFeatures(): Collection<Feature<FeatureConfigContainer, *>>
}
