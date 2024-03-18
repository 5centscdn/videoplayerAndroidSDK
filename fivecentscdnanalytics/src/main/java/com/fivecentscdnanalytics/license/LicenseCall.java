package com.fivecentscdnanalytics.license;

import android.content.Context;
import android.net.Uri;
import com.fivecentscdnanalytics.config.FCCDNAnalyticsConfig;
import com.fivecentscdnanalytics.data.LicenseCallData;
import com.fivecentscdnanalytics.data.LicenseResponse;
import com.fivecentscdnanalytics.utils.ClientFactory;
import com.fivecentscdnanalytics.utils.DataSerializer;
import com.fivecentscdnanalytics.utils.FCCLog;
import com.fivecentscdnanalytics.utils.HttpClient;
import com.fivecentscdnanalytics.utils.Util;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LicenseCall {
    private static final String TAG = "LicenseCall";
    private final String backendUrl;
    private final FCCDNAnalyticsConfig config;
    private final Context context;
    private final HttpClient httpClient;

    public LicenseCall(FCCDNAnalyticsConfig config, Context context) {
        this.config = config;
        this.context = context;
        this.backendUrl =
                Uri.parse(config.getConfig().getBackendUrl())
                        .buildUpon()
                        .appendEncodedPath("licensing")
                        .build()
                        .toString();


       FCCLog.d(TAG, String.format("Initialized License Call with backendUrl: %s", backendUrl));
        this.httpClient =
                new HttpClient(context, new ClientFactory().createClient(config.getConfig()));
    }

    public void authenticate(final AuthenticationCallback callback) {
        final LicenseCallData data = new LicenseCallData();
//        data.setKey(this.config.getKey());
        data.setAnalyticsVersion(Util.getAnalyticsVersion());
        data.setDomain(Util.getDomain(context));
        String json = DataSerializer.serialize(data);
        httpClient.post(
                this.backendUrl,
                json,
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        FCCLog.e(TAG, "\n\nLicense call failed due to connectivity issues\n\n", e);
                        //callback.authenticationCompleted(false, null);
                        callback.authenticationCompleted(true, null);
                        FCCLog.e(TAG, "\n\nLicense call failed due to connectivity issues\n\n", e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response == null || response.body() == null) {
                            FCCLog.e(TAG, "\n\nLicense call was denied without providing a response body\n\n");
                            //callback.authenticationCompleted(false, null);
                            callback.authenticationCompleted(true, null);
                            FCCLog.e(TAG, "\n\nLicense call was denied without providing a response body\n\n");
                            return;
                        }

                        String licensingResponseBody = response.body().string();
                        LicenseResponse licenseResponse =
                                DataSerializer.deserialize(
                                        licensingResponseBody, LicenseResponse.class);
                        if (licenseResponse == null) {
                            FCCLog.e(TAG, "\n\nLicense call was denied without providing a response body\n\n");
                            //callback.authenticationCompleted(false, null);
                            callback.authenticationCompleted(true, null);
                            FCCLog.e(TAG, "\n\nLicense call was denied without providing a response body\n\n");
                            return;
                        }

                        if (licenseResponse.getStatus() == null) {
                            FCCLog.e(TAG, String.format("\n\nLicense response was denied without status\n\n"));
                            //callback.authenticationCompleted(false, null);
                            callback.authenticationCompleted(true, null);
                            FCCLog.e(TAG, String.format("\n\nLicense response was denied without status\n\n"));
                            return;
                        }

                        if (!licenseResponse.getStatus().equals("granted")) {
                            FCCLog.e(
                                    TAG,
                                    String.format(
                                            "\n\n*************License response was denied: %s**************\n\n",
                                            licenseResponse.getMessage()));

                            //callback.authenticationCompleted(false, null);
                            callback.authenticationCompleted(true, null);


                            FCCLog.e(
                                    TAG,
                                    String.format(
                                            "\n\n*************License response was denied: %s**************\n\n",
                                            licenseResponse.getMessage()));
                            return;
                        }
                        FCCLog.e(TAG, "License response was granted");
                        callback.authenticationCompleted(true, licenseResponse.getFeatures());
                    }
                });
    }
}
