package com.fivecentscdnanalytics.calls.presenter;


import android.util.Log;
import android.widget.FrameLayout;

import com.fivecentscdnanalytics.config.FCCDNAnalyticsConfig;
import com.fivecentscdnanalytics.data.ViewerSession;
import com.fivecentscdnanalytics.calls.api.ApiClient;
import com.fivecentscdnanalytics.calls.api.ServiceApi;
import com.fivecentscdnanalytics.calls.events.PlayerEvents;
import com.fivecentscdnanalytics.data.FCCDNRequest;
import com.fivecentscdnanalytics.data.ShowCVResponse;
import com.fivecentscdnanalytics.utils.FCCLog;
import com.fivecentscdnanalytics.utils.TextOverlay;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.CompletableSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PlayerInstancePresenter extends BaseEventPresenter<PlayerEvents>{
    private static final String TAG = "PlayerInstancePresenter";
    private ServiceApi serviceApi;

    public PlayerInstancePresenter() {
        serviceApi = ApiClient.getClient().create(ServiceApi.class);
    }

    public void playerInit(PlayerEvents listener, FCCDNRequest fccdnRequest, ExoPlayer exoPlayer){
        fccdnRequest.setPlayerInitStartTime(System.currentTimeMillis());
        FCCLog.d(TAG, "playerInit: " + System.currentTimeMillis() );
        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put(ViewerSession.HASH_ID, fccdnRequest.getHashID());
            jsonParams.put(ViewerSession.URL, fccdnRequest.getUrl());

            RequestBody requestBody = RequestBody.create(jsonParams.toString(), MediaType.parse("application/json"));
            serviceApi.init(requestBody).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableSubscriber() {
                        @Override
                        public void onCompleted() {
                            // Request completed successfully
                            FCCLog.d(TAG, "Player Init Request completed successfully");
                            FCCLog.d(TAG, "isPlayerError() " + fccdnRequest.isPlayerError());
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "Player Init Request completed successfully");
                            }
                            exoPlayer.play();
                            if (!fccdnRequest.isPlayerError()){
                                onPlay(fccdnRequest, listener);
                            }

                        }
                        @Override
                        public void onError(Throwable e) {
                            // Error occurred during the request
                            if (e instanceof HttpException) {
                                int responseCode = ((HttpException) e).response().code();
                                FCCLog.e(TAG, "Request failed with HTTP response code: " + responseCode);
                                if (fccdnRequest.isLog()){
                                    fccdnRequest.getLogTV().append("\n" + "Request failed with HTTP response code: " + responseCode);
                                }
                            } else {
                                FCCLog.e(TAG, "Request failed with an error: " + e.getMessage());
                            }
                            exoPlayer.stop();
                            exoPlayer.release();
                        }

                        @Override
                        public void onSubscribe(Subscription d) {
                            FCCLog.d(TAG, "Player Init Request is sent!");
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "Player Init Request is sent!");
                            }
                        }
                    });
            listener.onPlayerInitSuccess();
        }catch (Exception ex){
        }
    }

    public void onPlay(FCCDNRequest fccdnRequest, PlayerEvents listener){
        fccdnRequest.setPlayerInitEndTime(System.currentTimeMillis());
        FCCLog.d(TAG, "On Play " + System.currentTimeMillis());
        try{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(ViewerSession.HASH_ID, fccdnRequest.getHashID());
            jsonObject.addProperty(ViewerSession.TITLE, fccdnRequest.getTitle());
            jsonObject.add(ViewerSession.TAGS, fccdnRequest.getTags());
            jsonObject.addProperty(ViewerSession.URL,fccdnRequest.getUrl());
            jsonObject.addProperty(ViewerSession.VIEWER_ID, fccdnRequest.getViewerID());
            jsonObject.addProperty(ViewerSession.VIEW_ID, fccdnRequest.getViewID());
            jsonObject.addProperty(ViewerSession.USER_AGENT, fccdnRequest.getUserAgent());
            jsonObject.addProperty(ViewerSession.TYPE, "page_load");
            jsonObject.addProperty(ViewerSession.REFERRER, fccdnRequest.getReferrer());
            jsonObject.addProperty(ViewerSession.VALUE1, fccdnRequest.getValueOne());
            FCCLog.d(TAG, jsonObject.toString());
            RequestBody requestBody = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
            serviceApi.onPlay(requestBody).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableSubscriber() {
                        @Override
                        public void onCompleted() {
                            // Request completed successfully
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "On Play Request completed successfully");
                            }
                            FCCLog.d(TAG, "On Play Request completed successfully");
                            postPlay(fccdnRequest, listener);

                        }
                        @Override
                        public void onError(Throwable e) {
                            // Error occurred during the request
                            if (e instanceof HttpException) {
                                int responseCode = ((HttpException) e).response().code();
                                FCCLog.e(TAG, "On Play Request failed with HTTP response code: " + responseCode);
                                if (fccdnRequest.isLog()){
                                    fccdnRequest.getLogTV().append("\n" + "On Play Request failed with HTTP response code: " + responseCode);
                                }
                            } else {
                                FCCLog.e(TAG, "On Play Request failed with an error: " + e.getMessage());
                            }

                        }

                        @Override
                        public void onSubscribe(Subscription d) {
                            FCCLog.d(TAG, "On Play Request is sent!");
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "On Play Request is sent!");
                            }
                        }
                    });
            listener.onPlayerInitSuccess();
        }catch (Exception ex){
            FCCLog.e(TAG, ex.getMessage());
        }
        try{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(ViewerSession.HASH_ID, fccdnRequest.getHashID());
            jsonObject.addProperty(ViewerSession.TITLE, fccdnRequest.getTitle());
            jsonObject.add(ViewerSession.TAGS, fccdnRequest.getTags());
            jsonObject.addProperty(ViewerSession.URL,fccdnRequest.getUrl());
            jsonObject.addProperty(ViewerSession.VIEWER_ID, fccdnRequest.getViewerID());
            jsonObject.addProperty(ViewerSession.VIEW_ID, fccdnRequest.getViewID());
            jsonObject.addProperty(ViewerSession.USER_AGENT, fccdnRequest.getUserAgent());
            jsonObject.addProperty(ViewerSession.TYPE, "impression");
            jsonObject.addProperty(ViewerSession.REFERRER, fccdnRequest.getReferrer());
            FCCLog.d(TAG, jsonObject.toString());
            RequestBody requestBody = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
            serviceApi.postImpression(requestBody).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableSubscriber() {
                        @Override
                        public void onCompleted() {
                            // Request completed successfully
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "Post Impression Request completed successfully");
                            }
                            FCCLog.d(TAG, "Post Impression Request completed successfully");

                        }
                        @Override
                        public void onError(Throwable e) {
                            // Error occurred during the request
                            if (e instanceof HttpException) {
                                int responseCode = ((HttpException) e).response().code();
                                FCCLog.e(TAG, "Post Impression Request failed with HTTP response code: " + responseCode);
                                if (fccdnRequest.isLog()){
                                    fccdnRequest.getLogTV().append("\n" + "Post Impression Request failed with HTTP response code: " + responseCode);
                                }
                            } else {
                                FCCLog.e(TAG, "Post Impression Request failed with an error: " + e.getMessage());
                            }

                        }

                        @Override
                        public void onSubscribe(Subscription d) {
                            FCCLog.d(TAG, "Post Impression Request is Sent!");
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "Post Impression Request is Sent!");
                            }
                        }
                    });
            listener.onPlayerInitSuccess();
        }catch (Exception ex){
            FCCLog.e(TAG, ex.getMessage());
        }
    }
    public void postPlay(FCCDNRequest fccdnRequest, PlayerEvents listener){
        try{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(ViewerSession.HASH_ID, fccdnRequest.getHashID());
            jsonObject.addProperty(ViewerSession.TITLE, fccdnRequest.getTitle());
            jsonObject.add(ViewerSession.TAGS, fccdnRequest.getTags());
            jsonObject.addProperty(ViewerSession.URL,fccdnRequest.getUrl());
            jsonObject.addProperty(ViewerSession.VIEWER_ID, fccdnRequest.getViewerID());
            jsonObject.addProperty(ViewerSession.VIEW_ID, fccdnRequest.getViewID());
            jsonObject.addProperty(ViewerSession.USER_AGENT, fccdnRequest.getUserAgent());
            jsonObject.addProperty(ViewerSession.TYPE, "player_load");
            jsonObject.addProperty(ViewerSession.REFERRER, fccdnRequest.getReferrer());
            jsonObject.addProperty(ViewerSession.VALUE1, fccdnRequest.getInitTime());
            Log.e("here", jsonObject.toString());
            RequestBody requestBody = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
            serviceApi.postInitPlayerTime(requestBody).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableSubscriber() {
                        @Override
                        public void onCompleted() {
                            // Request completed successfully
                            FCCLog.d(TAG, "Player Load Request completed successfully");
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "Player Load Request completed successfully");
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            // Error occurred during the request
                            if (e instanceof HttpException) {
                                int responseCode = ((HttpException) e).response().code();
                                FCCLog.e(TAG, "Player Load Request failed with HTTP response code: " + responseCode);
                                if (fccdnRequest.isLog()){
                                    fccdnRequest.getLogTV().append("\n" + "Player Load Request failed with HTTP response code: " + responseCode);
                                }
                            } else {
                                FCCLog.e(TAG, "Player Load Request failed with an error: " + e.getMessage());
                            }

                        }

                        @Override
                        public void onSubscribe(Subscription d) {
                            FCCLog.d(TAG, "Player Load Request is sent!");
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "Player Load Request is sent!");
                            }
                        }
                    });
            listener.onPlayerInitSuccess();
        }catch (Exception ex){
            FCCLog.e(TAG, ex.getMessage());
        }
        try{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(ViewerSession.HASH_ID, fccdnRequest.getHashID());
            jsonObject.addProperty(ViewerSession.TITLE, fccdnRequest.getTitle());
            jsonObject.add(ViewerSession.TAGS, fccdnRequest.getTags());
            jsonObject.addProperty(ViewerSession.URL,fccdnRequest.getUrl());
            jsonObject.addProperty(ViewerSession.VIEWER_ID, fccdnRequest.getViewerID());
            jsonObject.addProperty(ViewerSession.VIEW_ID, fccdnRequest.getViewID());
            jsonObject.addProperty(ViewerSession.USER_AGENT, fccdnRequest.getUserAgent());
            jsonObject.addProperty(ViewerSession.TYPE, "play");
            jsonObject.addProperty(ViewerSession.REFERRER, fccdnRequest.getReferrer());
            FCCLog.d(TAG, jsonObject.toString());
            RequestBody requestBody = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
            serviceApi.postPlay(requestBody).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableSubscriber() {
                        @Override
                        public void onCompleted() {
                            // Request completed successfully
                            FCCLog.d(TAG, "Post Play Request completed successfully");
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "Post Play Request completed successfully");
                            }
                            postEngagement(fccdnRequest, listener);
                        }
                        @Override
                        public void onError(Throwable e) {
                            // Error occurred during the request
                            if (e instanceof HttpException) {
                                int responseCode = ((HttpException) e).response().code();
                                FCCLog.e(TAG, "Post Play Request failed with HTTP response code: " + responseCode);
                                if (fccdnRequest.isLog()){
                                    fccdnRequest.getLogTV().append("\n" + "Post Play Request failed with HTTP response code: " + responseCode);
                                }
                            } else {
                                FCCLog.e(TAG, "Post Play Request failed with an error: " + e.getMessage());
                            }

                        }

                        @Override
                        public void onSubscribe(Subscription d) {
                            FCCLog.d(TAG, "Post Play Request is sent!");
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "Post Play Request is sent!");
                            }
                        }
                    });
            listener.onPlayerInitSuccess();
        }catch (Exception ex){
            FCCLog.e(TAG, ex.getMessage());
        }
    }

    public void postEngagement(FCCDNRequest fccdnRequest, PlayerEvents listener){
        try{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(ViewerSession.HASH_ID, fccdnRequest.getHashID());
            jsonObject.addProperty(ViewerSession.TITLE, fccdnRequest.getTitle());
            jsonObject.add(ViewerSession.TAGS, fccdnRequest.getTags());
            jsonObject.addProperty(ViewerSession.URL,fccdnRequest.getUrl());
            jsonObject.addProperty(ViewerSession.VIEWER_ID, fccdnRequest.getViewerID());
            jsonObject.addProperty(ViewerSession.VIEW_ID, fccdnRequest.getViewID());
            jsonObject.addProperty(ViewerSession.USER_AGENT, fccdnRequest.getUserAgent());
            jsonObject.addProperty(ViewerSession.TYPE, "engagement");
            jsonObject.addProperty(ViewerSession.REFERRER, fccdnRequest.getReferrer());
            jsonObject.addProperty(ViewerSession.VALUE1, fccdnRequest.getInitTime());
            FCCLog.d(TAG, jsonObject.toString());
            RequestBody requestBody = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
            serviceApi.postEngagement(requestBody).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableSubscriber() {
                        @Override
                        public void onCompleted() {
                            // Request completed successfully
                            FCCLog.d(TAG, "Post Engagement Request completed successfully");
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "Post Engagement Request completed successfully");
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            // Error occurred during the request
                            if (e instanceof HttpException) {
                                int responseCode = ((HttpException) e).response().code();
                                FCCLog.e(TAG, "Post Engagement Request failed with HTTP response code: " + responseCode);
                                if (fccdnRequest.isLog()){
                                    fccdnRequest.getLogTV().append("\n" + "Post Engagement Request completed successfully");
                                }
                            } else {
                                FCCLog.e(TAG, "Post Engagement Request failed with an error: " + e.getMessage());
                            }

                        }

                        @Override
                        public void onSubscribe(Subscription d) {
                            FCCLog.d(TAG, "Post Engagement Request is sent!");
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "Post Engagement Request completed successfully");
                            }
                        }
                    });
            listener.onPlayerInitSuccess();
        }catch (Exception ex){
            FCCLog.e(TAG, ex.getMessage());
        }

    }
    public void pollEvery10Sec(FCCDNRequest fccdnRequest, PlayerEvents listener, long bytes){
        try{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(ViewerSession.HASH_ID, fccdnRequest.getHashID());
            jsonObject.addProperty(ViewerSession.TITLE, fccdnRequest.getTitle());
            jsonObject.add(ViewerSession.TAGS, fccdnRequest.getTags());
            jsonObject.addProperty(ViewerSession.URL,fccdnRequest.getUrl());
            jsonObject.addProperty(ViewerSession.VIEWER_ID, fccdnRequest.getViewerID());
            jsonObject.addProperty(ViewerSession.VIEW_ID, fccdnRequest.getViewID());
            jsonObject.addProperty(ViewerSession.USER_AGENT, fccdnRequest.getUserAgent());
            jsonObject.addProperty(ViewerSession.TYPE, "engagement");
            jsonObject.addProperty(ViewerSession.REFERRER, fccdnRequest.getReferrer());
            jsonObject.addProperty(ViewerSession.VALUE1, 10);
            jsonObject.addProperty(ViewerSession.VALUE2, bytes);
            FCCLog.d(TAG, jsonObject.toString());
            RequestBody requestBody = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
            serviceApi.pollEvery10Sec(requestBody).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableSubscriber() {
                        @Override
                        public void onCompleted() {
                            // Request completed successfully
                            FCCLog.d(TAG, "Poll Every 10 sec Request completed successfully");
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "Poll Every 10 sec Request completed successfully");
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            // Error occurred during the request
                            if (e instanceof HttpException) {
                                int responseCode = ((HttpException) e).response().code();
                                FCCLog.e(TAG, "Poll Every 10 sec Request failed with HTTP response code: " + responseCode);
                                if (fccdnRequest.isLog()){
                                    fccdnRequest.getLogTV().append("\n" + "Poll Every 10 sec Request failed with HTTP response code: " + responseCode);
                                }
                            } else {
                                FCCLog.e(TAG, "Poll Every 10 sec Request failed with an error: " + e.getMessage());
                            }

                        }
                        @Override
                        public void onSubscribe(Subscription d) {
                            FCCLog.d(TAG, "Poll Every 10 sec Request is sent!");
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "Poll Every 10 sec Request is sent!");
                            }
                        }
                    });
            listener.onPlayerInitSuccess();
        }catch (Exception ex){
            FCCLog.e(TAG, ex.getMessage());
        }

    }

    public void postCompleted(FCCDNRequest fccdnRequest, PlayerEvents listener){
        try{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(ViewerSession.HASH_ID, fccdnRequest.getHashID());
            jsonObject.addProperty(ViewerSession.TITLE, fccdnRequest.getTitle());
            jsonObject.add(ViewerSession.TAGS, fccdnRequest.getTags());
            jsonObject.addProperty(ViewerSession.URL,fccdnRequest.getUrl());
            jsonObject.addProperty(ViewerSession.VIEWER_ID, fccdnRequest.getViewerID());
            jsonObject.addProperty(ViewerSession.VIEW_ID, fccdnRequest.getViewID());
            jsonObject.addProperty(ViewerSession.USER_AGENT, fccdnRequest.getUserAgent());
            jsonObject.addProperty(ViewerSession.TYPE, "complete");
            jsonObject.addProperty(ViewerSession.REFERRER, fccdnRequest.getReferrer());
            FCCLog.d(TAG, jsonObject.toString());
            RequestBody requestBody = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
            serviceApi.postCompleted(requestBody).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableSubscriber() {
                        @Override
                        public void onCompleted() {
                            // Request completed successfully
                            FCCLog.d(TAG, "Playback Complete Request completed successfully");
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "Playback Complete Request completed successfully");
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            // Error occurred during the request
                            if (e instanceof HttpException) {
                                int responseCode = ((HttpException) e).response().code();
                                FCCLog.e(TAG, "Playback Complete Request failed with HTTP response code: " + responseCode);
                                if (fccdnRequest.isLog()){
                                    fccdnRequest.getLogTV().append("\n" + "Playback Complete Request failed with HTTP response code: " + responseCode);
                                }
                            } else {
                                FCCLog.e(TAG, "Playback Complete Request failed with an error: " + e.getMessage());
                            }

                        }
                        @Override
                        public void onSubscribe(Subscription d) {
                            FCCLog.d(TAG, "Playback Complete Request is sent!");
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "Playback Complete Request is sent!");
                            }
                        }
                    });
            listener.onPlayerInitSuccess();
        }catch (Exception ex){
            FCCLog.e(TAG, ex.getMessage());
        }

    }

    boolean isVisible;
    TextOverlay textOverlay;

    public void postIfShowCV(FCCDNRequest fccdnRequest, FCCDNAnalyticsConfig fccdnAnalyticsConfig){
        final int[] count = {0};
        try{
            Call<ShowCVResponse> call = serviceApi.postIfShowCVTrue(fccdnRequest.getHashID());
            call.enqueue(new Callback<ShowCVResponse>() {
                @Override
                public void onResponse(Call<ShowCVResponse> call, Response<ShowCVResponse> response) {
                    if (response.isSuccessful()) {
                        ShowCVResponse apiResponse = response.body();
                        // Handle the successful response
                        if (apiResponse != null) {
                            FCCLog.d(TAG, "postIfShowCV count " + String.valueOf(apiResponse.getCount()));
                            count[0] = apiResponse.getCount();
                            if(!isVisible){
                                textOverlay = new TextOverlay(fccdnAnalyticsConfig.getContext());
                                ((FrameLayout) fccdnAnalyticsConfig.getPlayerView()).addView(textOverlay);
                                textOverlay.setText("");
                                textOverlay.setText(String.valueOf(count[0]));
                                isVisible = true;
                            }else {
                                textOverlay.setText("");
                                textOverlay.setText(String.valueOf(count[0]));
                            }
                            if (fccdnRequest.isLog()){
                                fccdnRequest.getLogTV().append("\n" + "Showing CV Request Successful! Count: " + count[0]);
                            }

                        }else {
                            count[0] = 0;
                        }
                    } else {
                        // Handle the error response
                        FCCLog.e(TAG, "Error response: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ShowCVResponse> call, Throwable t) {
                    FCCLog.e(TAG, "onFailure: " + t.getMessage());
                    if (fccdnRequest.isLog()){
                        fccdnRequest.getLogTV().append("\n" + "Showing CV Request has failed!");
                    }
                }


            });
        }catch (Exception ex){
            FCCLog.e(TAG, "Exception occurred: " + ex.getMessage());
        }

    }

}
