package com.fivecentscdnanalytics.data;

import android.content.Context;
import android.os.Handler;
import com.fivecentscdnanalytics.config.FCCDNAnalyticsConfig;
import com.fivecentscdnanalytics.retryBackend.RetryBackend;

public class BackendFactory {

    public Backend createBackend(FCCDNAnalyticsConfig config, Context context) {
        HttpBackend httpBackend = new HttpBackend(config.getConfig(), context);
        if (!config.getConfig().getTryResendDataOnFailedConnection()) {
            return httpBackend;
        }

        return new RetryBackend(httpBackend, new Handler());
    }
}
