package com.fivecentscdnanalytics;


import static com.fivecentscdnanalytics.utils.DataSerializer.serialize;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.fivecentscdnanalytics.adapters.AdAdapter;
import com.fivecentscdnanalytics.adapters.PlayerAdapter;
import com.fivecentscdnanalytics.calls.AnalyticsCallback;
import com.fivecentscdnanalytics.calls.DebugCallback;
import com.fivecentscdnanalytics.calls.InsightsReporter;
import com.fivecentscdnanalytics.config.FCCDNAnalyticsConfig;
import com.fivecentscdnanalytics.data.AdEventData;
import com.fivecentscdnanalytics.data.BackendFactory;
import com.fivecentscdnanalytics.data.DebuggingEventDataDispatcher;
import com.fivecentscdnanalytics.data.DeviceInformation;
import com.fivecentscdnanalytics.data.DeviceInformationProvider;
import com.fivecentscdnanalytics.data.ErrorCode;
import com.fivecentscdnanalytics.data.EventBus;
import com.fivecentscdnanalytics.data.EventData;
import com.fivecentscdnanalytics.data.EventDataFactory;
import com.fivecentscdnanalytics.data.IEventDataDispatcher;
import com.fivecentscdnanalytics.data.RandomizedUserIdIdProvider;
import com.fivecentscdnanalytics.data.SecureSettingsAndroidIdUserIdProvider;
import com.fivecentscdnanalytics.data.SimpleEventDataDispatcher;
import com.fivecentscdnanalytics.data.UserData;
import com.fivecentscdnanalytics.data.UserIdProvider;
import com.fivecentscdnanalytics.data.ViewerSession;
import com.fivecentscdnanalytics.data.manipulators.ManifestUrlEventDataManipulator;
import com.fivecentscdnanalytics.enums.FCCAdInsights;
import com.fivecentscdnanalytics.enums.VideoStartFailedReason;
import com.fivecentscdnanalytics.features.Feature;
import com.fivecentscdnanalytics.features.FeatureManager;
import com.fivecentscdnanalytics.features.errordetails.OnErrorDetailEventListener;
import com.fivecentscdnanalytics.interfaces.ImpressionIdProvider;
import com.fivecentscdnanalytics.interfaces.Observable;
import com.fivecentscdnanalytics.license.FeatureConfigContainer;
import com.fivecentscdnanalytics.license.LicenseCallback;
import com.fivecentscdnanalytics.listener.OnAnalyticsReleasingEventListener;
import com.fivecentscdnanalytics.stateMachines.PlayerStateMachine;
import com.fivecentscdnanalytics.stateMachines.PlayerStates;
import com.fivecentscdnanalytics.stateMachines.StateMachineListener;
import com.fivecentscdnanalytics.utils.FCCLog;
import com.fivecentscdnanalytics.utils.FCCPreferenceManager;
import com.fivecentscdnanalytics.utils.NetworkUtil;
import com.fivecentscdnanalytics.utils.Util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Calendar;
import java.util.Collection;

/**
 * An insights plugin that sends video playback insights to 5centsCDN Analytics servers. Currently
 * supports insights of ExoPlayer video players
 */
