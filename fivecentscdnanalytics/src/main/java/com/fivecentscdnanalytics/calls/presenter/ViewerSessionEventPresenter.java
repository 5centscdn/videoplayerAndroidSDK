package com.fivecentscdnanalytics.calls.presenter;



import com.fivecentscdnanalytics.data.ViewerSession;
import com.fivecentscdnanalytics.data.ViewerSessionEvent;
import com.fivecentscdnanalytics.calls.api.ApiClient;
import com.fivecentscdnanalytics.calls.api.ServiceApi;
import com.fivecentscdnanalytics.calls.events.SessionEvents;
import com.fivecentscdnanalytics.data.FiveCentsData;
import com.fivecentscdnanalytics.utils.Util;

import java.util.HashMap;
import java.util.Map;

public class ViewerSessionEventPresenter extends BaseEventPresenter<SessionEvents> {
    private final ServiceApi serviceApi;

    public ViewerSessionEventPresenter() {
        serviceApi = ApiClient.getClient().create(ServiceApi.class);
    }
    
//    public void sendInit(FiveCentsData fiveCentsData, SessionEvents listener){
//
//
//
//
//        try{
////            serviceApi.init(ApiConfig.HEADER_ACCEPT_DATA, fiveCentsData.getHashID(), fiveCentsData.getURL()).subscribeOn(Schedulers.newThread()).subscribe();
//            serviceApi.init(ApiConfig.HEADER_ACCEPT_DATA, fiveCentsData.getHashID(), fiveCentsData.getURL()).enqueue(new Callback<Response>() {
//                @Override
//                public void onResponse(Call<Response> call, Response<Response> response) {
//                    // Handle the successful response
//                    Log.e("here", "onResponse: " + response.code() );
//                }
//
//                @Override
//                public void onFailure(Call<Response> call, Throwable t) {
//                    // Handle the error response and show an error message
//                    Log.e("here", "onResponse: " + call.toString() );
//                }
//            });
//
//        }catch (Exception ex){
//            FCCLog.e("","",ex);
//        }
//    }
    public void postAppPageLoad(FiveCentsData fiveCentsData){

    }


//    public void logSessionEvent(FiveCentsData fiveCentsData, SessionEvents listener){
//
//        try{
////            serviceApi.init(ApiConfig.HEADER_ACCEPT_DATA, fiveCentsData.getHashID(), fiveCentsData.getURL()).subscribeOn(Schedulers.newThread()).subscribe();
//            serviceApi.init(ApiConfig.HEADER_ACCEPT_DATA, fiveCentsData.getHashID(), fiveCentsData.getURL()).enqueue(new Callback<Response>() {
//                @Override
//                public void onResponse(Call<Response> call, Response<Response> response) {
//                    // Handle the successful response
//                    Log.e("here", "onResponse: " + response.code() );
//                }
//
//                @Override
//                public void onFailure(Call<Response> call, Throwable t) {
//                    // Handle the error response and show an error message
//                    Log.e("here", "onResponse: " + call.toString() );
//                }
//            });
//
//        }catch (Exception ex){
//            FCCLog.e("","",ex);
//        }
//    }

//    public void LogSessionEvent(ViewerSession viewerSession, SessionEvents listener){
//
//        try{
//            serviceApi.viewer_session(
//                    /*"Bearer "+ ApiConfig.BEARER_TOKEN,*/
//                    "session",
//                    viewerSession.getSessionId(),
//                    viewerSession.getPropertyId(),
//                    viewerSession.getUserId(),
//                    viewerSession.getCustomUserId(),
//                    viewerSession.getViewerClientVersion(),
//                    viewerSession.getMetaAsn(),
//                    viewerSession.getMetaIpAddress(),
//                    viewerSession.getMetaBrowser(),
//                    viewerSession.getMetaBrowserVersion(),
//                    viewerSession.getMetaUserAgent(),
//                    viewerSession.getMetaDeviceDisplayWidth(),
//                    viewerSession.getMetaDeviceDisplayHeight(),
//                    viewerSession.getMetaDeviceDisplayDpr(),
//                    viewerSession.getMetaDeviceIsTouchScreen(),
//                    viewerSession.getMetaOperatingSystem(),
//                    viewerSession.getMetaOperatingSystemVersion(),
//                    viewerSession.getMetaDeviceCategory(),
//                    viewerSession.getMetaConnectionType(),
//                    viewerSession.getMetaConnectionSpeed(),
//                    viewerSession.getMetaDeviceManufacturer(),
//                    viewerSession.getMetaDeviceName(),
//                    viewerSession.getMetaCity(),
//                    viewerSession.getMetaCountryCode(),
//                    viewerSession.getMetaCountry(),
//                    viewerSession.getMetaContinentCode(),
//                    viewerSession.getMetaRegion(),
//                    viewerSession.getMetaLatitude(),
//                    viewerSession.getMetaLongitude(),
//                    viewerSession.getMetaDeviceArchitecture(),
//                    viewerSession.getCustomData1(),
//                    viewerSession.getCustomData2(),
//                    viewerSession.getCustomData3(),
//                    viewerSession.getCustomData4(),
//                    viewerSession.getCustomData5(),
//                    viewerSession.getCustomData6(),
//                    viewerSession.getCustomData7(),
//                    viewerSession.getCustomData8(),
//                    viewerSession.getCustomData9(),
//                    viewerSession.getCustomData10(),
//                    viewerSession.getErrorCode(),
//                    viewerSession.getError(),
//                    viewerSession.getErrorText(),
//                    viewerSession.getUserName(),
//                    viewerSession.getUserEmail(),
//                    viewerSession.getUserPhone(),
//                    viewerSession.getUserProfileImage(),
//                    viewerSession.getUserAddressLine1(),
//                    viewerSession.getUserAddressLine2(),
//                    viewerSession.getUserCity(),
//                    viewerSession.getUserState(),
//                    viewerSession.getUserCountry(),
//                    viewerSession.getUserZipcode(),
//                    Util.getTimeStampInMs()
//            ).subscribeOn(Schedulers.newThread()).subscribe();
//
//            if(listener != null) {
//                listener.onSessionEventSuccess();
//            }
//        }catch (Exception ex){
//
//        }
//    }
//
//    public void sessionEvent(ViewerSessionEvent eventConfig, SessionEvents listener){
//
//        try{
//            String eventId = Util.getRandomCharacterString();
//            eventConfig.setEventId(eventId);
//
////            Map<String,String> requestParameterMap = createRequestParams(eventConfig,"session_event");
////            URL requestUrl = RequestUrlCreator.buildURL(ApiConfig.BASE_URL,requestParameterMap);
//
//            if(ApiParameterBasicStability.isValidPropertyId(eventConfig.getPropertyId())
//                    && ApiParameterBasicStability.isValidIdsProvidedInApiCall(eventConfig)
//                    && ApiParameterBasicStability.rebufferIsCorrect(eventConfig)){
//
//                serviceApi.sessionEvent(
//                        "session_event", //event_family
//                        eventId, //event_id
//                        eventConfig.getSessionId(), //session_id
//                        eventConfig.getPropertyId(), //property_id
//                        eventConfig.getUserId(), //user_id
//                        eventConfig.getPlayerInstanceId(), //player_instance_id
//                        eventConfig.getPlaybackId(), //playback_id
//                        eventConfig.getEvent(), //event
//                        eventConfig.getFrom(), //from
//                        eventConfig.getTo(),  //to
//                        eventConfig.getPlaybackTimeInstantMillis(),//playback_time_instant_millis
//                        eventConfig.getVideoDownscalePercentage(), //video_downscale_percentage
//                        eventConfig.getVideoUpscalePercentage(),  //video_upscale_percentage
//                        eventConfig.getBitrateMbps(), //bitrate_mbps
//                        eventConfig.getCustomData1(),
//                        eventConfig.getCustomData2(),
//                        eventConfig.getCustomData3(),
//                        eventConfig.getCustomData4(),
//                        eventConfig.getCustomData5(),
//                        eventConfig.getCustomData6(),
//                        eventConfig.getCustomData7(),
//                        eventConfig.getCustomData8(),
//                        eventConfig.getCustomData9(),
//                        eventConfig.getCustomData10(),
//                        eventConfig.getErrorCode(),
//                        eventConfig.getError(),
//                        eventConfig.getErrorText(),
//                        eventConfig.getLatitude(),
//                        eventConfig.getLongitude(),
//                        eventConfig.getVideoCover(),
//                        eventConfig.getVideoCover(),
//                        eventConfig.getPlayerRemotePlayed(),
//                        eventConfig.getVideoTotalDurationMillis(),
//                        eventConfig.getVideoWidthPixels(),
//                        eventConfig.getVideoHeightPixels(),
//                        eventConfig.getVideoSourceUrl(),
//                        eventConfig.getVideoSourceHostname(),
//                        eventConfig.getVideoSourceType(),
//                        eventConfig.getCustomContentType(),
//                        eventConfig.getCustomVideoDurationMillis(),
//                        eventConfig.getCustomEncodingVariant(),
//                        eventConfig.getCustomVideoLanguage(),
//                        eventConfig.getCustomVideoId(),
//                        eventConfig.getCustomVideoSeries(),
//                        eventConfig.getCustomVideoProducer(),
//                        eventConfig.getCustomVideoTitle(),
//                        eventConfig.getCustomVideoVariantName(),
//                        eventConfig.getCustomVideoVariant(),
//                        eventConfig.getMuted(),
//                        eventConfig.getPreviousEvent(),
//                        eventConfig.getMillisFromPreviousEvent(),
//                        eventConfig.getCasting(),
//                        eventConfig.getFullscreen(),
//                        eventConfig.getOrientation(),
//                        eventConfig.getOrientationFrom(),
//                        eventConfig.getQuality(),
//                        eventConfig.getQualityFrom(),
//                        Util.getTimeStampInMs(eventConfig.getTimestamp())
//                ).subscribeOn(Schedulers.newThread()).subscribe();
//
//                listener.onSessionEventSuccess(eventConfig.getEvent());
//
//            }
//        }catch (Exception ex){
//            FCCLog.e("","",ex);
//        }
//
//    }

