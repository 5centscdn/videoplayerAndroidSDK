package com.fivecentscdnanalytics.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.fivecentscdnanalytics.utils.Util;

import java.util.Calendar;

public class PlayerInstance implements Parcelable {

    public static final String KEY_PLAYER_INSTANCE_ID = "player_instance_id";
    public static final String KEY_PROPERTY_ID = "property_id";
    public static final String KEY_SESSION_ID = "session_id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_CUSTOM_USER_ID = "custom_user_id";
    public static final String KEY_PLAYER_HEIGHT_PIXEL = "player_height_pixels";
    public static final String KEY_PLAYER_WIDTH_PIXEL = "player_width_pixels";
    public static final String KEY_META_PAGE_TYPE = "meta_page_type";
    public static final String KEY_META_PAGE_URL = "meta_page_url";
    public static final String KEY_PLAYER_SOFTWARE = "player_software";
    public static final String KEY_PLAYER_LANGUAGE_CODE = "player_language_code";
    public static final String KEY_PLAYER_NAME = "player_name";
    public static final String KEY_ERROR = "error";
    public static final String KEY_ERROR_CODE = "error_code";
    public static final String KEY_ERROR_TEXT = "error_text";
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
    public static final String KEY_PLAYER_INTEGRATION_VERSION = "player_integration_version";
    public static final String KEY_PLAYER_SOFTWARE_VERSION = "player_software_version";
    public static final String KEY_PLAYER_PRELOAD = "player_preload";
    public static final String KEY_PLAYER_AUTOPLAY = "player_autoplay";
    public static final String KEY_TIMESTAMP = "z";

    private String playerInstanceId;
    private String propertyId;
    private String sessionId;
    private String userId;
    private String customUserId;
    private Integer playerHeightPixels;
    private Integer playerWidthPixels;
    private String metaPageType;
    private String metaPageUrl;
    private String playerSoftware;
    private String playerLanguageCode;
    private String playerName;
    private String error;
    private String errorCode;
    private String errorText;
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
    private String playerIntegrationVersion;
    private String playerSoftwareVersion;
    private Boolean playerPreload;
    private Boolean playerAutoPlay;
    private Long timestamp;


    private static PlayerInstance playerInstance;

    public static PlayerInstance getInstance(){

        if(playerInstance == null){
            playerInstance = new PlayerInstance();
        }

        return playerInstance;
    }

    private PlayerInstance() {
        this.playerInstanceId = Util.getRandomCharacterString();
        this.propertyId = ViewerSession.getInstance().getPropertyId();
        this.sessionId = ViewerSession.getInstance().getSessionId();
        this.userId = ViewerSession.getInstance().getUserId();
        this.customUserId = ViewerSession.getInstance().getCustomUserId();
        this.playerHeightPixels = 1260;
        this.playerWidthPixels = 720;
        this.metaPageType = "";
        this.metaPageUrl = "";
        this.playerSoftware = "Custom";
        this.playerLanguageCode = "en";
        this.playerName = "Custom";
        this.playerIntegrationVersion = "1.0";
        this.playerSoftwareVersion = "1.0.0";
        this.playerPreload = false;
        this.playerAutoPlay = false;
        this.timestamp = Calendar.getInstance().getTimeInMillis();

    }

    private PlayerInstance(Context context) {

        this.playerInstanceId = Util.getRandomCharacterString();
        this.propertyId = ViewerSession.getInstance().getPropertyId();
        this.sessionId = ViewerSession.getInstance().getSessionId();
        this.userId = ViewerSession.getInstance().getUserId();
        this.customUserId = ViewerSession.getInstance().getCustomUserId();
        this.playerHeightPixels = 1260;
        this.playerWidthPixels = 720;
        this.metaPageType = "";
        this.metaPageUrl = "";
        this.playerSoftware = "Custom";
        this.playerLanguageCode = "en";
        this.playerName = "Custom";
        this.error = "";
        this.errorCode = "";
        this.errorText = "";
        this.customData1 = "";
        this.customData2 = "";
        this.customData3 = "";
        this.customData4 = "";
        this.customData5 = "";
        this.customData6 = "";
        this.customData7 = "";
        this.customData8 = "";
        this.customData9 = "";
        this.customData10 = "";
        this.playerIntegrationVersion = "1.0";
        this.playerSoftwareVersion = "1.0.0";
        this.playerPreload = true;
        this.playerAutoPlay = true;
        this.timestamp = 0L;
    }

    protected PlayerInstance(Parcel in) {
        playerInstanceId = in.readString();
        propertyId = in.readString();
        sessionId = in.readString();
        userId = in.readString();
        customUserId = in.readString();
        if (in.readByte() == 0) {
            playerHeightPixels = null;
        } else {
            playerHeightPixels = in.readInt();
        }
        if (in.readByte() == 0) {
            playerWidthPixels = null;
        } else {
            playerWidthPixels = in.readInt();
        }
        metaPageType = in.readString();
        metaPageUrl = in.readString();
        playerSoftware = in.readString();
        playerLanguageCode = in.readString();
        playerName = in.readString();
        error = in.readString();
        errorCode = in.readString();
        errorText = in.readString();
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
        playerIntegrationVersion = in.readString();
        playerSoftwareVersion = in.readString();
        byte tmpPlayerPreload = in.readByte();
        playerPreload = tmpPlayerPreload == 0 ? null : tmpPlayerPreload == 1;
        byte tmpPlayerAutoPlay = in.readByte();
        playerAutoPlay = tmpPlayerAutoPlay == 0 ? null : tmpPlayerAutoPlay == 1;
    }

