package com.fivecentscdnanalytics.data;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.fivecentscdnanalytics.calls.api.ApiConfig;
import com.fivecentscdnanalytics.utils.NetworkUtil;
import com.fivecentscdnanalytics.utils.Util;


public class ViewerSession implements Parcelable {


    public static final String HASH_ID = "hash_id";
    public static final String TITLE = "title";
    public static final String TAGS = "tags";
    public static final String URL = "url";
    public static final String VIEWER_ID = "viewer_id";
    public static final String VIEW_ID = "view_id";
    public static final String USER_AGENT = "user_agent";
    public static final String TYPE = "type";
    public static final String REFERRER = "referer";
    public static final String VALUE1 = "value1";
    public static final String VALUE2 = "value2";


    public static final String KEY_EVENT_FAMILY = "event_family";
    public static final String KEY_SESSION_ID = "session_id";
    public static final String KEY_PROPERTY_ID = "property_id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_CUSTOM_USER_ID = "custom_user_id";
    public static final String KEY_VIEWER_CLIENT_VERSION = "viewer_client_version";
    public static final String KEY_META_ASN = "meta_asn";
    public static final String KEY_META_IP_ADDRESS = "meta_ip_address";
    public static final String KEY_META_BROWSER = "meta_browser";
    public static final String KEY_META_BROWSER_VERSION = "meta_browser_version";
    public static final String KEY_META_USER_AGENT = "meta_user_agent";
    public static final String KEY_META_DEVICE_DISPLAY_WIDTH = "meta_device_display_width";
    public static final String KEY_META_DEVICE_DISPLAY_HEIGHT = "meta_device_display_height";
    public static final String KEY_META_DEVICE_DISPLAY_DPR = "meta_device_display_dpr";
    public static final String KEY_META_DEVICE_IS_TOUCHSCREEN = "meta_device_is_touchscreen";
    public static final String KEY_META_OPERATING_SYSTEM = "meta_operating_system";
    public static final String KEY_META_OPERATING_SYSTEM_VERSION = "meta_operating_system_version";
    public static final String KEY_META_DEVICE_CATEGORY = "meta_device_category";
    public static final String KEY_META_CONNECTION_TYPE = "meta_connection_type";
    public static final String KEY_META_CONNECTION_SPEED = "meta_connection_speed";
    public static final String KEY_META_DEVICE_MANUFACTURER = "meta_device_manufacturer";
    public static final String KEY_META_DEVICE_NAME = "meta_device_name";
    public static final String KEY_META_CITY = "meta_city";
    public static final String KEY_META_COUNTRY_CODE = "meta_country_code";
    public static final String KEY_META_COUNTRY = "meta_country";
    public static final String KEY_META_CONTINENT_CODE = "meta_continent_code";
    public static final String KEY_META_REGION = "meta_region";
    public static final String KEY_META_LATITUDE = "meta_latitude";
    public static final String KEY_META_LONGITUDE = "meta_longitude";
    public static final String KEY_META_DEVICE_ARCHITECTURE = "meta_device_architecture";
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
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_PHONE = "user_phone";
    public static final String KEY_USER_PROFILE_IMAGE = "user_profile_image";
    public static final String KEY_USER_ADDRESS_LINE1 = "user_address_line1";
    public static final String KEY_USER_ADDRESS_LINE2 = "user_address_line2";
    public static final String KEY_USER_CITY = "user_city";
    public static final String KEY_USER_STATE = "user_state";
    public static final String KEY_USER_COUNTRY = "user_country";
    public static final String KEY_USER_ZIPCODE = "user_zipcode";
    public static final String KEY_TIMESTAMP = "z";


