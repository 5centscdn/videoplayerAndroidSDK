package com.fivecentscdnanalytics.utils

import com.fivecentscdnanalytics.config.CollectorConfig
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient

class ClientFactory {
    fun createClient(config: CollectorConfig): OkHttpClient {
        return if (config.tryResendDataOnFailedConnection) {
            OkHttpClient.Builder()
                    .retryOnConnectionFailure(false)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .build()
        } else {
            OkHttpClient()
        }
    }
}
