package com.fivecentscdnanalytics.data;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.fivecentscdnanalytics.config.CollectorConfig;
import com.fivecentscdnanalytics.retryBackend.OnFailureCallback;
import com.fivecentscdnanalytics.utils.ClientFactory;
import com.fivecentscdnanalytics.utils.DataSerializer;
import com.fivecentscdnanalytics.utils.FCCLog;
import com.fivecentscdnanalytics.utils.HttpClient;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HttpBackend implements Backend, CallbackBackend {
    private static final String TAG = "5CentsCDNBackend";
    private final HttpClient httpClient;
    private final String analyticsBackendUrl;
    private final String adsAnalyticsBackendUrl;

    public HttpBackend(CollectorConfig config, Context context) {
        analyticsBackendUrl =
                Uri.parse(config.getBackendUrl())
                        .buildUpon()
                        .build()
                        .toString();
        adsAnalyticsBackendUrl =
                Uri.parse(config.getBackendUrl())
                        .buildUpon()
                        .build()
                        .toString();
       FCCLog.d(
                TAG,
                String.format("Initialized Analytics HTTP Backend with %s", analyticsBackendUrl));
        this.httpClient = new HttpClient(context, new ClientFactory().createClient(config));
    }

    @Override
    public void send(@NonNull EventData eventData) {
        this.send(eventData, null);
    }

    @Override
    public void sendAd(@NonNull AdEventData eventData) {
        this.sendAd(eventData, null);
    }

    @Override
    public void send(EventData eventData, OnFailureCallback callback) {
       FCCLog.d(
                TAG,
                String.format(
                        "Sending sample: %s (state: %s, videoId: %s, startupTime: %d, videoStartupTime: %d, buffered: %d, audioLanguage: %s)",
                        eventData.getImpressionId(),
                        eventData.getVideoId(),
                        eventData.getState(),
                        eventData.getStartupTime(),
                        eventData.getVideoStartupTime(),
                        eventData.getBuffered(),
                        eventData.getAudioLanguage()));
        try{
            this.httpClient.post(
                    analyticsBackendUrl,
                    DataSerializer.serialize(eventData),
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            if (callback == null) {
                                return;
                            }
                            callback.onFailure(
                                    e,
                                    () -> {
                                        call.cancel();
                                        return null;
                                    });
                        }

                        @Override
                        public void onResponse(Call call, Response response) {}
                    });

        }catch(Exception ex){

        }

    }

    @Override
    public void sendAd(AdEventData eventData, OnFailureCallback callback) {
       FCCLog.d(
                TAG,
                String.format(
                        "Sending ad sample: %s (videoImpressionId: %s, adImpressionId: %s)",
                        eventData.getAdImpressionId(),
                        eventData.getVideoImpressionId(),
                        eventData.getAdImpressionId()));

        try{
            this.httpClient.post(
                    adsAnalyticsBackendUrl,
                    DataSerializer.serialize(eventData),
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            if (callback == null) {
                                return;
                            }
                            callback.onFailure(
                                    e,
                                    () -> {
                                        call.cancel();
                                        return null;
                                    });
                        }

                        @Override
                        public void onResponse(Call call, Response response) {}
                    });

        }catch (Exception ex){

        }

    }
}