public class FCCDNAnalytics
        implements StateMachineListener, LicenseCallback, ImpressionIdProvider {

    private static final String TAG = "5centsCDNInsights";

    private FeatureManager<FeatureConfigContainer> featureManager = new FeatureManager<>();
    private final EventBus eventBus = new EventBus();

    private final FCCDNAnalyticsConfig fccdnAnalyticsConfig;
    @Nullable private PlayerAdapter playerAdapter;

    private PlayerStateMachine playerStateMachine;
    private FCCAdInsights adAnalytics;
    private IEventDataDispatcher eventDataDispatcher;

    private Context context;
    private final UserIdProvider userIdProvider;
    private final EventDataFactory eventDataFactory;
    private final DeviceInformationProvider deviceInformationProvider;
    private FCCPreferenceManager fccPreferenceManager;

    private AnalyticsCallback analyticsCallback;

    public AnalyticsCallback getAnalyticsCallback() {
        return analyticsCallback;
    }

    public void setAnalyticsCallback(AnalyticsCallback analyticsCallback) {
        this.analyticsCallback = analyticsCallback;
    }

    private InsightsReporter insightsReporter;

    public FCCDNAnalytics(
            FCCDNAnalyticsConfig fccdnAnalyticsConfig,
            Context context,
            @NotNull DeviceInformationProvider deviceInformationProvider) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        // FCCLog.d(TAG, "Initializing 5centsCDN Insights with Key: " + fccdnAnalyticsConfig.getKey());
        this.context = context;
        this.deviceInformationProvider = deviceInformationProvider;
        this.userIdProvider =
                fccdnAnalyticsConfig.getRandomizeUserId()
                        ? new RandomizedUserIdIdProvider()
                        : new SecureSettingsAndroidIdUserIdProvider(context);

        this.fccdnAnalyticsConfig = fccdnAnalyticsConfig;
        this.eventDataFactory = new EventDataFactory(fccdnAnalyticsConfig, this.userIdProvider);

        this.playerStateMachine = new PlayerStateMachine(this.fccdnAnalyticsConfig, this);
        this.playerStateMachine.addListener(this);

        this.fccPreferenceManager = FCCPreferenceManager.getInstance(fccdnAnalyticsConfig.getContext());
        this.insightsReporter = InsightsReporter.getInstance(this.fccdnAnalyticsConfig.getHashID(),this.fccdnAnalyticsConfig.getContext());

        IEventDataDispatcher innerEventDataDispatcher =
                new SimpleEventDataDispatcher(
                        this.fccdnAnalyticsConfig, this.context, this, new BackendFactory());


        this.eventDataDispatcher = new DebuggingEventDataDispatcher(innerEventDataDispatcher, debugCallback);

        if (this.fccdnAnalyticsConfig.getAds()) {
            this.adAnalytics = new FCCAdInsights(this);
        }
        createViewerSession();

    }

    private void createViewerSession() {

        boolean sessionCreationAllowed = false;
        if(this.fccPreferenceManager != null
                && this.fccPreferenceManager.getSessionTimeout() != 0){

            sessionCreationAllowed = checkSessionCreationAllowed();

            if(!sessionCreationAllowed){
                ViewerSession.setViewerSession(this.fccPreferenceManager.restoreSession());
            }

        }else if(this.fccdnAnalyticsConfig != null
                && this.fccPreferenceManager.getSessionTimeout() == 0){
            sessionCreationAllowed = true;
        }

        if(sessionCreationAllowed && this.fccdnAnalyticsConfig != null
                && this.deviceInformationProvider != null
                && this.userIdProvider != null){

            if(context == null && this.fccdnAnalyticsConfig.getContext() != null){
                this.context = this.fccdnAnalyticsConfig.getContext();
            }
            ViewerSession session = ViewerSession.getInstance();
            session.setPropertyId(this.fccdnAnalyticsConfig.getHashID());

            session.setMetaBrowser(this.userIdProvider.userId());
            session.setUserId(this.userIdProvider.userId());
            session.setMetaConnectionSpeed(NetworkUtil.networkConnectionSpeed(fccdnAnalyticsConfig.getContext()));

            DeviceInformation information = this.deviceInformationProvider.getDeviceInformation();

            if(information != null){

                if(!TextUtils.isEmpty(information.getUserAgent())) {
                    session.setMetaUserAgent(information.getUserAgent());
                }

                if(information.isTV()){
                    session.setMetaDeviceCategory("TV");
                    session.setMetaDeviceIsTouchScreen(false);
                }else{
                    session.setMetaDeviceCategory("MOBILE");
                    session.setMetaDeviceIsTouchScreen(true);
                }

                session.setMetaDeviceDisplayHeight(information.getScreenHeight());
                session.setMetaDeviceDisplayWidth(information.getScreenWidth());
                session.setMetaDeviceManufacturer(information.getManufacturer());
                session.setMetaDeviceName(information.getModel());
                session.setZ(Calendar.getInstance().getTimeInMillis());

                session.setMetaOperatingSystem("Android");
                session.setMetaOperatingSystemVersion(String.valueOf(Build.VERSION.SDK_INT));

                session.setMetaBrowser(context.getPackageName());

                try {
                    PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    String version = pInfo.versionName;
                    session.setMetaBrowserVersion(version);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    session.setMetaBrowserVersion("Unknown");
                }

                if(context != null){
                    if(NetworkUtil.getConnectionStatus(context) == NetworkUtil.WIFI) {
                        session.setMetaConnectionType("WiFi");
                    }else if(NetworkUtil.getConnectionStatus(context) == NetworkUtil.MOBILE){
                        session.setMetaConnectionType("Mobile");
                    }else if(NetworkUtil.getConnectionStatus(context) == NetworkUtil.NOT_CONNECTED){
                        session.setMetaConnectionType("No Network");
                    }

                }else{

                    if(NetworkUtil.getConnectionStatus(fccdnAnalyticsConfig.getContext()) == NetworkUtil.WIFI) {
                        session.setMetaConnectionType("WiFi");
                    }else if(NetworkUtil.getConnectionStatus(fccdnAnalyticsConfig.getContext()) == NetworkUtil.MOBILE){
                        session.setMetaConnectionType("Mobile");
                    }else if(NetworkUtil.getConnectionStatus(fccdnAnalyticsConfig.getContext()) == NetworkUtil.NOT_CONNECTED){
                        session.setMetaConnectionType("No Network");
                    }

                }

            }


            if(this.fccdnAnalyticsConfig.getUserData() != null){

                UserData data = this.fccdnAnalyticsConfig.getUserData();
                session.setCustomUserId(data.getCustomUserId());
                session.setUserName(data.getName());
                session.setUserEmail(data.getEmail());
                session.setUserPhone(data.getPhone());
                session.setUserProfileImage(data.getProfileImage());
                session.setUserAddressLine1(data.getAddressLine1());
                session.setUserAddressLine2(data.getAddressLine2());
                session.setUserCity(data.getCity());
                session.setUserState(data.getState());
                session.setUserCountry(data.getCountry());
                session.setUserZipcode(data.getZipCode());
            }

            ViewerSession.setViewerSession(session);
            this.insightsReporter.setViewerSession(session);

            if(NetworkUtil.isNetworkAvailable(fccdnAnalyticsConfig.getContext())
                    && NetworkUtil.isDataAvailable(fccdnAnalyticsConfig.getContext())) {



               // new ViewerSessionEventPresenter().logSessionEvent(fccdnAnalyticsConfig.getFiveCentsData(), null);

               // new ViewerSessionEventPresenter().LogSessionEvent(session,null);


               // Log.e("here", String.valueOf(response));


                this.fccPreferenceManager.setSessionTimeout(Calendar.getInstance().getTimeInMillis());



                if(session != null) {
                    this.fccPreferenceManager.saveSession(session);
                }
            }

        }
    }

    private boolean checkSessionCreationAllowed() {

        long previousSessionTime = this.fccPreferenceManager.getSessionTimeout();
        long currentTime = Calendar.getInstance().getTimeInMillis();

        if(currentTime>previousSessionTime){

            long timeDifference = currentTime - previousSessionTime;
            long thirtyMinutesInMillis = 30 * 60 * 1000;

            if(timeDifference>thirtyMinutesInMillis){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public Context getContext() {
        return context;
    }

    public FCCDNAnalyticsConfig getConfig() {
        return fccdnAnalyticsConfig;
    }

    public PlayerStateMachine getPlayerStateMachine() {
        return playerStateMachine;
    }

    /**
     * Attach a player instance to this insights plugin. After this is completed, 5centsCDNInsights
     * will start monitoring and sending insights data based on the attached player adapter.
     *
     * <p>To attach a different player instance, simply call this method again.
     */
    public void attach(PlayerAdapter adapter) {

        FCCLog.e(TAG,"5centsCDN_ANALYTICS - attach()");
        // detachPlayer();
        eventDataDispatcher.enable();
        this.playerAdapter = adapter;
        Collection<Feature<FeatureConfigContainer, ?>> features = this.playerAdapter.init();
        this.featureManager.registerFeatures(features);

        // this.registerEventDataManipulators(prePipelineManipulator);
        this.playerAdapter.registerEventDataManipulators(eventDataFactory);
        this.eventDataFactory.registerEventDataManipulator(
                new ManifestUrlEventDataManipulator(
                        this.playerAdapter, this.fccdnAnalyticsConfig));
        // this.registerEventDataManipulators(postPipelineManipulator);

        // tryAttachAd(adapter);

        if(this.analyticsCallback != null){
            this.analyticsCallback.onPlayerAttached();
        }
    }

    private void tryAttachAd(PlayerAdapter adapter) {
        if (adAnalytics == null) {
            return;
        }
        AdAdapter adAdapter = adapter.createAdAdapter();
        if (adAdapter == null) {
            return;
        }
        adAnalytics.attachAdapter(adAdapter);
    }

    /** Detach the current player that is being used with 5centsCDN Analytics. */
    public void detachPlayer() {
        FCCLog.e(TAG,"5centsCDN_ANALYTICS - detachPlayer()");
        // detachAd();

        featureManager.unregisterFeatures();
        eventBus.notify(
                OnAnalyticsReleasingEventListener.class,
                OnAnalyticsReleasingEventListener::onReleasing);

        if (playerAdapter != null) {
            playerAdapter.release();
        }

        if (playerStateMachine != null) {
            playerStateMachine.resetStateMachine();
        }
        eventDataDispatcher.disable();
        eventDataFactory.clearEventDataManipulators();

        if(this.analyticsCallback != null){
            this.analyticsCallback.onPlayerDetached();
        }
    }

    private void detachAd() {
        if (adAnalytics != null) {
            adAnalytics.detachAdapter();
        }
    }

    @Nullable
    public EventData createEventData() {
        if (playerAdapter == null) {
            return null;
        }
        return eventDataFactory.create(
                playerStateMachine.getImpressionId(),
                playerAdapter.getCurrentSourceMetadata(),
                deviceInformationProvider.getDeviceInformation());
    }

    @Override
    public void onStartup(long videoStartupTime, long playerStartupTime) {
        try{
            FCCLog.e(TAG,"5centsCDN_ANALYTICS - onStartup()");

           FCCLog.d(TAG, String.format("onStartup %s", playerStateMachine.getImpressionId()));
            EventData data = createEventData();
            data.setSupportedVideoCodecs(Util.getSupportedVideoFormats());
            data.setState("startup");
            data.setDuration(videoStartupTime + playerStartupTime);
            data.setVideoStartupTime(videoStartupTime);

            data.setDrmLoadTime(playerAdapter.getDRMDownloadTime());

            data.setPlayerStartupTime(playerStartupTime);
            data.setStartupTime(videoStartupTime + playerStartupTime);

            data.setVideoTimeStart(playerStateMachine.getVideoTimeStart());
            data.setVideoTimeEnd(playerStateMachine.getVideoTimeEnd());
            sendEventData(data);

            if(this.analyticsCallback != null){
                this.analyticsCallback.onPlayerStartup(data);
            }

        }catch (Exception ex){

        }
    }

    @Override
    public void onPauseExit(long duration) {
        try {
            FCCLog.e(TAG,"5centsCDN_ANALYTICS - onPauseExit()");
           FCCLog.d(TAG, String.format("onPauseExit %s", playerStateMachine.getImpressionId()));
            EventData data = createEventData();
            data.setState(playerStateMachine.getCurrentState().getName());
            data.setDuration(duration);
            data.setPaused(duration);
            data.setVideoTimeStart(playerStateMachine.getVideoTimeStart());
            data.setVideoTimeEnd(playerStateMachine.getVideoTimeEnd());

            if(this.analyticsCallback != null){
                this.analyticsCallback.onPlayerPauseExit(data);
            }
            sendEventData(data);
        }catch (Exception ex){

        }
    }

    @Override
    public void onPlayExit(long duration) {
        try {
            FCCLog.e(TAG,"5centsCDN_ANALYTICS - onPlayExit()");
            FCCLog.d(TAG, String.format("onPlayExit %s", playerStateMachine.getImpressionId()));
            EventData data = createEventData();
            data.setState(playerStateMachine.getCurrentState().getName());
            data.setDuration(duration);
            data.setPlayed(duration);
            data.setVideoTimeStart(playerStateMachine.getVideoTimeStart());
            data.setVideoTimeEnd(playerStateMachine.getVideoTimeEnd());
            if(this.analyticsCallback != null){
                this.analyticsCallback.onPlayerPlayExit(data);
            }
            sendEventData(data);
        }catch (Exception ex){

        }
    }

    @Override
    public void onRebuffering(long duration) {

        FCCLog.e(TAG,"5centsCDN_ANALYTICS - onRebuffering()");
        FCCLog.d(TAG, String.format("onRebuffering %s", playerStateMachine.getImpressionId()));
        EventData data = createEventData();
        data.setState(playerStateMachine.getCurrentState().getName());
        data.setDuration(duration);
        data.setBuffered(duration);
        data.setVideoTimeStart(playerStateMachine.getVideoTimeStart());
        data.setVideoTimeEnd(playerStateMachine.getVideoTimeEnd());
        if(this.analyticsCallback != null){
            this.analyticsCallback.onPlayerRebuffering(data);
        }
        sendEventData(data);
    }

    @Override
    public void onError(ErrorCode errorCode) {
        FCCLog.e(TAG,"5centsCDN_ANALYTICS - onError()");
        FCCLog.d(TAG, String.format("onError %s", playerStateMachine.getImpressionId()));
        EventData data = createEventData();
        data.setState(playerStateMachine.getCurrentState().getName());
        data.setVideoTimeStart(playerStateMachine.getVideoTimeEnd());
        data.setVideoTimeEnd(playerStateMachine.getVideoTimeEnd());

        if (playerStateMachine.getVideoStartFailedReason() != null) {
            data.setVideoStartFailedReason(
                    playerStateMachine.getVideoStartFailedReason().getReason());
            data.setVideoStartFailed(true);
        }

        data.setErrorCode(errorCode.getErrorCode());
        data.setErrorMessage(errorCode.getDescription());
        data.setErrorData(serialize(errorCode.getLegacyErrorData()));

        if(this.analyticsCallback != null){
            this.analyticsCallback.onPlayerError(data);
        }
        sendEventData(data);



        eventBus.notify(
                OnErrorDetailEventListener.class,
                listener ->
                        listener.onError(
                                errorCode.getErrorCode(),
                                errorCode.getDescription(),
                                errorCode.getErrorData()));
    }

    @Override
    public void onSeekComplete(long duration) {
        FCCLog.e(TAG,"5centsCDN_ANALYTICS - onSeekComplete()");
        FCCLog.d(TAG, String.format("onSeekComplete %s", playerStateMachine.getImpressionId()));
        EventData data = createEventData();
        data.setState(playerStateMachine.getCurrentState().getName());
        data.setSeeked(duration);
        data.setDuration(duration);
        data.setVideoTimeStart(playerStateMachine.getVideoTimeStart());
        data.setVideoTimeEnd(playerStateMachine.getVideoTimeEnd());

        if(this.analyticsCallback != null){
            this.analyticsCallback.onPlayerSeekCompleted(data);
        }
        sendEventData(data);
    }

    @Override
    public void onHeartbeat(long duration) {
        FCCLog.d(
                TAG,
                String.format(
                        "onHeartbeat %s %s",
                        playerStateMachine.getCurrentState().getName(),
                        playerStateMachine.getImpressionId()));

        FCCLog.e(TAG,"5centsCDN_ANALYTICS - onHeartbeat()");

        EventData data = createEventData();
        data.setState(playerStateMachine.getCurrentState().getName());
        data.setDuration(duration);

        if (playerStateMachine.getCurrentState() == PlayerStates.PLAYING) {
            data.setPlayed(duration);
        } else if (playerStateMachine.getCurrentState() == PlayerStates.PAUSE) {
            data.setPaused(duration);
        } else if (playerStateMachine.getCurrentState() == PlayerStates.BUFFERING) {
            data.setBuffered(duration);
        }

        data.setVideoTimeStart(playerStateMachine.getVideoTimeStart());
        data.setVideoTimeEnd(playerStateMachine.getVideoTimeEnd());

        if(this.analyticsCallback != null){
            this.analyticsCallback.onPlayerHeartBeat(data);
        }
        sendEventData(data);
    }

    @Override
    public void onAd() {
        FCCLog.d(TAG, "onAd");
    }

    @Override
    public void onMute() {
        FCCLog.d(TAG, "onMute");
    }

    @Override
    public void onUnmute() {
        FCCLog.d(TAG, "onUnmute");
    }

    @Override
    public void onUpdateSample() {
        FCCLog.d(TAG, "onUpdateSample");
    }

    @Override
    public void onQualityChange() {
        FCCLog.e(TAG,"5centsCDN_ANALYTICS - onQualityChange()");
        FCCLog.d(TAG, String.format("onQualityChange %s", playerStateMachine.getImpressionId()));
        EventData data = createEventData();
        data.setState(playerStateMachine.getCurrentState().getName());
        data.setDuration(0);
        sendEventData(data);
        data.setVideoTimeStart(playerStateMachine.getVideoTimeEnd());
        data.setVideoTimeEnd(playerStateMachine.getVideoTimeEnd());
    }

    @Override
    public void onVideoChange() {
        FCCLog.e(TAG,"5centsCDN_ANALYTICS - onVideoChange()");
        FCCLog.d(TAG, "onVideoChange");
    }

    @Override
    public void onSubtitleChange() {
        FCCLog.e(TAG,"5centsCDN_ANALYTICS - onSubtitleChange()");
        FCCLog.d(TAG, String.format("onSubtitleChange %s", playerStateMachine.getImpressionId()));
        EventData data = createEventData();
        data.setState(playerStateMachine.getCurrentState().getName());
        data.setDuration(0);
        sendEventData(data);
        data.setVideoTimeStart(playerStateMachine.getVideoTimeStart());
        data.setVideoTimeEnd(playerStateMachine.getVideoTimeEnd());
    }

    @Override
    public void onAudioTrackChange() {
        FCCLog.e(TAG,"5centsCDN_ANALYTICS - onAudioTrackChange()");
        FCCLog.d(TAG, String.format("onAudioTrackChange %s", playerStateMachine.getImpressionId()));
        EventData data = createEventData();
        data.setState(playerStateMachine.getCurrentState().getName());
        data.setDuration(0);
        sendEventData(data);
        data.setVideoTimeStart(playerStateMachine.getVideoTimeStart());
        data.setVideoTimeEnd(playerStateMachine.getVideoTimeEnd());
    }

    @Override
    public void onVideoStartFailed() {

        FCCLog.e(TAG,"5centsCDN_ANALYTICS - onVideoStartFailed()");

        VideoStartFailedReason videoStartFailedReason =
                playerStateMachine.getVideoStartFailedReason();
        if (videoStartFailedReason == null) {
            videoStartFailedReason = VideoStartFailedReason.UNKNOWN;
        }

        EventData data = createEventData();
        data.setState(playerStateMachine.getCurrentState().getName());
        data.setVideoStartFailed(true);
        ErrorCode errorCode = videoStartFailedReason.getErrorCode();
        if (errorCode != null) {
            data.setErrorCode(errorCode.getErrorCode());
            data.setErrorMessage(errorCode.getDescription());
            data.setErrorData(serialize(errorCode.getLegacyErrorData()));
            eventBus.notify(
                    OnErrorDetailEventListener.class,
                    listener ->
                            listener.onError(
                                    errorCode.getErrorCode(),
                                    errorCode.getDescription(),
                                    errorCode.getErrorData()));
        }
        data.setVideoStartFailedReason(videoStartFailedReason.getReason());
        sendEventData(data);
        this.detachPlayer();
    }

    public final void resetSourceRelatedState() {
        if (this.eventDataDispatcher != null) {
            this.eventDataDispatcher.resetSourceRelatedState();
        }

        featureManager.resetFeatures();
        // TODO reset features and prepare for new source

        if (this.playerAdapter != null) {
            this.playerAdapter.resetSourceRelatedState();
        }
    }

    public void sendEventData(EventData data) {
        this.eventDataDispatcher.add(data);
        if (this.playerAdapter != null) {
            this.playerAdapter.clearValues();
        }
    }

    public void sendAdEventData(AdEventData data) {
        this.eventDataDispatcher.addAd(data);
    }

    public long getPosition() {
        if (playerAdapter == null) {
            return 0;
        }
        return playerAdapter.getPosition();
    }

    public Observable<OnAnalyticsReleasingEventListener> getOnAnalyticsReleasingObservable() {
        return eventBus.get(OnAnalyticsReleasingEventListener.class);
    }

    public Observable<OnErrorDetailEventListener> getOnErrorDetailObservable() {
        return eventBus.get(OnErrorDetailEventListener.class);
    }

    @Override
    public void configureFeatures(
            boolean authenticated, @Nullable FeatureConfigContainer featureConfigs) {
        featureManager.configureFeatures(authenticated, featureConfigs);
    }

    @Override
    public void authenticationCompleted(boolean success) {
        if (!success) {
            detachPlayer();
        }
    }

    public void addDebugListener(DebugListener listener) {
        eventBus.get(DebugListener.class).subscribe(listener);
    }

    public void removeDebugListener(DebugListener listener) {
        eventBus.get(DebugListener.class).unsubscribe(listener);
    }

    @NotNull
    @Override
    public String getImpressionId() {
        return this.playerStateMachine.getImpressionId();
    }

    public interface DebugListener {
        void onDispatchEventData(EventData data);

        void onDispatchAdEventData(AdEventData data);

        void onMessage(String message);
    }

    private DebugCallback debugCallback =
            new DebugCallback() {
                @Override
                public void dispatchEventData(@NotNull EventData data) {
                    playerStateMachine.setCurrentEventData(data);
                    FCCDNAnalytics.this.fccPreferenceManager.setSessionTimeout(Calendar.getInstance().getTimeInMillis());

                    eventBus.notify(
                            DebugListener.class, listener -> listener.onDispatchEventData(data));
                }

                @Override
                public void dispatchAdEventData(@NotNull AdEventData data) {
                    eventBus.notify(
                            DebugListener.class, listener -> listener.onDispatchAdEventData(data));
                }

                @Override
                public void message(@NotNull String message) {
                    eventBus.notify(DebugListener.class, listener -> listener.onMessage(message));
                }
            };
}