    private Map<String, String> createRequestParams(final ViewerSessionEvent eventConfig,final String eventFamily) {
        Map<String,String> map = new HashMap<>();
        map.put(ViewerSession.KEY_EVENT_FAMILY,eventFamily);
        map.put(ViewerSessionEvent.KEY_EVENT_ID,eventConfig.getEventId());
        map.put(ViewerSessionEvent.KEY_SESSION_ID,eventConfig.getSessionId());
        map.put(ViewerSessionEvent.KEY_PROPERTY_ID,eventConfig.getPropertyId());
        map.put(ViewerSessionEvent.KEY_USER_ID,eventConfig.getUserId());
        map.put(ViewerSessionEvent.KEY_PLAYER_INSTANCE_ID,eventConfig.getPlayerInstanceId());
        map.put(ViewerSessionEvent.KEY_PLAYBACK_ID,eventConfig.getPlaybackId());
        map.put(ViewerSessionEvent.KEY_EVENT,eventConfig.getEvent());
        map.put(ViewerSessionEvent.KEY_FROM,""+eventConfig.getFrom());
        map.put(ViewerSessionEvent.KEY_TO,""+eventConfig.getTo());
        map.put(ViewerSessionEvent.KEY_PLAYBACK_TIME_INSTANT_MILLIS,""+eventConfig.getPlaybackTimeInstantMillis());
        map.put(ViewerSessionEvent.KEY_VIDEO_DOWNSCALE_PERCENTAGE,""+eventConfig.getVideoDownscalePercentage());
        map.put(ViewerSessionEvent.KEY_VIDEO_UPSCALE_PERCENTAGE,""+eventConfig.getVideoUpscalePercentage());
        map.put(ViewerSessionEvent.KEY_BITRATE_MBPS,""+eventConfig.getBitrateMbps());
        map.put(ViewerSessionEvent.KEY_CUSTOM_DATA_1,eventConfig.getCustomData1());
        map.put(ViewerSessionEvent.KEY_CUSTOM_DATA_2,eventConfig.getCustomData2());
        map.put(ViewerSessionEvent.KEY_CUSTOM_DATA_3,eventConfig.getCustomData3());
        map.put(ViewerSessionEvent.KEY_CUSTOM_DATA_4,eventConfig.getCustomData4());
        map.put(ViewerSessionEvent.KEY_CUSTOM_DATA_5,eventConfig.getCustomData5());
        map.put(ViewerSessionEvent.KEY_CUSTOM_DATA_6,eventConfig.getCustomData6());
        map.put(ViewerSessionEvent.KEY_CUSTOM_DATA_7,eventConfig.getCustomData7());
        map.put(ViewerSessionEvent.KEY_CUSTOM_DATA_8,eventConfig.getCustomData8());
        map.put(ViewerSessionEvent.KEY_CUSTOM_DATA_9,eventConfig.getCustomData9());
        map.put(ViewerSessionEvent.KEY_CUSTOM_DATA_10,eventConfig.getCustomData10());
        map.put(ViewerSessionEvent.KEY_ERROR_CODE,eventConfig.getErrorCode());
        map.put(ViewerSessionEvent.KEY_ERROR,eventConfig.getError());
        map.put(ViewerSessionEvent.KEY_ERROR_TEXT,eventConfig.getErrorText());
        map.put(ViewerSessionEvent.KEY_LATITUDE,eventConfig.getLatitude());
        map.put(ViewerSessionEvent.KEY_LONGITUDE,eventConfig.getLongitude());
        map.put(ViewerSessionEvent.KEY_VIDEO_COVER,eventConfig.getVideoCover());
        map.put(ViewerSessionEvent.KEY_VIDEO_COVER_REFERENCE,eventConfig.getVideoCoverReference());
        map.put(ViewerSessionEvent.KEY_PLAYER_REMOTE_PLAYED,""+eventConfig.getPlayerRemotePlayed());
        map.put(ViewerSessionEvent.KEY_VIDEO_TOTAL_DURATION_MILLIS,""+eventConfig.getVideoTotalDurationMillis());
        map.put(ViewerSessionEvent.KEY_VIDEO_WIDTH_PIXEL,""+eventConfig.getVideoWidthPixels());
        map.put(ViewerSessionEvent.KEY_VIDEO_HEIGHT_PIXEL,""+eventConfig.getVideoHeightPixels());
        map.put(ViewerSessionEvent.KEY_VIDEO_SOURCE_URL,eventConfig.getVideoSourceUrl());
        map.put(ViewerSessionEvent.KEY_VIDEO_SOURCE_HOSTNAME,eventConfig.getVideoSourceHostname());
        map.put(ViewerSessionEvent.KEY_VIDEO_SOURCE_TYPE,eventConfig.getVideoSourceType());
        map.put(ViewerSessionEvent.KEY_CUSTOM_CONTENT_TYPE,eventConfig.getCustomContentType());
        map.put(ViewerSessionEvent.KEY_CUSTOM_VIDEO_DURATION_MILLIS,""+eventConfig.getCustomVideoDurationMillis());
        map.put(ViewerSessionEvent.KEY_CUSTOM_ENCODING_VARIANT,eventConfig.getCustomEncodingVariant());
        map.put(ViewerSessionEvent.KEY_CUSTOM_VIDEO_LANGUAGE,eventConfig.getCustomVideoLanguage());
        map.put(ViewerSessionEvent.KEY_CUSTOM_VIDEO_ID,eventConfig.getCustomVideoId());
        map.put(ViewerSessionEvent.KEY_CUSTOM_VIDEO_SERIES,eventConfig.getCustomVideoSeries());
        map.put(ViewerSessionEvent.KEY_CUSTOM_VIDEO_PRODUCER,eventConfig.getCustomVideoProducer());
        map.put(ViewerSessionEvent.KEY_CUSTOM_VIDEO_TITLE,eventConfig.getCustomVideoTitle());
        map.put(ViewerSessionEvent.KEY_CUSTOM_VIDEO_VARIANT_NAME,eventConfig.getCustomVideoVariantName());
        map.put(ViewerSessionEvent.KEY_CUSTOM_VIDEO_VARIANT,eventConfig.getCustomVideoVariant());
        map.put(ViewerSessionEvent.KEY_MUTED,""+eventConfig.getMuted());
        map.put(ViewerSessionEvent.KEY_PREVIOUS_EVENT,eventConfig.getPreviousEvent());
        map.put(ViewerSessionEvent.KEY_MILLIS_FROM_PREVIOUS_EVENT,""+eventConfig.getMillisFromPreviousEvent());
        map.put(ViewerSessionEvent.KEY_CASTING,""+eventConfig.getCasting());
        map.put(ViewerSessionEvent.KEY_FULLSCREEN,""+eventConfig.getFullscreen());
        map.put(ViewerSessionEvent.KEY_ORIENTATION,eventConfig.getOrientation());
        map.put(ViewerSessionEvent.KEY_ORIENTATION_FROM,eventConfig.getOrientationFrom());
        map.put(ViewerSessionEvent.KEY_QUALITY,eventConfig.getQuality());
        map.put(ViewerSessionEvent.KEY_QUALITY_FROM,eventConfig.getQualityFrom());
        map.put(ViewerSessionEvent.KEY_TIMESTAMP,Util.getTimeStampInMs(eventConfig.getTimestamp()));

        return map;

    }

}
