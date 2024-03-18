package com.fivecentscdnanalytics.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.fivecentscdnanalytics.calls.api.ApiConfig;
import com.fivecentscdnanalytics.utils.NetworkUtil;
import com.fivecentscdnanalytics.utils.Util;

public class ViewerSessionEvent implements Parcelable {

    public static final String KEY_EVENT_ID = "event_id";
    public static final String KEY_SESSION_ID = "session_id";
    public static final String KEY_PROPERTY_ID = "property_id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_PLAYER_INSTANCE_ID = "player_instance_id";
    public static final String KEY_PLAYBACK_ID = "playback_id";
    public static final String KEY_EVENT = "event";
    public static final String KEY_FROM = "from";
    public static final String KEY_TO = "to";
    public static final String KEY_PLAYBACK_TIME_INSTANT_MILLIS = "playback_time_instant_millis";
    public static final String KEY_VIDEO_DOWNSCALE_PERCENTAGE = "video_downscale_percentage";
    public static final String KEY_VIDEO_UPSCALE_PERCENTAGE = "video_upscale_percentage";
    public static final String KEY_BITRATE_MBPS = "bitrate_mbps";
    public static final String KEY_CUSTOM_DATA_1 = "custom_data_1";
    public static final String KEY_CUSTOM_DATA_2 = "custom_data_2";
    public static final String KEY_CUSTOM_DATA_3 = "custom_data_3";
    public static final String KEY_CUSTOM_DATA_4 = "custom_data_4";
    public static final String KEY_CUSTOM_DATA_5 = "custom_data_5";
    public static final String KEY_CUSTOM_DATA_6 = "custom_data_6";
    public static final String KEY_CUSTOM_DATA_7 = "custom_data_7";
    public static final String KEY_CUSTOM_DATA_8 = "custom_data_8";
    public static final String KEY_CUSTOM_DATA_9 = "custom_data_9";
    public static final String KEY_CUSTOM_DATA_10 = "custom_data_10";
    public static final String KEY_ERROR_CODE = "error_code";
    public static final String KEY_ERROR = "error";
    public static final String KEY_ERROR_TEXT = "error_text";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_VIDEO_COVER = "video_cover";
    public static final String KEY_VIDEO_COVER_REFERENCE = "video_cover_reference";
    public static final String KEY_PLAYER_REMOTE_PLAYED = "player_remote_played";
    public static final String KEY_VIDEO_TOTAL_DURATION_MILLIS = "video_total_duration_millis";
    public static final String KEY_VIDEO_WIDTH_PIXEL = "video_width_pixels";
    public static final String KEY_VIDEO_HEIGHT_PIXEL = "video_height_pixels";
    public static final String KEY_VIDEO_SOURCE_URL = "video_source_url";
    public static final String KEY_VIDEO_SOURCE_HOSTNAME = "video_source_hostname";
    public static final String KEY_VIDEO_SOURCE_TYPE = "video_source_type";
    public static final String KEY_CUSTOM_CONTENT_TYPE = "custom_content_type";
    public static final String KEY_CUSTOM_VIDEO_DURATION_MILLIS = "custom_video_duration_millis";
    public static final String KEY_CUSTOM_ENCODING_VARIANT = "custom_encoding_variant";
    public static final String KEY_CUSTOM_VIDEO_LANGUAGE = "custom_video_language";
    public static final String KEY_CUSTOM_VIDEO_ID = "custom_video_id";
    public static final String KEY_CUSTOM_VIDEO_SERIES = "custom_video_series";
    public static final String KEY_CUSTOM_VIDEO_PRODUCER = "custom_video_producer";
    public static final String KEY_CUSTOM_VIDEO_TITLE = "custom_video_title";
    public static final String KEY_CUSTOM_VIDEO_VARIANT_NAME = "custom_video_variant_name";
    public static final String KEY_CUSTOM_VIDEO_VARIANT = "custom_video_variant";
    public static final String KEY_MUTED = "muted";
    public static final String KEY_PREVIOUS_EVENT = "previous_event";
    public static final String KEY_MILLIS_FROM_PREVIOUS_EVENT = "millis_from_previous_event";
    public static final String KEY_CASTING = "casting";
    public static final String KEY_FULLSCREEN = "fullscreen";
    public static final String KEY_ORIENTATION = "orientation";
    public static final String KEY_ORIENTATION_FROM = "orientation_from";
    public static final String KEY_QUALITY = "quality";
    public static final String KEY_QUALITY_FROM = "quality_from";
    public static final String KEY_TIMESTAMP = "z";