    private String sessionId;
    private String propertyId;
    private String userId;
    private String customUserId;
    private String viewerClientVersion;
    private String metaAsn;
    private String metaIpAddress;
    private String metaBrowser;
    private String metaBrowserVersion;
    private String metaUserAgent;
    private Integer metaDeviceDisplayWidth;
    private Integer metaDeviceDisplayHeight;
    private Integer metaDeviceDisplayDpr;
    private Boolean metaDeviceIsTouchScreen;
    private String metaOperatingSystem;
    private String metaOperatingSystemVersion;
    private String metaDeviceCategory;
    private String metaConnectionType;
    private String metaConnectionSpeed;
    private String metaDeviceManufacturer;
    private String metaDeviceName;
    private String metaCity;
    private String metaCountryCode;
    private String metaCountry;
    private String metaContinentCode;
    private String metaRegion;
    private String metaLatitude;
    private String metaLongitude;
    private String metaDeviceArchitecture;
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
    private String errorCode;
    private String error;
    private String errorText;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String userProfileImage;
    private String userAddressLine1;
    private String userAddressLine2;
    private String userCity;
    private String userState;
    private String userCountry;
    private String userZipcode;
    private Long z;


    private static ViewerSession viewerSession;

    public static ViewerSession getInstance(){

        if(viewerSession == null){
            viewerSession = new ViewerSession();
        }
        return viewerSession;
    }

    public static void setInstance(ViewerSession session){
        viewerSession = session;
    }

    public ViewerSession() {
        this.sessionId = ApiConfig.SESSION_ID;
        this.propertyId = "";
        this.viewerClientVersion = ApiConfig.CLIENT_VERSION;
        this.metaIpAddress = NetworkUtil.getIPAddress(true);
        this.metaUserAgent = NetworkUtil.getDeviceName();
        this.metaDeviceArchitecture = System.getProperty("os.arch");;
        this.metaDeviceDisplayDpr = Util.getDeviceDensity();

    }

    private ViewerSession(Context context) {

        this.sessionId = ApiConfig.SESSION_ID;
        this.propertyId = "";
        this.userId = NetworkUtil.getDeviceId(context);
        this.customUserId = Util.getRandomCharacterString();
        this.viewerClientVersion = ApiConfig.CLIENT_VERSION;
        this.metaAsn = "";
        this.metaIpAddress = NetworkUtil.getIPAddress(true);
        this.metaBrowser = "";
        this.metaBrowserVersion = "";
        this.metaUserAgent = NetworkUtil.getDeviceName();
        this.metaDeviceDisplayWidth = context.getResources().getDisplayMetrics().widthPixels;
        this.metaDeviceDisplayHeight = context.getResources().getDisplayMetrics().heightPixels;
        this.metaDeviceDisplayDpr = Util.getDeviceDensity();
        this.metaDeviceIsTouchScreen = true;
        this.metaOperatingSystem = "ANDROID : "+Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName();
        this.metaOperatingSystemVersion = String.valueOf(Build.VERSION.SDK_INT);
        this.metaDeviceCategory = ApiConfig.DEVICE_CATEGORY;

        if(NetworkUtil.getConnectionStatus(context) == NetworkUtil.WIFI) {
            this.metaConnectionType = "WiFi";
        }else if(NetworkUtil.getConnectionStatus(context) == NetworkUtil.MOBILE){
            this.metaConnectionType = "Mobile";
        }else if(NetworkUtil.getConnectionStatus(context) == NetworkUtil.NOT_CONNECTED){
            this.metaConnectionType = "No Network";
        }
        this.metaConnectionSpeed = NetworkUtil.networkConnectionSpeed(context);
        this.metaDeviceManufacturer = Build.MANUFACTURER;
        this.metaDeviceName = Build.DEVICE;
        this.metaCity = "";
        this.metaCountryCode = "";
        this.metaCountry = "";
        this.metaContinentCode = "";
        this.metaRegion = "";
        this.metaLatitude = "";
        this.metaLongitude = "";
        this.metaDeviceArchitecture = System.getProperty("os.arch");;
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
        this.errorCode = "";
        this.error = "";
        this.errorText = "";
        this.userName = "";
        this.userPhone = "";
        this.userEmail = "";
        this.userProfileImage = "";
        this.userAddressLine1 = "";
        this.userAddressLine2 = "";
        this.userCity = "";
        this.userState = "";
        this.userCountry = "";
        this.userZipcode = "";
        this.z = 0L;
    }

