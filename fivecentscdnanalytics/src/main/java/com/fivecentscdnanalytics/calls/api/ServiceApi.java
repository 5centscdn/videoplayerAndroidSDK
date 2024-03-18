package com.fivecentscdnanalytics.calls.api;

import com.fivecentscdnanalytics.data.ShowCVResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Completable;

public interface ServiceApi {

    //API 1
    @POST("/v")
    Completable init(@Body RequestBody requestBody);


    //API 2.1
    @POST("/t")
    Completable onPlay(@Body RequestBody requestBody);

    //API 2.2
    @POST("/t")
    Completable postInitPlayerTime(@Body RequestBody requestBody);


    //API 2.3
    @POST("/t")
    Completable postImpression(@Body RequestBody requestBody);

    //API 2.4
    @POST("/t")
    Completable postPlay(@Body RequestBody requestBody);

    //API 2.5
    @POST("/t")
    Completable postEngagement(@Body RequestBody requestBody);

    //API 3
    @POST("/t")
    Completable pollEvery10Sec(@Body RequestBody requestBody);

    //API 4
    @POST("/t")
    Completable postCompleted(@Body RequestBody requestBody);

    //API 5; if showcv == true
    @GET("/c")
    Call<ShowCVResponse> postIfShowCVTrue(@Query("hash_id") String hashId);

//
//    @GET("/")
//    Completable viewer_session(/*@Header("Authorization") String authorization,*/
//                                      @Query(ViewerSession.KEY_EVENT_FAMILY) String event_family,
//                                      @Query(ViewerSession.KEY_SESSION_ID) String sessionId,
//                                      @Query(ViewerSession.KEY_PROPERTY_ID) String property_id,
//                                      @Query(ViewerSession.KEY_USER_ID) String user_id,
//                                      @Query(ViewerSession.KEY_CUSTOM_USER_ID) String custom_user_id,
//                                      @Query(ViewerSession.KEY_VIEWER_CLIENT_VERSION) String viewer_client_version,
//                                      @Query(ViewerSession.KEY_META_ASN) String meta_asn,
//                                      @Query(ViewerSession.KEY_META_IP_ADDRESS) String meta_ip_address,
//                                      @Query(ViewerSession.KEY_META_BROWSER) String meta_browser,
//                                      @Query(ViewerSession.KEY_META_BROWSER_VERSION) String meta_browser_version,
//                                      @Query(ViewerSession.KEY_META_USER_AGENT) String meta_user_agent,
//                                      @Query(ViewerSession.KEY_META_DEVICE_DISPLAY_WIDTH) Integer meta_device_display_width,
//                                      @Query(ViewerSession.KEY_META_DEVICE_DISPLAY_HEIGHT) Integer meta_device_display_height,
//                                      @Query(ViewerSession.KEY_META_DEVICE_DISPLAY_DPR) Integer meta_device_display_dpr,
//                                      @Query(ViewerSession.KEY_META_DEVICE_IS_TOUCHSCREEN) Boolean meta_device_is_touchscreen,
//                                      @Query(ViewerSession.KEY_META_OPERATING_SYSTEM) String meta_operating_system,
//                                      @Query(ViewerSession.KEY_META_OPERATING_SYSTEM_VERSION) String meta_operating_system_version,
//                                      @Query(ViewerSession.KEY_META_DEVICE_CATEGORY) String meta_device_category,
//                                      @Query(ViewerSession.KEY_META_CONNECTION_TYPE) String meta_connection_type,
//                                      @Query(ViewerSession.KEY_META_CONNECTION_SPEED) String meta_connection_speed,
//                                      @Query(ViewerSession.KEY_META_DEVICE_MANUFACTURER) String meta_device_manufacturer,
//                                      @Query(ViewerSession.KEY_META_DEVICE_NAME) String meta_device_name,
//                                      @Query(ViewerSession.KEY_META_CITY) String meta_city,
//                                      @Query(ViewerSession.KEY_META_COUNTRY_CODE) String meta_country_code,
//                                      @Query(ViewerSession.KEY_META_COUNTRY) String meta_country,
//                                      @Query(ViewerSession.KEY_META_CONTINENT_CODE) String meta_continent_code,
//                                      @Query(ViewerSession.KEY_META_REGION) String meta_region,
//                                      @Query(ViewerSession.KEY_META_LATITUDE) String meta_latitude,
//                                      @Query(ViewerSession.KEY_META_LONGITUDE) String meta_longitude,
//                                      @Query(ViewerSession.KEY_META_DEVICE_ARCHITECTURE) String meta_device_architecture,
//                                      @Query(ViewerSession.KEY_CUSTOM_DATA_1) String custom_data_1,
//                                      @Query(ViewerSession.KEY_CUSTOM_DATA_2) String custom_data_2,
//                                      @Query(ViewerSession.KEY_CUSTOM_DATA_3) String custom_data_3,
//                                      @Query(ViewerSession.KEY_CUSTOM_DATA_4) String custom_data_4,
//                                      @Query(ViewerSession.KEY_CUSTOM_DATA_5) String custom_data_5,
//                                      @Query(ViewerSession.KEY_CUSTOM_DATA_6) String custom_data_6,
//                                      @Query(ViewerSession.KEY_CUSTOM_DATA_7) String custom_data_7,
//                                      @Query(ViewerSession.KEY_CUSTOM_DATA_8) String custom_data_8,
//                                      @Query(ViewerSession.KEY_CUSTOM_DATA_9) String custom_data_9,
//                                      @Query(ViewerSession.KEY_CUSTOM_DATA_10) String custom_data_10,
//                                      @Query(ViewerSession.KEY_ERROR_CODE) String error_code,
//                                      @Query(ViewerSession.KEY_ERROR) String error,
//                                      @Query(ViewerSession.KEY_ERROR_TEXT) String error_text,
//                                      @Query(ViewerSession.KEY_USER_NAME) String user_name,
//                                      @Query(ViewerSession.KEY_USER_EMAIL) String user_email,
//                                      @Query(ViewerSession.KEY_USER_PHONE) String user_phone,
//                                      @Query(ViewerSession.KEY_USER_PROFILE_IMAGE) String user_profile_image,
//                                      @Query(ViewerSession.KEY_USER_ADDRESS_LINE1) String user_address_line1,
//                                      @Query(ViewerSession.KEY_USER_ADDRESS_LINE2) String user_address_line2,
//                                      @Query(ViewerSession.KEY_USER_CITY) String user_city,
//                                      @Query(ViewerSession.KEY_USER_STATE) String user_state,
//                                      @Query(ViewerSession.KEY_USER_COUNTRY) String user_country,
//                                      @Query(ViewerSession.KEY_USER_ZIPCODE) String user_zipcode,
//                                      @Query(ViewerSession.KEY_TIMESTAMP) String Z);
//
//
//    @GET("/")
//    Completable sessionEvent(/*@Header("Authorization") String authorization,*/
//            @Query(ViewerSession.KEY_EVENT_FAMILY) String event_family,
//            @Query(ViewerSessionEvent.KEY_EVENT_ID) String event_id,
//            @Query(ViewerSessionEvent.KEY_SESSION_ID) String session_id,
//            @Query(ViewerSessionEvent.KEY_PROPERTY_ID) String property_id,
//            @Query(ViewerSessionEvent.KEY_USER_ID) String user_id,
//            @Query(ViewerSessionEvent.KEY_PLAYER_INSTANCE_ID) String player_instance_id,
//            @Query(ViewerSessionEvent.KEY_PLAYBACK_ID) String playback_id,
//            @Query(ViewerSessionEvent.KEY_EVENT) String event,
//            @Query(ViewerSessionEvent.KEY_FROM) Long from,
//            @Query(ViewerSessionEvent.KEY_TO) Long to,
//            @Query(ViewerSessionEvent.KEY_PLAYBACK_TIME_INSTANT_MILLIS) Long playback_time_instant_millis,
//            @Query(ViewerSessionEvent.KEY_VIDEO_DOWNSCALE_PERCENTAGE) Double video_downscale_percentage,
//            @Query(ViewerSessionEvent.KEY_VIDEO_UPSCALE_PERCENTAGE) Double video_upscale_percentage,
//            @Query(ViewerSessionEvent.KEY_BITRATE_MBPS) Float bitrate_mbps,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_DATA_1) String custom_data_1,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_DATA_2) String custom_data_2,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_DATA_3) String custom_data_3,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_DATA_4) String custom_data_4,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_DATA_5) String custom_data_5,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_DATA_6) String custom_data_6,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_DATA_7) String custom_data_7,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_DATA_8) String custom_data_8,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_DATA_9) String custom_data_9,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_DATA_10) String custom_data_10,
//            @Query(ViewerSessionEvent.KEY_ERROR_CODE) String error_code,
//            @Query(ViewerSessionEvent.KEY_ERROR) String error,
//            @Query(ViewerSessionEvent.KEY_ERROR_TEXT) String error_text,
//            @Query(ViewerSessionEvent.KEY_LATITUDE) String latitude,
//            @Query(ViewerSessionEvent.KEY_LONGITUDE) String longitude,
//            @Query(ViewerSessionEvent.KEY_VIDEO_COVER) String video_cover,
//            @Query(ViewerSessionEvent.KEY_VIDEO_COVER_REFERENCE) String video_cover_reference,
//            @Query(ViewerSessionEvent.KEY_PLAYER_REMOTE_PLAYED) Boolean player_remote_played,
//            @Query(ViewerSessionEvent.KEY_VIDEO_TOTAL_DURATION_MILLIS) Long video_total_duration_millis,
//            @Query(ViewerSessionEvent.KEY_VIDEO_WIDTH_PIXEL) Integer video_width_pixels,
//            @Query(ViewerSessionEvent.KEY_VIDEO_HEIGHT_PIXEL) Integer video_height_pixels,
//            @Query(ViewerSessionEvent.KEY_VIDEO_SOURCE_URL) String video_source_url,
//            @Query(ViewerSessionEvent.KEY_VIDEO_SOURCE_HOSTNAME) String video_source_hostname,
//            @Query(ViewerSessionEvent.KEY_VIDEO_SOURCE_TYPE) String video_source_type,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_CONTENT_TYPE) String custom_content_type,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_VIDEO_DURATION_MILLIS) Long custom_video_duration_millis,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_ENCODING_VARIANT) String custom_encoding_variant,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_VIDEO_LANGUAGE) String custom_video_language,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_VIDEO_ID) String custom_video_id,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_VIDEO_SERIES) String custom_video_series,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_VIDEO_PRODUCER) String custom_video_producer,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_VIDEO_TITLE) String custom_video_title,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_VIDEO_VARIANT_NAME) String custom_video_variant_name,
//            @Query(ViewerSessionEvent.KEY_CUSTOM_VIDEO_VARIANT) String custom_video_variant,
//            @Query(ViewerSessionEvent.KEY_MUTED) Boolean muted,
//            @Query(ViewerSessionEvent.KEY_PREVIOUS_EVENT) String previous_event,
//            @Query(ViewerSessionEvent.KEY_MILLIS_FROM_PREVIOUS_EVENT) Long millis_from_previous_event,
//            @Query(ViewerSessionEvent.KEY_CASTING) Boolean casting,
//            @Query(ViewerSessionEvent.KEY_FULLSCREEN) Boolean fullscreen,
//            @Query(ViewerSessionEvent.KEY_ORIENTATION) String orientation,
//            @Query(ViewerSessionEvent.KEY_ORIENTATION_FROM) String orientation_from,
//            @Query(ViewerSessionEvent.KEY_QUALITY) String quality,
//            @Query(ViewerSessionEvent.KEY_QUALITY_FROM) String quality_from,
//            @Query(ViewerSessionEvent.KEY_TIMESTAMP) String Z);
//
//    @GET("/")
//    Completable playerInstance(
//            @Query(ViewerSession.KEY_EVENT_FAMILY) String event_family,
//            @Query(PlayerInstance.KEY_PLAYER_INSTANCE_ID) String player_instance_id,
//            @Query(PlayerInstance.KEY_PROPERTY_ID) String property_id,
//            @Query(PlayerInstance.KEY_SESSION_ID) String session_id,
//            @Query(PlayerInstance.KEY_USER_ID) String user_id,
//            @Query(PlayerInstance.KEY_CUSTOM_USER_ID) String custom_user_id,
//            @Query(PlayerInstance.KEY_PLAYER_HEIGHT_PIXEL) Integer player_height_pixels,
//            @Query(PlayerInstance.KEY_PLAYER_WIDTH_PIXEL) Integer player_width_pixels,
//            @Query(PlayerInstance.KEY_META_PAGE_TYPE) String meta_page_type,
//            @Query(PlayerInstance.KEY_META_PAGE_URL) String meta_page_url,
//            @Query(PlayerInstance.KEY_PLAYER_SOFTWARE) String player_software,
//            @Query(PlayerInstance.KEY_PLAYER_LANGUAGE_CODE) String player_language_code,
//            @Query(PlayerInstance.KEY_PLAYER_NAME) String player_name,
//            @Query(PlayerInstance.KEY_ERROR) String error,
//            @Query(PlayerInstance.KEY_ERROR_CODE) String error_code,
//            @Query(PlayerInstance.KEY_ERROR_TEXT) String error_text,
//            @Query(PlayerInstance.KEY_CUSTOM_DATA_1) String custom_data_1,
//            @Query(PlayerInstance.KEY_CUSTOM_DATA_2) String custom_data_2,
//            @Query(PlayerInstance.KEY_CUSTOM_DATA_3) String custom_data_3,
//            @Query(PlayerInstance.KEY_CUSTOM_DATA_4) String custom_data_4,
//            @Query(PlayerInstance.KEY_CUSTOM_DATA_5) String custom_data_5,
//            @Query(PlayerInstance.KEY_CUSTOM_DATA_6) String custom_data_6,
//            @Query(PlayerInstance.KEY_CUSTOM_DATA_7) String custom_data_7,
//            @Query(PlayerInstance.KEY_CUSTOM_DATA_8) String custom_data_8,
//            @Query(PlayerInstance.KEY_CUSTOM_DATA_9) String custom_data_9,
//            @Query(PlayerInstance.KEY_CUSTOM_DATA_10) String custom_data_10,
//            @Query(PlayerInstance.KEY_PLAYER_INTEGRATION_VERSION) String player_integration_version,
//            @Query(PlayerInstance.KEY_PLAYER_SOFTWARE_VERSION) String player_software_version,
//            @Query(PlayerInstance.KEY_PLAYER_PRELOAD) Boolean player_preload,
//            @Query(PlayerInstance.KEY_PLAYER_AUTOPLAY) Boolean player_autoplay,
//            @Query(PlayerInstance.KEY_TIMESTAMP) String z);
//
//
//    @GET("/")
//    Completable playerEvents(
//            @Query(ViewerSession.KEY_EVENT_FAMILY) String event_family,
//            @Query(PlayerNetworkRequest.KEY_REQUEST_ID) String request_id,
//            @Query(PlayerNetworkRequest.KEY_SESSION_ID) String session_id,
//            @Query(PlayerNetworkRequest.KEY_USER_ID) String user_id,
//            @Query(PlayerNetworkRequest.KEY_PROPERTY_ID) String property_id,
//            @Query(PlayerNetworkRequest.KEY_PLAYER_INSTANCE_ID) String player_instance_id,
//            @Query(PlayerNetworkRequest.KEY_REQUEST_START) Long request_start,
//            @Query(PlayerNetworkRequest.KEY_REQUEST_RESPONSE_START) Long request_response_start,
//            @Query(PlayerNetworkRequest.KEY_REQUEST_RESPONSE_END) Long request_response_end,
//            @Query(PlayerNetworkRequest.KEY_REQUEST_TYPE) String request_type,
//            @Query(PlayerNetworkRequest.KEY_REQUEST_HOSTNAME) String request_hostname,
//            @Query(PlayerNetworkRequest.KEY_REQUEST_BYTE_LOADED) Integer request_bytes_loaded,
//            @Query(PlayerNetworkRequest.KEY_RESPONSE_HEADER) String request_response_headers,
//            @Query(PlayerNetworkRequest.KEY_REQUEST_MEDIA_DURATION_MILLIS) Integer request_media_duration_millis,
//            @Query(PlayerNetworkRequest.KEY_REQUEST_VIDEO_WIDTH_PIXELS) Integer request_video_width_pixels,
//            @Query(PlayerNetworkRequest.KEY_REQUEST_VIDEO_HEIGHT_PIXELS) Integer request_video_height_pixels,
//            @Query(PlayerNetworkRequest.KEY_ERROR) String error,
//            @Query(PlayerNetworkRequest.KEY_ERROR_CODE) String error_code,
//            @Query(PlayerNetworkRequest.KEY_ERROR_TEXT) String error_text,
//            @Query(PlayerNetworkRequest.KEY_TIMESTAMP) String z);

}