    public static final Creator<PlayerInstance> CREATOR = new Creator<PlayerInstance>() {
        @Override
        public PlayerInstance createFromParcel(Parcel in) {
            return new PlayerInstance(in);
        }

        @Override
        public PlayerInstance[] newArray(int size) {
            return new PlayerInstance[size];
        }
    };

    public String getPlayerInstanceId() {
        return playerInstanceId;
    }

    public void setPlayerInstanceId(String playerInstanceId) {
        this.playerInstanceId = playerInstanceId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
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

    public String getCustomUserId() {
        return customUserId;
    }

    public void setCustomUserId(String customUserId) {
        this.customUserId = customUserId;
    }

    public Integer getPlayerHeightPixels() {
        return playerHeightPixels;
    }

    public void setPlayerHeightPixels(Integer playerHeightPixels) {
        this.playerHeightPixels = playerHeightPixels;
    }

    public Integer getPlayerWidthPixels() {
        return playerWidthPixels;
    }

    public void setPlayerWidthPixels(Integer playerWidthPixels) {
        this.playerWidthPixels = playerWidthPixels;
    }

    public String getMetaPageType() {
        return metaPageType;
    }

    public void setMetaPageType(String metaPageType) {
        this.metaPageType = metaPageType;
    }

    public String getMetaPageUrl() {
        return metaPageUrl;
    }

    public void setMetaPageUrl(String metaPageUrl) {
        this.metaPageUrl = metaPageUrl;
    }

    public String getPlayerSoftware() {
        return playerSoftware;
    }

    public void setPlayerSoftware(String playerSoftware) {
        this.playerSoftware = playerSoftware;
    }

    public String getPlayerLanguageCode() {
        return playerLanguageCode;
    }

    public void setPlayerLanguageCode(String playerLanguageCode) {
        this.playerLanguageCode = playerLanguageCode;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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

    public String getPlayerIntegrationVersion() {
        return playerIntegrationVersion;
    }

    public void setPlayerIntegrationVersion(String playerIntegrationVersion) {
        this.playerIntegrationVersion = playerIntegrationVersion;
    }

    public String getPlayerSoftwareVersion() {
        return playerSoftwareVersion;
    }

    public void setPlayerSoftwareVersion(String playerSoftwareVersion) {
        this.playerSoftwareVersion = playerSoftwareVersion;
    }

    public Boolean getPlayerPreload() {
        return playerPreload;
    }

    public void setPlayerPreload(Boolean playerPreload) {
        this.playerPreload = playerPreload;
    }

    public Boolean getPlayerAutoPlay() {
        return playerAutoPlay;
    }

    public void setPlayerAutoPlay(Boolean playerAutoPlay) {
        this.playerAutoPlay = playerAutoPlay;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PlayerInstance{" +
                "playerInstanceId='" + playerInstanceId + '\'' +
                ", propertyId='" + propertyId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", userId='" + userId + '\'' +
                ", customUserId='" + customUserId + '\'' +
                ", playerHeightPixels=" + playerHeightPixels +
                ", playerWidthPixels=" + playerWidthPixels +
                ", metaPageType='" + metaPageType + '\'' +
                ", metaPageUrl='" + metaPageUrl + '\'' +
                ", playerSoftware='" + playerSoftware + '\'' +
                ", playerLanguageCode='" + playerLanguageCode + '\'' +
                ", playerName='" + playerName + '\'' +
                ", error='" + error + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", errorText='" + errorText + '\'' +
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
                ", playerIntegrationVersion='" + playerIntegrationVersion + '\'' +
                ", playerSoftwareVersion='" + playerSoftwareVersion + '\'' +
                ", playerPreload=" + playerPreload +
                ", playerAutoPlay=" + playerAutoPlay +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(playerInstanceId);
        parcel.writeString(propertyId);
        parcel.writeString(sessionId);
        parcel.writeString(userId);
        parcel.writeString(customUserId);
        if (playerHeightPixels == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(playerHeightPixels);
        }
        if (playerWidthPixels == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(playerWidthPixels);
        }
        parcel.writeString(metaPageType);
        parcel.writeString(metaPageUrl);
        parcel.writeString(playerSoftware);
        parcel.writeString(playerLanguageCode);
        parcel.writeString(playerName);
        parcel.writeString(error);
        parcel.writeString(errorCode);
        parcel.writeString(errorText);
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
        parcel.writeString(playerIntegrationVersion);
        parcel.writeString(playerSoftwareVersion);
        parcel.writeByte((byte) (playerPreload == null ? 0 : playerPreload ? 1 : 2));
        parcel.writeByte((byte) (playerAutoPlay == null ? 0 : playerAutoPlay ? 1 : 2));
    }

    public static void setPlayerInstance(PlayerInstance playerInstance) {
        PlayerInstance.playerInstance = playerInstance;
    }
}