    private String eventId;
    private String sessionId;
    private String propertyId;
    private String userId;
    private String playerInstanceId;
    private String playbackId;
    private String event;
    private Long from;
    private Long to;
    private Long playbackTimeInstantMillis;
    private Double videoDownscalePercentage;
    private Double videoUpscalePercentage;
    private Float bitrateMbps;
    private String customData1;
    private String customData2;
    private String customData3;
    private String customData4;
    private String customData5;
    private String customData6;
    private String customData7;
    private String customData8;
    private String customData9;
    private String customData10;
    private String error;
    private String errorCode;
    private String errorText;
    private String latitude;
    private String longitude;
    private String videoCover;
    private String videoCoverReference;
    private Boolean playerRemotePlayed;
    private Long videoTotalDurationMillis;
    private Integer videoWidthPixels;
    private Integer videoHeightPixels;

    private String videoSourceUrl;
    private String videoSourceHostname;
    private String videoSourceType;

    private String customContentType;
    private Long customVideoDurationMillis;
    private String customEncodingVariant;
    private String customVideoLanguage;
    private String customVideoId;
    private String customVideoSeries;
    private String customVideoProducer;
    private String customVideoTitle;
    private String customVideoVariantName;
    private String customVideoVariant;

    private Boolean muted;
    private String previousEvent;
    private Long millisFromPreviousEvent;
    private Boolean casting;
    private Boolean fullscreen;
    private String orientation;
    private String orientationFrom;
    private String quality;
    private String qualityFrom;
    private Long timestamp;

    private boolean setupEventDone;
    private long eventDataTime;

    public ViewerSessionEvent() {
    }

    public ViewerSessionEvent(Context context) {

        this.eventId = Util.getRandomCharacterString();
        this.sessionId = ApiConfig.SESSION_ID;
        this.propertyId = "";
        this.userId = NetworkUtil.getDeviceId(context);
        this.playerInstanceId = ApiConfig.PLAYER_INSTANCE_ID;
        this.playbackId = Util.getRandomCharacterString();
        this.event = null;
        this.from = 0l;
        this.to = 0l;
        this.playbackTimeInstantMillis = 0l;
        this.videoDownscalePercentage = 0.0;
        this.videoUpscalePercentage = 0.0;
        this.bitrateMbps = 0f;
        this.customData1 = null;
        this.customData2 = null;
        this.customData3 = null;
        this.customData4 = null;
        this.customData5 = null;
        this.customData6 = null;
        this.customData7 = null;
        this.customData8 = null;
        this.customData9 = null;
        this.customData10 = null;
        this.error = null;
        this.errorCode = null;
        this.errorText = null;
        this.latitude = null;
        this.longitude = null;
        this.videoCover = null;
        this.videoCoverReference = null;
        this.playerRemotePlayed = false;
        this.videoTotalDurationMillis = 0L;
        this.videoSourceUrl = null;
        this.videoSourceHostname = null;
        this.videoSourceType = null;
        this.customContentType = null;
        this.customVideoDurationMillis = 0l;
        this.customEncodingVariant = null;
        this.customVideoLanguage = null;
        this.customVideoId = null;
        this.customVideoSeries = null;
        this.customVideoProducer = null;
        this.customVideoTitle = null;
        this.customVideoVariantName = null;
        this.customVideoVariant = null;
        this.muted = false;
        this.previousEvent = null;
        this.millisFromPreviousEvent = 0l;
        this.casting = false;
        this.fullscreen = false;
        this.orientation = null;
        this.orientationFrom = null;
        this.quality = null;
        this.qualityFrom = null;
        this.timestamp = 0L;
    }

