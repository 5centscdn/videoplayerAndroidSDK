package com.fivecentscdnanalytics.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.fivecentscdnanalytics.utils.NetworkUtil;
import com.fivecentscdnanalytics.utils.Util;


public class PlayerNetworkRequest implements Parcelable {

    public static final String KEY_REQUEST_ID = "request_id";
    public static final String KEY_SESSION_ID = "session_id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_PROPERTY_ID = "property_id";
    public static final String KEY_PLAYER_INSTANCE_ID = "player_instance_id";
    public static final String KEY_REQUEST_START = "request_start";
    public static final String KEY_REQUEST_RESPONSE_START = "request_response_start";
    public static final String KEY_REQUEST_RESPONSE_END = "request_response_end";
    public static final String KEY_REQUEST_TYPE = "request_type";
    public static final String KEY_REQUEST_HOSTNAME = "request_hostname";
    public static final String KEY_REQUEST_BYTE_LOADED = "request_bytes_loaded";
    public static final String KEY_RESPONSE_HEADER = "request_response_headers";
    public static final String KEY_REQUEST_MEDIA_DURATION_MILLIS = "request_media_duration_millis";
    public static final String KEY_REQUEST_VIDEO_WIDTH_PIXELS = "request_video_width_pixels";
    public static final String KEY_REQUEST_VIDEO_HEIGHT_PIXELS = "request_video_height_pixels";
    public static final String KEY_ERROR = "error";
    public static final String KEY_ERROR_CODE = "error_code";
    public static final String KEY_ERROR_TEXT = "error_text";
    public static final String KEY_TIMESTAMP = "z";

    private String requestId;
    private String sessionId;
    private String userId;
    private String propertyId;
    private String playerInstanceId;
    private Long requestStart;
    private Long requestResponseStart;
    private Long requestResponseEnd;
    private String requestType;
    private String requestHostName;
    private Integer requestByteLoaded;
    private String requestResponseHeaders;
    private Integer requestMediaDurationMillis;
    private Integer requestVideoWidthPixels;
    private Integer requestVideoHeightPixels;
    private String errorCode;
    private String error;
    private String errorText;
    private Long timestamp;

    public PlayerNetworkRequest(String requestId, String sessionId, String userId, String propertyId, String playerInstanceId,
                                Long requestStart, Long requestResponseStart, Long requestResponseEnd, String requestType,
                                String requestHostName, Integer requestByteLoaded, String requestResponseHeaders,
                                Integer requestMediaDurationMillis, Integer requestVideoWidthPixels,
                                Integer requestVideoHeightPixels, String errorCode, String error, String errorText, Long timestamp) {
        this.requestId = requestId;
        this.sessionId = sessionId;
        this.userId = userId;
        this.propertyId = propertyId;
        this.playerInstanceId = playerInstanceId;
        this.requestStart = requestStart;
        this.requestResponseStart = requestResponseStart;
        this.requestResponseEnd = requestResponseEnd;
        this.requestType = requestType;
        this.requestHostName = requestHostName;
        this.requestByteLoaded = requestByteLoaded;
        this.requestResponseHeaders = requestResponseHeaders;
        this.requestMediaDurationMillis = requestMediaDurationMillis;
        this.requestVideoWidthPixels = requestVideoWidthPixels;
        this.requestVideoHeightPixels = requestVideoHeightPixels;
        this.errorCode = errorCode;
        this.error = error;
        this.errorText = errorText;
        this.timestamp = timestamp;
    }

    public PlayerNetworkRequest() {

        this.requestId = Util.getRandomCharacterString();
        this.sessionId = Util.getRandomCharacterString();
        this.userId = Util.getRandomCharacterString();
        this.propertyId = "";
        this.playerInstanceId = Util.getRandomCharacterString();
        this.requestStart = 0L;
        this.requestResponseStart = 0L;
        this.requestResponseEnd = 0L;
        this.requestType = "Player Event";
        this.requestHostName = "Not Known";
        this.requestByteLoaded = 0;
        this.requestResponseHeaders = "";
        this.requestMediaDurationMillis = 0;
        this.requestVideoWidthPixels = 1280;
        this.requestVideoHeightPixels = 720;
        this.errorCode = "";
        this.error = "";
        this.errorText = "";
        this.timestamp = 0L;
    }

    public PlayerNetworkRequest(Context context) {

        this.requestId = Util.getRandomCharacterString();
        this.sessionId = Util.getRandomCharacterString();
        this.userId = NetworkUtil.getDeviceId(context);
        this.propertyId = "";
        this.playerInstanceId = Util.getRandomCharacterString();
        this.requestStart = 0L;
        this.requestResponseStart = 0L;
        this.requestResponseEnd = 0L;
        this.requestType = "Player Event";
        this.requestHostName = "Not Known";
        this.requestByteLoaded = 0;
        this.requestResponseHeaders = "";
        this.requestMediaDurationMillis = 0;
        this.requestVideoWidthPixels = 1280;
        this.requestVideoHeightPixels = 720;
        this.errorCode = "";
        this.error = "";
        this.errorText = "";
        this.timestamp = 0L;
    }

