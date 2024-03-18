package com.fivecentscdnanalytics.adapters;

import com.fivecentscdnanalytics.config.SourceMetadata;
import com.fivecentscdnanalytics.data.manipulators.EventDataManipulatorPipeline;
import com.fivecentscdnanalytics.features.Feature;
import com.fivecentscdnanalytics.license.FeatureConfigContainer;

import java.util.Collection;

public interface PlayerAdapter {
    Collection<Feature<FeatureConfigContainer, ?>> init();

    void release();

    void resetSourceRelatedState();

    void registerEventDataManipulators(EventDataManipulatorPipeline pipeline);

    long getPosition();

    Long getDRMDownloadTime();

    void clearValues();

    SourceMetadata getCurrentSourceMetadata();

    default AdAdapter createAdAdapter() {
        return null;
    }
}