    protected ViewerSession(Parcel in) {
        sessionId = in.readString();
        propertyId = in.readString();
        userId = in.readString();
        customUserId = in.readString();
        viewerClientVersion = in.readString();
        metaAsn = in.readString();
        metaIpAddress = in.readString();
        metaBrowser = in.readString();
        metaBrowserVersion = in.readString();
        metaUserAgent = in.readString();
        if (in.readByte() == 0) {
            metaDeviceDisplayWidth = null;
        } else {
            metaDeviceDisplayWidth = in.readInt();
        }
        if (in.readByte() == 0) {
            metaDeviceDisplayHeight = null;
        } else {
            metaDeviceDisplayHeight = in.readInt();
        }
        if (in.readByte() == 0) {
            metaDeviceDisplayDpr = null;
        } else {
            metaDeviceDisplayDpr = in.readInt();
        }
        byte tmpMetaDeviceIsTouchScreen = in.readByte();
        metaDeviceIsTouchScreen = tmpMetaDeviceIsTouchScreen == 0 ? null : tmpMetaDeviceIsTouchScreen == 1;
        metaOperatingSystem = in.readString();
        metaOperatingSystemVersion = in.readString();
        metaDeviceCategory = in.readString();
        metaConnectionType = in.readString();
        metaConnectionSpeed = in.readString();
        metaDeviceManufacturer = in.readString();
        metaDeviceName = in.readString();
        metaCity = in.readString();
        metaCountryCode = in.readString();
        metaCountry = in.readString();
        metaContinentCode = in.readString();
        metaRegion = in.readString();
        metaLatitude = in.readString();
        metaLongitude = in.readString();
        metaDeviceArchitecture = in.readString();
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
        errorCode = in.readString();
        error = in.readString();
        errorText = in.readString();
        userName = in.readString();
        userPhone = in.readString();
        userEmail = in.readString();
        userProfileImage = in.readString();
        userAddressLine1 = in.readString();
        userAddressLine2 = in.readString();
        userCity = in.readString();
        userState = in.readString();
        userCountry = in.readString();
        userZipcode = in.readString();
        if (in.readByte() == 0) {
            z = null;
        } else {
            z = in.readLong();
        }
    }

    public static final Creator<ViewerSession> CREATOR = new Creator<ViewerSession>() {
        @Override
        public ViewerSession createFromParcel(Parcel in) {
            return new ViewerSession(in);
        }

        @Override
        public ViewerSession[] newArray(int size) {
            return new ViewerSession[size];
        }
    };

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

    public String getCustomUserId() {
        return customUserId;
    }

    public void setCustomUserId(String customUserId) {
        this.customUserId = customUserId;
    }

    public String getViewerClientVersion() {
        return viewerClientVersion;
    }

    public void setViewerClientVersion(String viewerClientVersion) {
        this.viewerClientVersion = viewerClientVersion;
    }

    public String getMetaAsn() {
        return metaAsn;
    }

    public void setMetaAsn(String metaAsn) {
        this.metaAsn = metaAsn;
    }

    public String getMetaIpAddress() {
        return metaIpAddress;
    }

    public void setMetaIpAddress(String metaIpAddress) {
        this.metaIpAddress = metaIpAddress;
    }

    public String getMetaBrowser() {
        return metaBrowser;
    }

    public void setMetaBrowser(String metaBrowser) {
        this.metaBrowser = metaBrowser;
    }

    public String getMetaBrowserVersion() {
        return metaBrowserVersion;
    }

    public void setMetaBrowserVersion(String metaBrowserVersion) {
        this.metaBrowserVersion = metaBrowserVersion;
    }

    public String getMetaUserAgent() {
        return metaUserAgent;
    }

    public void setMetaUserAgent(String metaUserAgent) {
        this.metaUserAgent = metaUserAgent;
    }

    public Integer getMetaDeviceDisplayWidth() {
        return metaDeviceDisplayWidth;
    }

    public void setMetaDeviceDisplayWidth(Integer metaDeviceDisplayWidth) {
        this.metaDeviceDisplayWidth = metaDeviceDisplayWidth;
    }

    public Integer getMetaDeviceDisplayHeight() {
        return metaDeviceDisplayHeight;
    }

    public void setMetaDeviceDisplayHeight(Integer metaDeviceDisplayHeight) {
        this.metaDeviceDisplayHeight = metaDeviceDisplayHeight;
    }

    public Integer getMetaDeviceDisplayDpr() {
        return metaDeviceDisplayDpr;
    }

