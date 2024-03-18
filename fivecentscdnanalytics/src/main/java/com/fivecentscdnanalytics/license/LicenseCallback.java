package com.fivecentscdnanalytics.license;

public interface LicenseCallback {
    void configureFeatures(boolean authenticated, FeatureConfigContainer featureConfigs);

    void authenticationCompleted(boolean success);
}
//mohammadia housing society, road 6, jame mosjid opposite