    protected PlayerNetworkRequest(Parcel in) {
        requestId = in.readString();
        sessionId = in.readString();
        userId = in.readString();
        propertyId = in.readString();
        playerInstanceId = in.readString();
        if (in.readByte() == 0) {
            requestStart = null;
        } else {
            requestStart = in.readLong();
        }
        if (in.readByte() == 0) {
            requestResponseStart = null;
        } else {
            requestResponseStart = in.readLong();
        }
        if (in.readByte() == 0) {
            requestResponseEnd = null;
        } else {
            requestResponseEnd = in.readLong();
        }
        requestType = in.readString();
        requestHostName = in.readString();
        if (in.readByte() == 0) {
            requestByteLoaded = null;
        } else {
            requestByteLoaded = in.readInt();
        }
        requestResponseHeaders = in.readString();
        if (in.readByte() == 0) {
            requestMediaDurationMillis = null;
        } else {
            requestMediaDurationMillis = in.readInt();
        }
        if (in.readByte() == 0) {
            requestVideoWidthPixels = null;
        } else {
            requestVideoWidthPixels = in.readInt();
        }
        if (in.readByte() == 0) {
            requestVideoHeightPixels = null;
        } else {
            requestVideoHeightPixels = in.readInt();
        }
        errorCode = in.readString();
        error = in.readString();
        errorText = in.readString();
        if (in.readByte() == 0) {
            timestamp = null;
        } else {
            timestamp = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(requestId);
        dest.writeString(sessionId);
        dest.writeString(userId);
        dest.writeString(propertyId);
        dest.writeString(playerInstanceId);
        if (requestStart == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(requestStart);
        }
        if (requestResponseStart == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(requestResponseStart);
        }
        if (requestResponseEnd == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(requestResponseEnd);
        }
        dest.writeString(requestType);
        dest.writeString(requestHostName);
        if (requestByteLoaded == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(requestByteLoaded);
        }
        dest.writeString(requestResponseHeaders);
        if (requestMediaDurationMillis == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(requestMediaDurationMillis);
        }
        if (requestVideoWidthPixels == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(requestVideoWidthPixels);
        }
        if (requestVideoHeightPixels == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(requestVideoHeightPixels);
        }
        dest.writeString(errorCode);
        dest.writeString(error);
        dest.writeString(errorText);
        if (timestamp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(timestamp);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlayerNetworkRequest> CREATOR = new Creator<PlayerNetworkRequest>() {
        @Override
        public PlayerNetworkRequest createFromParcel(Parcel in) {
            return new PlayerNetworkRequest(in);
        }

        @Override
        public PlayerNetworkRequest[] newArray(int size) {
            return new PlayerNetworkRequest[size];
        }
    };

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getPlayerInstanceId() {
        return playerInstanceId;
    }

    public void setPlayerInstanceId(String playerInstanceId) {
        this.playerInstanceId = playerInstanceId;
    }

    public Long getRequestStart() {
        return requestStart;
    }

    public void setRequestStart(Long requestStart) {
        this.requestStart = requestStart;
    }

    public Long getRequestResponseStart() {
        return requestResponseStart;
    }

    public void setRequestResponseStart(Long requestResponseStart) {
        this.requestResponseStart = requestResponseStart;
    }

    public Long getRequestResponseEnd() {
        return requestResponseEnd;
    }

    public void setRequestResponseEnd(Long requestResponseEnd) {
        this.requestResponseEnd = requestResponseEnd;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestHostName() {
        return requestHostName;
    }

    public void setRequestHostName(String requestHostName) {
        this.requestHostName = requestHostName;
    }

    public Integer getRequestByteLoaded() {
        return requestByteLoaded;
    }

    public void setRequestByteLoaded(Integer requestByteLoaded) {
        this.requestByteLoaded = requestByteLoaded;
    }

    public String getRequestResponseHeaders() {
        return requestResponseHeaders;
    }

    public void setRequestResponseHeaders(String requestResponseHeaders) {
        this.requestResponseHeaders = requestResponseHeaders;
    }

    public Integer getRequestMediaDurationMillis() {
        return requestMediaDurationMillis;
    }

    public void setRequestMediaDurationMillis(Integer requestMediaDurationMillis) {
        this.requestMediaDurationMillis = requestMediaDurationMillis;
    }

    public Integer getRequestVideoWidthPixels() {
        return requestVideoWidthPixels;
    }

    public void setRequestVideoWidthPixels(Integer requestVideoWidthPixels) {
        this.requestVideoWidthPixels = requestVideoWidthPixels;
    }

    public Integer getRequestVideoHeightPixels() {
        return requestVideoHeightPixels;
    }

    public void setRequestVideoHeightPixels(Integer requestVideoHeightPixels) {
        this.requestVideoHeightPixels = requestVideoHeightPixels;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PlayerNetworkRequest{" +
                "requestId='" + requestId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", userId='" + userId + '\'' +
                ", propertyId='" + propertyId + '\'' +
                ", playerInstanceId='" + playerInstanceId + '\'' +
                ", requestStart=" + requestStart +
                ", requestResponseStart=" + requestResponseStart +
                ", requestResponseEnd=" + requestResponseEnd +
                ", requestType='" + requestType + '\'' +
                ", requestHostName='" + requestHostName + '\'' +
                ", requestByteLoaded=" + requestByteLoaded +
                ", requestResponseHeaders='" + requestResponseHeaders + '\'' +
                ", requestMediaDurationMillis=" + requestMediaDurationMillis +
                ", requestVideoWidthPixels=" + requestVideoWidthPixels +
                ", requestVideoHeightPixels=" + requestVideoHeightPixels +
                ", errorCode='" + errorCode + '\'' +
                ", error='" + error + '\'' +
                ", errorText='" + errorText + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