    public void setMetaDeviceDisplayDpr(Integer metaDeviceDisplayDpr) {
        this.metaDeviceDisplayDpr = metaDeviceDisplayDpr;
    }

    public Boolean getMetaDeviceIsTouchScreen() {
        return metaDeviceIsTouchScreen;
    }

    public void setMetaDeviceIsTouchScreen(Boolean metaDeviceIsTouchScreen) {
        this.metaDeviceIsTouchScreen = metaDeviceIsTouchScreen;
    }

    public String getMetaOperatingSystem() {
        return metaOperatingSystem;
    }

    public void setMetaOperatingSystem(String metaOperatingSystem) {
        this.metaOperatingSystem = metaOperatingSystem;
    }

    public String getMetaOperatingSystemVersion() {
        return metaOperatingSystemVersion;
    }

    public void setMetaOperatingSystemVersion(String metaOperatingSystemVersion) {
        this.metaOperatingSystemVersion = metaOperatingSystemVersion;
    }

    public String getMetaDeviceCategory() {
        return metaDeviceCategory;
    }

    public void setMetaDeviceCategory(String metaDeviceCategory) {
        this.metaDeviceCategory = metaDeviceCategory;
    }

    public String getMetaConnectionType() {
        return metaConnectionType;
    }

    public void setMetaConnectionType(String metaConnectionType) {
        this.metaConnectionType = metaConnectionType;
    }

    public String getMetaConnectionSpeed() {
        return metaConnectionSpeed;
    }

    public void setMetaConnectionSpeed(String metaConnectionSpeed) {
        this.metaConnectionSpeed = metaConnectionSpeed;
    }

    public String getMetaDeviceManufacturer() {
        return metaDeviceManufacturer;
    }

    public String getMetaDeviceName() {
        return metaDeviceName;
    }

    public void setMetaDeviceName(String metaDeviceName) {
        this.metaDeviceName = metaDeviceName;
    }

