package com.fivecentscdnanalytics.calls.api;


import com.fivecentscdnanalytics.utils.Util;

public class ApiConfig {
    public static final String BASE_URL = "https://collector-videoplayer.5centscdn.net";
    public static final int RANDOM_STRING_LENGTH = 24;
    public static final String DEVICE_CATEGORY = "PHONE";
    public static final String CLIENT_VERSION = "1.0";
    public static String SESSION_ID = Util.getRandomCharacterString();
    public static String PLAYER_INSTANCE_ID = Util.getRandomCharacterString();
    public static final boolean PRODUCTION_ENV = false;

    public final static String HEADER_ACCEPT = "Accept";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_ACCEPT_DATA = "application/json";
    public static final String TYPE_PAGE_LOAD = "page_load";
    public static final String TYPE_PLAYER_LOAD = "player_load";
    public static final String TYPE_IMPRESSION = "impression";
    public static final String TYPE_PLAY = "play";
    public static final String TYPE_ENGAGEMENT = "engagement";
    public static final String TYPE_COMPLETE = "complete";
}