    protected ViewerSessionEvent(Parcel in) {
        eventId = in.readString();
        sessionId = in.readString();
        propertyId = in.readString();
        userId = in.readString();
        playerInstanceId = in.readString();
        playbackId = in.readString();
        event = in.readString();
        if (in.readByte() == 0) {
            from = null;
        } else {
            from = in.readLong();
        }
        if (in.readByte() == 0) {
            to = null;
        } else {
            to = in.readLong();
        }
        if (in.readByte() == 0) {
            playbackTimeInstantMillis = null;
        } else {
            playbackTimeInstantMillis = in.readLong();
        }
        if (in.readByte() == 0) {
            videoDownscalePercentage = null;
        } else {
            videoDownscalePercentage = in.readDouble();
        }
        if (in.readByte() == 0) {
            videoUpscalePercentage = null;
        } else {
            videoUpscalePercentage = in.readDouble();
        }
        if (in.readByte() == 0) {
            bitrateMbps = null;
        } else {
            bitrateMbps = in.readFloat();
        }
        customData1 = in.readString();
        customData2 = in.readString();
        customData3 = in.readString();
        customData4 = in.readString();
        customData5 = in.readString();
        customData6 = in.readString();
        customData7 = in.readString();
        customData8 = in.readString();
        customData9 = in.readString();
        customData10 = in.readString();
        error = in.readString();
        errorCode = in.readString();
        errorText = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        videoCover = in.readString();
        videoCoverReference = in.readString();
        byte tmpPlayerRemotePlayed = in.readByte();
        playerRemotePlayed = tmpPlayerRemotePlayed == 0 ? null : tmpPlayerRemotePlayed == 1;
        if (in.readByte() == 0) {
            videoTotalDurationMillis = null;
        } else {
            videoTotalDurationMillis = in.readLong();
        }
        if (in.readByte() == 0) {
            videoWidthPixels = null;
        } else {
            videoWidthPixels = in.readInt();
        }
        if (in.readByte() == 0) {
            videoHeightPixels = null;
        } else {
            videoHeightPixels = in.readInt();
        }
        videoSourceUrl = in.readString();
        videoSourceHostname = in.readString();
        videoSourceType = in.readString();
        customContentType = in.readString();
        if (in.readByte() == 0) {
            customVideoDurationMillis = null;
        } else {
            customVideoDurationMillis = in.readLong();
        }
        customEncodingVariant = in.readString();
        customVideoLanguage = in.readString();
        customVideoId = in.readString();
        customVideoSeries = in.readString();
        customVideoProducer = in.readString();
        customVideoTitle = in.readString();
        customVideoVariantName = in.readString();
        customVideoVariant = in.readString();
        byte tmpMuted = in.readByte();
        muted = tmpMuted == 0 ? null : tmpMuted == 1;
        previousEvent = in.readString();
        if (in.readByte() == 0) {
            millisFromPreviousEvent = null;
        } else {
            millisFromPreviousEvent = in.readLong();
        }
        byte tmpCasting = in.readByte();
        casting = tmpCasting == 0 ? null : tmpCasting == 1;
        byte tmpFullscreen = in.readByte();
        fullscreen = tmpFullscreen == 0 ? null : tmpFullscreen == 1;
        orientation = in.readString();
        orientationFrom = in.readString();
        quality = in.readString();
        qualityFrom = in.readString();
    }

    public static final Creator<ViewerSessionEvent> CREATOR = new Creator<ViewerSessionEvent>() {
        @Override
        public ViewerSessionEvent createFromParcel(Parcel in) {
            return new ViewerSessionEvent(in);
        }

        @Override
        public ViewerSessionEvent[] newArray(int size) {
            return new ViewerSessionEvent[size];
        }
    };

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlayerInstanceId() {
        return playerInstanceId;
    }

    public void setPlayerInstanceId(String playerInstanceId) {
        this.playerInstanceId = playerInstanceId;
    }

    public String getPlaybackId() {
        return playbackId;
    }

