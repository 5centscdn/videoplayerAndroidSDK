package com.fivecentscdnanalytics.utils;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Build;
import android.os.SystemClock;
import android.util.Pair;

import com.fivecentscdnanalytics.calls.api.ApiConfig;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;

public class Util {
    public static final String COLLECTOR_CORE_VERSION = "1.0";
    public static final String DASH_STREAM_FORMAT = "dash";
    public static final String HLS_STREAM_FORMAT = "hls";
    public static final String PROGRESSIVE_STREAM_FORMAT = "progressive";
    public static final String SMOOTH_STREAM_FORMAT = "smooth";
    public static final int MILLISECONDS_IN_SECONDS = 1000;
    public static final int VIDEOSTART_TIMEOUT = 1000 * 60; // in milliseconds
    public static final int ANALYTICS_QUALITY_CHANGE_COUNT_THRESHOLD = 50;
    public static final int ANALYTICS_QUALITY_CHANGE_COUNT_RESET_INTERVAL =
            1000 * 60 * 60; // in milliseconds;
    public static final int REBUFFERING_TIMEOUT = 1000 * 60 * 2; // in milliseconds

    private static final Map<String, String> VIDEO_FORMAT_MIME_TYPE_MAP;
    public static final String PLAYER_TECH = "Android:Exoplayer";
    public static final String UNKNOWN = "UNKNOWN";

    static {
        VIDEO_FORMAT_MIME_TYPE_MAP = new HashMap<>();
        VIDEO_FORMAT_MIME_TYPE_MAP.put("avc", "video/avc");
        VIDEO_FORMAT_MIME_TYPE_MAP.put("hevc", "video/hevc");
        VIDEO_FORMAT_MIME_TYPE_MAP.put("vp9", "video/x-vnd.on2.vp9");
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Returns the time in ms since the system was booted, and guaranteed to be monotonic Details
     * here: https://developer.android.com/reference/android/os/SystemClock
     *
     * @return The time in ms since the system was booted, and guaranteed to be monotonic.
     */
    public static long getElapsedTime() {
        return SystemClock.elapsedRealtime();
    }

    public static long getTimestamp() {
        return System.currentTimeMillis();
    }

    public static String getLocale() {
        return Resources.getSystem().getConfiguration().locale.toString();
    }

    public static List<String> getSupportedVideoFormats() {
        List<String> codecs = new ArrayList<>();
        for (String format : VIDEO_FORMAT_MIME_TYPE_MAP.keySet()) {
            if (isMimeTypeSupported(VIDEO_FORMAT_MIME_TYPE_MAP.get(format))) {
                codecs.add(format);
            }
        }
        return codecs;
    }

    @SuppressWarnings("deprecation")
    public static boolean isMimeTypeSupported(String mimeType) {
        int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);

            if (codecInfo.isEncoder()) {
                continue;
            }

            String[] types = codecInfo.getSupportedTypes();
            for (String type : types) {
                if (type.equalsIgnoreCase(mimeType)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Integer calculatePercentage(Long numerator, Long denominator, Boolean clamp) {
        if (denominator == null || denominator == 0 || numerator == null) {
            return null;
        }
        int result = Math.round((numerator.floatValue() / denominator.floatValue()) * 100);
        return clamp ? Math.min(result, 100) : result;
    }

    public static Pair<String, String> getHostnameAndPath(String uriString) {
        try {
            URI uri = new URI(uriString);
            return new Pair<>(uri.getHost(), uri.getPath());
        } catch (Exception ignored) {

        }
        return new Pair<>(null, null);
    }

    public static boolean getIsLiveFromConfigOrPlayer(
            boolean isPlayerReady, Boolean isLiveFromConfig, boolean isLiveFromPlayer) {
        if (isPlayerReady) {
            return isLiveFromPlayer;
        }
        return isLiveFromConfig != null ? isLiveFromConfig : false;
    }

    public static boolean isClassLoaded(String className, ClassLoader loader) {
        try {
            Class.forName(className, false, loader);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static long toPrimitiveLong(Double value) {
        return value == null ? 0 : value.longValue();
    }

    public static Double multiply(Double value, Long multiplicand) {
        return value == null || multiplicand == null ? null : value * multiplicand;
    }

    public static Double multiply(Double value, Integer multiplicand) {
        return value == null || multiplicand == null ? null : value * multiplicand;
    }

    public static long secondsToMillis(Double seconds) {
        return toPrimitiveLong(multiply(seconds, MILLISECONDS_IN_SECONDS));
    }

    public static String joinUrl(String baseUrl, String relativeUrl) {
        StringBuilder result = new StringBuilder(baseUrl);
        if (!baseUrl.endsWith("/")) {
            result.append('/');
        }

        if (relativeUrl.startsWith("/")) {
            relativeUrl = relativeUrl.substring(1);
        }
        result.append(relativeUrl);
        return result.toString();
    }

    public static Boolean isTVDevice(Context context) {
        Object untypedUiModeManager = context.getSystemService(Context.UI_MODE_SERVICE);
        if (untypedUiModeManager instanceof UiModeManager) {
            UiModeManager uiModeManager = (UiModeManager) untypedUiModeManager;
            return uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION;
        }
        return false;
    }

    public static String getPlatform(Boolean isTV) {
        return isTV ? "androidTV" : "android";
    }

    public static String getDomain(Context context) {
        return context.getPackageName();
    }

    public static String getAnalyticsVersion() {
        return COLLECTOR_CORE_VERSION;
    }

    public static <T> Object getPrivateFieldFromReflection(T source, String fieldName) {
        if (source == null) {
            return null;
        }
        try {
            Field privateField = source.getClass().getDeclaredField(fieldName);
            privateField.setAccessible(true);
            return privateField.get(source);
        } catch (Exception ignored) {
        }
        return null;
    }

    public static String getRandomCharacterString(){
        //String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        RandomStringUtils.random(ApiConfig.RANDOM_STRING_LENGTH, characters );

        return Util.getUUID();
    }


    public static String getTimeStampInMs() {

        StringBuilder strignBuilder = new StringBuilder(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        strignBuilder.insert(strignBuilder.length()- 3, ".");

        return strignBuilder.toString();
    }

    public static String getTimeStampInMs(long timestamp) {

        StringBuilder strignBuilder = new StringBuilder(String.valueOf(timestamp));
        strignBuilder.insert(strignBuilder.length()- 3, ".");

        return strignBuilder.toString();
    }


    public static Integer getDeviceDensity(){
        int density = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            density = Resources.getSystem().getConfiguration().densityDpi;
        }
        return density;
    }
}
