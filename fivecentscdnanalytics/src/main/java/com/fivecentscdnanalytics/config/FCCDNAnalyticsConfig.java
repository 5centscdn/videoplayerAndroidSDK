package com.fivecentscdnanalytics.config;

import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.TextView;

import com.fivecentscdnanalytics.data.FCCDNRequest;
import com.fivecentscdnanalytics.data.FiveCentsData;
import com.fivecentscdnanalytics.data.PlayerData;
import com.fivecentscdnanalytics.data.UserData;
import com.fivecentscdnanalytics.data.VideoMetadata;
import com.fivecentscdnanalytics.enums.CDNProvider;
import com.fivecentscdnanalytics.enums.PlayerType;
import com.fivecentscdnanalytics.listener.DataTransferListener;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import org.jetbrains.annotations.NotNull;


public class FCCDNAnalyticsConfig implements Parcelable {
    private String cdnProvider = CDNProvider.FIVECENTSCDN;

    private PlayerData playerData;
    StyledPlayerView playerView;
    int showCV;
    private UserData userData;
    private VideoMetadata videoMetadata;

    private FiveCentsData fiveCentsData;

    private FCCDNRequest fccdnRequest;

    DataTransferListener dataTransferListener;

    private String experimentName;
    private String mpdUrl;
    private String m3u8Url;
    private int heartbeatInterval = 1000;//59700;
//    private String key;
    private String hashID;
    private String title;
    private String path;
    private String playerKey;
    private PlayerType playerType;
    private String videoId;
    private Boolean ads = true;
    private Context context;
    private Boolean isLive;
    private Boolean randomizeUserId = false;
    private CollectorConfig config = new CollectorConfig();

    private boolean getLog;
    private TextView logTV;





    public static final Creator<FCCDNAnalyticsConfig> CREATOR =
            new Creator<FCCDNAnalyticsConfig>() {
                @Override
                public FCCDNAnalyticsConfig createFromParcel(Parcel in) {
                    return new FCCDNAnalyticsConfig(in);
                }

                @Override
                public FCCDNAnalyticsConfig[] newArray(int size) {
                    return new FCCDNAnalyticsConfig[size];
                }
            };


    public FCCDNAnalyticsConfig(@NotNull String hashID, @NotNull Context context) throws IllegalArgumentException{
        this.hashID = hashID;
        this.context = context;

        if(TextUtils.isEmpty(hashID)){
            throw new IllegalArgumentException("Invalid property id");
        }
        if(context == null){
            throw new IllegalArgumentException("Context is null or empty");
        }
    }


    protected FCCDNAnalyticsConfig(Parcel in) {
        cdnProvider = in.readString();
        experimentName = in.readString();
        mpdUrl = in.readString();
        m3u8Url = in.readString();
        heartbeatInterval = in.readInt();
//        key = in.readString();
        title = in.readString();
        path = in.readString();
        playerKey = in.readString();
        playerType = in.readParcelable(PlayerType.class.getClassLoader());
        videoId = in.readString();
        isLive = (Boolean) in.readSerializable();
        config = in.readParcelable(CollectorConfig.class.getClassLoader());
        ads = in.readInt() == 1;
        randomizeUserId = (Boolean) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cdnProvider);
        dest.writeString(experimentName);
        dest.writeString(mpdUrl);
        dest.writeString(m3u8Url);
        dest.writeInt(heartbeatInterval);
        dest.writeString(title);
        dest.writeString(path);
        dest.writeString(playerKey);
        dest.writeParcelable(playerType, flags);
        dest.writeString(videoId);
        dest.writeSerializable(isLive);
        dest.writeParcelable(config, config.describeContents());
        dest.writeInt(ads ? 1 : 0);
        dest.writeSerializable(randomizeUserId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getHashID() {
        return hashID;
    }

//    public String getKey() {
//        return key;
//    }


    public DataTransferListener getDataTransferListener() {
        return dataTransferListener;
    }

    public void setDataTransferListener(DataTransferListener dataTransferListener) {
        this.dataTransferListener = dataTransferListener;
    }


    public int getShowCV() {
        return showCV;
    }

    public void setShowCV(int showCV) {
        this.showCV = showCV;
    }

    public StyledPlayerView getPlayerView() {
        return playerView;
    }

    public void setPlayerView(StyledPlayerView playerView) {
        this.playerView = playerView;
    }

    public String getPlayerKey() {
        return playerKey;
    }

    public String getCdnProvider() {
        return cdnProvider;
    }

    public String getVideoId() {
        return videoId;
    }

    /**
     * ID of the Video in the Customer System
     *
     * @param videoId
     */
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    /**
     * Human readable title of the video asset currently playing
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Human readable title of the video asset currently playing
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getMpdUrl() {
        return this.mpdUrl;
    }

    public String getM3u8Url() {
        return this.m3u8Url;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public String getPath() {
        return path;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }


    public Context getContext() {
        return context;
    }

    /**
     * Configuration options for the Analytics collector
     *
     * @return collector configuration {@link CollectorConfig}
     */
    public CollectorConfig getConfig() {
        return config;
    }

    /**
     * Returns a value indicating if ads tracking is enabled
     *
     * @return
     */
    public Boolean getAds() {
        return ads;
    }

    /**
     * Enable or disable ads tracking
     *
     * @param ads
     */
    public void setAds(Boolean ads) {
        this.ads = ads;
    }

    /**
     * Returns true if the stream is marked as live before stream metadata is available.
     *
     * @return
     */
    public Boolean isLive() {
        return isLive;
    }

    /**
     * Returns true if random UserId value will be generated
     *
     * @return
     */

    public Boolean getRandomizeUserId() {
        return randomizeUserId;
    }



    public FCCDNRequest getFccdnRequest() {
        return fccdnRequest;
    }

    public void setFccdnRequest(FCCDNRequest fccdnRequest) {
        this.fccdnRequest = fccdnRequest;
    }

    public FiveCentsData getFiveCentsData() {
        return fiveCentsData;
    }

    public void setFiveCentsData(FiveCentsData fiveCentsData) {
        this.fiveCentsData = fiveCentsData;
    }



    public PlayerData getPlayerData() {
        return playerData;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }

    public VideoMetadata getVideoMetadata() {
        return videoMetadata;
    }

    public void setVideoMetadata(VideoMetadata videoMetadata) {
        this.videoMetadata = videoMetadata;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Handler playbackUpdateHandler = new Handler();

    public Handler getPlaybackUpdateHandler() {
        return playbackUpdateHandler;
    }

}