    public void setPlaybackId(String playbackId) {
        this.playbackId = playbackId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Long getPlaybackTimeInstantMillis() {
        return playbackTimeInstantMillis;
    }

    public void setPlaybackTimeInstantMillis(Long playbackTimeInstantMillis) {
        this.playbackTimeInstantMillis = playbackTimeInstantMillis;
    }

    public Double getVideoDownscalePercentage() {
        return videoDownscalePercentage;
    }

    public void setVideoDownscalePercentage(Double videoDownscalePercentage) {
        this.videoDownscalePercentage = videoDownscalePercentage;
    }

    public Double getVideoUpscalePercentage() {
        return videoUpscalePercentage;
    }

    public void setVideoUpscalePercentage(Double videoUpscalePercentage) {
        this.videoUpscalePercentage = videoUpscalePercentage;
    }

    public Float getBitrateMbps() {
        return bitrateMbps;
    }

    public void setBitrateMbps(Float bitrateMbps) {
        this.bitrateMbps = bitrateMbps;
    }

    public String getCustomData1() {
        return customData1;
    }

    public void setCustomData1(String customData1) {
        this.customData1 = customData1;
    }

    public String getCustomData2() {
        return customData2;
    }

    public void setCustomData2(String customData2) {
        this.customData2 = customData2;
    }

    public String getCustomData3() {
        return customData3;
    }

    public void setCustomData3(String customData3) {
        this.customData3 = customData3;
    }

    public String getCustomData4() {
        return customData4;
    }

    public void setCustomData4(String customData4) {
        this.customData4 = customData4;
    }

    public String getCustomData5() {
        return customData5;
    }

    public void setCustomData5(String customData5) {
        this.customData5 = customData5;
    }

    public String getCustomData6() {
        return customData6;
    }

    public void setCustomData6(String customData6) {
        this.customData6 = customData6;
    }

    public String getCustomData7() {
        return customData7;
    }

    public void setCustomData7(String customData7) {
        this.customData7 = customData7;
    }

    public String getCustomData8() {
        return customData8;
    }

    public void setCustomData8(String customData8) {
        this.customData8 = customData8;
    }

    public String getCustomData9() {
        return customData9;
    }

    public void setCustomData9(String customData9) {
        this.customData9 = customData9;
    }

    public String getCustomData10() {
        return customData10;
    }

    public void setCustomData10(String customData10) {
        this.customData10 = customData10;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getVideoCoverReference() {
        return videoCoverReference;
    }

    public void setVideoCoverReference(String videoCoverReference) {
        this.videoCoverReference = videoCoverReference;
    }

    public Boolean getPlayerRemotePlayed() {
        return playerRemotePlayed;
    }

    public void setPlayerRemotePlayed(Boolean playerRemotePlayed) {
        this.playerRemotePlayed = playerRemotePlayed;
    }

    public Long getVideoTotalDurationMillis() {
        return videoTotalDurationMillis;
    }

    public void setVideoTotalDurationMillis(Long videoTotalDurationMillis) {
        this.videoTotalDurationMillis = videoTotalDurationMillis;
    }

    public Integer getVideoWidthPixels() {
        return videoWidthPixels;
    }

    public void setVideoWidthPixels(Integer videoWidthPixels) {
        this.videoWidthPixels = videoWidthPixels;
    }

    public Integer getVideoHeightPixels() {
        return videoHeightPixels;
    }

    public void setVideoHeightPixels(Integer videoHeightPixels) {
        this.videoHeightPixels = videoHeightPixels;
    }

    public String getVideoSourceUrl() {
        return videoSourceUrl;
    }

    public void setVideoSourceUrl(String videoSourceUrl) {
        this.videoSourceUrl = videoSourceUrl;
    }

    public String getVideoSourceHostname() {
        return videoSourceHostname;
    }

    public void setVideoSourceHostname(String videoSourceHostname) {
        this.videoSourceHostname = videoSourceHostname;
    }

    public String getVideoSourceType() {
        return videoSourceType;
    }

    public void setVideoSourceType(String videoSourceType) {
        this.videoSourceType = videoSourceType;
    }

    public String getCustomContentType() {
        return customContentType;
    }

    public void setCustomContentType(String customContentType) {
        this.customContentType = customContentType;
    }

    public Long getCustomVideoDurationMillis() {
        return customVideoDurationMillis;
    }

    public void setCustomVideoDurationMillis(Long customVideoDurationMillis) {
        this.customVideoDurationMillis = customVideoDurationMillis;
    }

    public String getCustomEncodingVariant() {
        return customEncodingVariant;
    }

    public void setCustomEncodingVariant(String customEncodingVariant) {
        this.customEncodingVariant = customEncodingVariant;
    }

    public String getCustomVideoLanguage() {
        return customVideoLanguage;
    }

    public void setCustomVideoLanguage(String customVideoLanguage) {
        this.customVideoLanguage = customVideoLanguage;
    }

    public String getCustomVideoId() {
        return customVideoId;
    }

    public void setCustomVideoId(String customVideoId) {
        this.customVideoId = customVideoId;
    }

    public String getCustomVideoSeries() {
        return customVideoSeries;
    }

    public void setCustomVideoSeries(String customVideoSeries) {
        this.customVideoSeries = customVideoSeries;
    }

    public String getCustomVideoProducer() {
        return customVideoProducer;
    }

    public void setCustomVideoProducer(String customVideoProducer) {
        this.customVideoProducer = customVideoProducer;
    }

    public String getCustomVideoTitle() {
        return customVideoTitle;
    }

    public void setCustomVideoTitle(String customVideoTitle) {
        this.customVideoTitle = customVideoTitle;
    }

    public String getCustomVideoVariantName() {
        return customVideoVariantName;
    }

    public void setCustomVideoVariantName(String customVideoVariantName) {
        this.customVideoVariantName = customVideoVariantName;
    }

    public String getCustomVideoVariant() {
        return customVideoVariant;
    }

    public void setCustomVideoVariant(String customVideoVariant) {
        this.customVideoVariant = customVideoVariant;
    }

    public Boolean getMuted() {
        return muted;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }

    public String getPreviousEvent() {
        return previousEvent;
    }

    public void setPreviousEvent(String previousEvent) {
        this.previousEvent = previousEvent;
    }

    public Long getMillisFromPreviousEvent() {
        return millisFromPreviousEvent;
    }

    public void setMillisFromPreviousEvent(Long millisFromPreviousEvent) {
        this.millisFromPreviousEvent = millisFromPreviousEvent;
    }

    public Boolean getCasting() {
        return casting;
    }

    public void setCasting(Boolean casting) {
        this.casting = casting;
    }

    public Boolean getFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(Boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getOrientationFrom() {
        return orientationFrom;
    }

    public void setOrientationFrom(String orientationFrom) {
        this.orientationFrom = orientationFrom;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getQualityFrom() {
        return qualityFrom;
    }

    public void setQualityFrom(String qualityFrom) {
        this.qualityFrom = qualityFrom;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSetupEventDone() {
        return setupEventDone;
    }

    public void setSetupEventDone(boolean setupEventDone) {
        this.setupEventDone = setupEventDone;
    }

    @Override
    public String toString() {
        return "ViewerSessionEvent{" +
                "eventId='" + eventId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", propertyId='" + propertyId + '\'' +
                ", userId='" + userId + '\'' +
                ", playerInstanceId='" + playerInstanceId + '\'' +
                ", playbackId='" + playbackId + '\'' +
                ", event='" + event + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", playbackTimeInstantMillis=" + playbackTimeInstantMillis +
                ", videoDownscalePercentage=" + videoDownscalePercentage +
                ", videoUpscalePercentage=" + videoUpscalePercentage +
                ", bitrateMbps=" + bitrateMbps +
                ", customData1='" + customData1 + '\'' +
                ", customData2='" + customData2 + '\'' +
                ", customData3='" + customData3 + '\'' +
                ", customData4='" + customData4 + '\'' +
                ", customData5='" + customData5 + '\'' +
                ", customData6='" + customData6 + '\'' +
                ", customData7='" + customData7 + '\'' +
                ", customData8='" + customData8 + '\'' +
                ", customData9='" + customData9 + '\'' +
                ", customData10='" + customData10 + '\'' +
                ", error='" + error + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", errorText='" + errorText + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", videoCover='" + videoCover + '\'' +
                ", videoCoverReference='" + videoCoverReference + '\'' +
                ", playerRemotePlayed=" + playerRemotePlayed +
                ", videoTotalDurationMillis=" + videoTotalDurationMillis +
                ", videoWidthPixels=" + videoWidthPixels +
                ", videoHeightPixels=" + videoHeightPixels +
                ", videoSourceUrl='" + videoSourceUrl + '\'' +
                ", videoSourceHostname='" + videoSourceHostname + '\'' +
                ", videoSourceType='" + videoSourceType + '\'' +
                ", customContentType='" + customContentType + '\'' +
                ", customVideoDurationMillis=" + customVideoDurationMillis +
                ", customEncodingVariant='" + customEncodingVariant + '\'' +
                ", customVideoLanguage='" + customVideoLanguage + '\'' +
                ", customVideoId='" + customVideoId + '\'' +
                ", customVideoSeries='" + customVideoSeries + '\'' +
                ", customVideoProducer='" + customVideoProducer + '\'' +
                ", customVideoTitle='" + customVideoTitle + '\'' +
                ", customVideoVariantName='" + customVideoVariantName + '\'' +
                ", customVideoVariant='" + customVideoVariant + '\'' +
                ", muted=" + muted +
                ", previousEvent='" + previousEvent + '\'' +
                ", millisFromPreviousEvent=" + millisFromPreviousEvent +
                ", casting=" + casting +
                ", fullscreen=" + fullscreen +
                ", orientation='" + orientation + '\'' +
                ", orientationFrom='" + orientationFrom + '\'' +
                ", quality='" + quality + '\'' +
                ", qualityFrom='" + qualityFrom + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(eventId);
        parcel.writeString(sessionId);
        parcel.writeString(propertyId);
        parcel.writeString(userId);
        parcel.writeString(playerInstanceId);
        parcel.writeString(playbackId);
        parcel.writeString(event);
        if (from == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(from);
        }
        if (to == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(to);
        }
        if (playbackTimeInstantMillis == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(playbackTimeInstantMillis);
        }
        if (videoDownscalePercentage == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(videoDownscalePercentage);
        }
        if (videoUpscalePercentage == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(videoUpscalePercentage);
        }
        if (bitrateMbps == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(bitrateMbps);
        }
        parcel.writeString(customData1);
        parcel.writeString(customData2);
        parcel.writeString(customData3);
        parcel.writeString(customData4);
        parcel.writeString(customData5);
        parcel.writeString(customData6);
        parcel.writeString(customData7);
        parcel.writeString(customData8);
        parcel.writeString(customData9);
        parcel.writeString(customData10);
        parcel.writeString(error);
        parcel.writeString(errorCode);
        parcel.writeString(errorText);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(videoCover);
        parcel.writeString(videoCoverReference);
        parcel.writeByte((byte) (playerRemotePlayed == null ? 0 : playerRemotePlayed ? 1 : 2));
        if (videoTotalDurationMillis == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(videoTotalDurationMillis);
        }
        if (videoWidthPixels == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(videoWidthPixels);
        }
        if (videoHeightPixels == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(videoHeightPixels);
        }
        parcel.writeString(videoSourceUrl);
        parcel.writeString(videoSourceHostname);
        parcel.writeString(videoSourceType);
        parcel.writeString(customContentType);
        if (customVideoDurationMillis == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(customVideoDurationMillis);
        }
        parcel.writeString(customEncodingVariant);
        parcel.writeString(customVideoLanguage);
        parcel.writeString(customVideoId);
        parcel.writeString(customVideoSeries);
        parcel.writeString(customVideoProducer);
        parcel.writeString(customVideoTitle);
        parcel.writeString(customVideoVariantName);
        parcel.writeString(customVideoVariant);
        parcel.writeByte((byte) (muted == null ? 0 : muted ? 1 : 2));
        parcel.writeString(previousEvent);
        if (millisFromPreviousEvent == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(millisFromPreviousEvent);
        }
        parcel.writeByte((byte) (casting == null ? 0 : casting ? 1 : 2));
        parcel.writeByte((byte) (fullscreen == null ? 0 : fullscreen ? 1 : 2));
        parcel.writeString(orientation);
        parcel.writeString(orientationFrom);
        parcel.writeString(quality);
        parcel.writeString(qualityFrom);
    }

    public long isEventDataTime() {
        return eventDataTime;
    }

    public void setEventDataTime(long eventDataTime) {
        this.eventDataTime = eventDataTime;
    }

    @NonNull
    @Override
    public ViewerSessionEvent clone()  {
        ViewerSessionEvent event = new ViewerSessionEvent();
        event.setEventId(this.eventId);
        event.setSessionId(this.sessionId);
        event.setPropertyId(this.propertyId);
        event.setUserId(this.userId);
        event.setPlayerInstanceId(this.playerInstanceId);
        event.setPlaybackId(this.playbackId);
        event.setEvent(this.event);
        event.setFrom(this.from);
        event.setTo(this.to);
        event.setPlaybackTimeInstantMillis(this.playbackTimeInstantMillis);
        event.setVideoDownscalePercentage(this.videoDownscalePercentage);
        event.setVideoUpscalePercentage(this.videoUpscalePercentage);
        event.setBitrateMbps(this.bitrateMbps);
        event.setCustomData1(this.customData1);
        event.setCustomData2(this.customData2);
        event.setCustomData3(this.customData3);
        event.setCustomData4(this.customData4);
        event.setCustomData5(this.customData5);
        event.setCustomData6(this.customData6);
        event.setCustomData7(this.customData7);
        event.setCustomData8(this.customData8);
        event.setCustomData9(this.customData9);
        event.setCustomData10(this.customData10);
        event.setError(this.error);
        event.setErrorCode(this.errorCode);
        event.setErrorText(this.errorText);
        event.setLatitude(this.latitude);
        event.setLongitude(this.longitude);
        event.setVideoCover(this.videoCover);
        event.setVideoCoverReference(this.videoCoverReference);
        event.setPlayerRemotePlayed(this.playerRemotePlayed);
        event.setVideoTotalDurationMillis(this.videoTotalDurationMillis);
        event.setVideoWidthPixels(this.videoWidthPixels);
        event.setVideoHeightPixels(this.videoHeightPixels);
        event.setVideoSourceUrl(this.videoSourceUrl);
        event.setVideoSourceHostname(this.videoSourceHostname);
        event.setVideoSourceType(this.videoSourceType);
        event.setCustomContentType(this.customContentType);
        event.setCustomVideoDurationMillis(this.customVideoDurationMillis);
        event.setCustomEncodingVariant(this.customEncodingVariant);
        event.setCustomVideoLanguage(this.customVideoLanguage);
        event.setCustomVideoId(this.customVideoId);
        event.setCustomVideoSeries(this.customVideoSeries);
        event.setCustomVideoProducer(this.customVideoProducer);
        event.setCustomVideoTitle(this.customVideoTitle);
        event.setCustomVideoVariantName(this.customVideoVariantName);
        event.setCustomVideoVariant(this.customVideoVariant);
        event.setMuted(this.muted);
        event.setPreviousEvent(this.previousEvent);
        event.setMillisFromPreviousEvent(this.millisFromPreviousEvent);
        event.setCasting(this.casting);
        event.setFullscreen(this.fullscreen);
        event.setOrientation(this.orientation);
        event.setOrientationFrom(this.orientationFrom);
        event.setQuality(this.quality);
        event.setQualityFrom(this.qualityFrom);
        event.setTimestamp(this.timestamp);
        event.setSetupEventDone(this.setupEventDone);
        event.setEventDataTime(this.eventDataTime);

        return event;
    }
}