    public void setMetaDeviceManufacturer(String metaDeviceManufacturer) {
        this.metaDeviceManufacturer = metaDeviceManufacturer;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getUserAddressLine1() {
        return userAddressLine1;
    }

    public void setUserAddressLine1(String userAddressLine1) {
        this.userAddressLine1 = userAddressLine1;
    }

    public String getUserAddressLine2() {
        return userAddressLine2;
    }

    public void setUserAddressLine2(String userAddressLine2) {
        this.userAddressLine2 = userAddressLine2;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserZipcode() {
        return userZipcode;
    }

    public void setUserZipcode(String userZipcode) {
        this.userZipcode = userZipcode;
    }

    public Long getZ() {
        return z;
    }

    public void setZ(Long z) {
        this.z = z;
    }

    public String getMetaCity() {
        return metaCity;
    }

    public void setMetaCity(String metaCity) {
        this.metaCity = metaCity;
    }

    public String getMetaCountryCode() {
        return metaCountryCode;
    }

    public void setMetaCountryCode(String metaCountryCode) {
        this.metaCountryCode = metaCountryCode;
    }

    public String getMetaCountry() {
        return metaCountry;
    }

    public void setMetaCountry(String metaCountry) {
        this.metaCountry = metaCountry;
    }

    public String getMetaContinentCode() {
        return metaContinentCode;
    }

    public void setMetaContinentCode(String metaContinentCode) {
        this.metaContinentCode = metaContinentCode;
    }

    public String getMetaRegion() {
        return metaRegion;
    }

    public void setMetaRegion(String metaRegion) {
        this.metaRegion = metaRegion;
    }

    public String getMetaLatitude() {
        return metaLatitude;
    }

    public void setMetaLatitude(String metaLatitude) {
        this.metaLatitude = metaLatitude;
    }

    public String getMetaLongitude() {
        return metaLongitude;
    }

    public void setMetaLongitude(String metaLongitude) {
        this.metaLongitude = metaLongitude;
    }

    public String getMetaDeviceArchitecture() {
        return metaDeviceArchitecture;
    }

    public void setMetaDeviceArchitecture(String metaDeviceArchitecture) {
        this.metaDeviceArchitecture = metaDeviceArchitecture;
    }

    @Override
    public String toString() {
        return "ViewerSession{" +
                "sessionId='" + sessionId + '\'' +
                ", propertyId='" + propertyId + '\'' +
                ", userId='" + userId + '\'' +
                ", customUserId='" + customUserId + '\'' +
                ", viewerClientVersion='" + viewerClientVersion + '\'' +
                ", metaAsn='" + metaAsn + '\'' +
                ", metaIpAddress='" + metaIpAddress + '\'' +
                ", metaBrowser='" + metaBrowser + '\'' +
                ", metaBrowserVersion='" + metaBrowserVersion + '\'' +
                ", metaUserAgent='" + metaUserAgent + '\'' +
                ", metaDeviceDisplayWidth=" + metaDeviceDisplayWidth +
                ", metaDeviceDisplayHeight=" + metaDeviceDisplayHeight +
                ", metaDeviceDisplayDpr=" + metaDeviceDisplayDpr +
                ", metaDeviceIsTouchScreen=" + metaDeviceIsTouchScreen +
                ", metaOperatingSystem='" + metaOperatingSystem + '\'' +
                ", metaOperatingSystemVersion='" + metaOperatingSystemVersion + '\'' +
                ", metaDeviceCategory='" + metaDeviceCategory + '\'' +
                ", metaConnectionType='" + metaConnectionType + '\'' +
                ", metaConnectionSpeed='" + metaConnectionSpeed + '\'' +
                ", metaDeviceManufacturer='" + metaDeviceManufacturer + '\'' +
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
                ", errorCode='" + errorCode + '\'' +
                ", error='" + error + '\'' +
                ", errorText='" + errorText + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userProfileImage='" + userProfileImage + '\'' +
                ", userAddressLine1='" + userAddressLine1 + '\'' +
                ", userAddressLine2='" + userAddressLine2 + '\'' +
                ", userCity='" + userCity + '\'' +
                ", userState='" + userState + '\'' +
                ", userCountry='" + userCountry + '\'' +
                ", userZipcode='" + userZipcode + '\'' +
                ", z=" + z +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sessionId);
        parcel.writeString(propertyId);
        parcel.writeString(userId);
        parcel.writeString(customUserId);
        parcel.writeString(viewerClientVersion);
        parcel.writeString(metaAsn);
        parcel.writeString(metaIpAddress);
        parcel.writeString(metaBrowser);
        parcel.writeString(metaBrowserVersion);
        parcel.writeString(metaUserAgent);
        if (metaDeviceDisplayWidth == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(metaDeviceDisplayWidth);
        }
        if (metaDeviceDisplayHeight == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(metaDeviceDisplayHeight);
        }
        if (metaDeviceDisplayDpr == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(metaDeviceDisplayDpr);
        }
        parcel.writeByte((byte) (metaDeviceIsTouchScreen == null ? 0 : metaDeviceIsTouchScreen ? 1 : 2));
        parcel.writeString(metaOperatingSystem);
        parcel.writeString(metaOperatingSystemVersion);
        parcel.writeString(metaDeviceCategory);
        parcel.writeString(metaConnectionType);
        parcel.writeString(metaConnectionSpeed);
        parcel.writeString(metaDeviceManufacturer);
        parcel.writeString(metaDeviceName);
        parcel.writeString(metaCity);
        parcel.writeString(metaCountryCode);
        parcel.writeString(metaCountry);
        parcel.writeString(metaContinentCode);
        parcel.writeString(metaRegion);
        parcel.writeString(metaLatitude);
        parcel.writeString(metaLongitude);
        parcel.writeString(metaDeviceArchitecture);
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
        parcel.writeString(errorCode);
        parcel.writeString(error);
        parcel.writeString(errorText);
        parcel.writeString(userName);
        parcel.writeString(userPhone);
        parcel.writeString(userEmail);
        parcel.writeString(userProfileImage);
        parcel.writeString(userAddressLine1);
        parcel.writeString(userAddressLine2);
        parcel.writeString(userCity);
        parcel.writeString(userState);
        parcel.writeString(userCountry);
        parcel.writeString(userZipcode);
        if (z == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(z);
        }
    }

    public static void setViewerSession(ViewerSession viewerSession) {
        ViewerSession.viewerSession = viewerSession;
    }
}
