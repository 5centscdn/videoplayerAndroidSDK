package com.fivecentscdnanalytics.adapters;

import static com.fivecentscdnanalytics.enums.Events.EVENT_PAUSE;
import static com.fivecentscdnanalytics.enums.Events.EVENT_PLAY;
import static com.fivecentscdnanalytics.enums.Events.EVENT_PLAYBACK_READY;
import static com.fivecentscdnanalytics.enums.Events.EVENT_PLAYBACK_UPDATE;
import static com.fivecentscdnanalytics.enums.Events.EVENT_PLAYER_READY;
import static com.fivecentscdnanalytics.enums.Events.EVENT_PLAYING;
import static com.fivecentscdnanalytics.enums.Events.EVENT_REBUFFER_END;
import static com.fivecentscdnanalytics.enums.Events.EVENT_REBUFFER_START;
import static com.fivecentscdnanalytics.enums.Events.EVENT_SEEKED;
import static com.fivecentscdnanalytics.enums.Events.EVENT_SETUP;
import static com.google.android.exoplayer2.C.CLEARKEY_UUID;
import static com.google.android.exoplayer2.C.DATA_TYPE_MANIFEST;
import static com.google.android.exoplayer2.C.DATA_TYPE_MEDIA;
import static com.google.android.exoplayer2.C.PLAYREADY_UUID;
import static com.google.android.exoplayer2.C.TIME_UNSET;
import static com.google.android.exoplayer2.C.WIDEVINE_UUID;
import static com.google.android.exoplayer2.Player.STATE_ENDED;

import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.fivecentscdnanalytics.enums.ExoPlayerExceptionMapper;
import com.fivecentscdnanalytics.data.PlayerInstance;
import com.fivecentscdnanalytics.data.ViewerSession;
import com.fivecentscdnanalytics.data.ViewerSessionEvent;
import com.fivecentscdnanalytics.calls.AnalyticsCallback;
import com.fivecentscdnanalytics.calls.InsightsReporter;
import com.fivecentscdnanalytics.calls.RebufferListener;
import com.fivecentscdnanalytics.calls.events.PlayerEvents;
import com.fivecentscdnanalytics.calls.events.SessionEvents;
import com.fivecentscdnanalytics.calls.presenter.PlayerInstancePresenter;
import com.fivecentscdnanalytics.calls.presenter.ViewerSessionEventPresenter;
import com.fivecentscdnanalytics.config.FCCDNAnalyticsConfig;
import com.fivecentscdnanalytics.config.SourceMetadata;
import com.fivecentscdnanalytics.data.ErrorCode;
import com.fivecentscdnanalytics.data.EventData;
import com.fivecentscdnanalytics.data.FCCDNRequest;
import com.fivecentscdnanalytics.data.SpeedMeasurement;
import com.fivecentscdnanalytics.data.VideoMetadata;
import com.fivecentscdnanalytics.data.manipulators.EventDataManipulator;
import com.fivecentscdnanalytics.data.manipulators.EventDataManipulatorPipeline;
import com.fivecentscdnanalytics.enums.DRMType;
import com.fivecentscdnanalytics.enums.Events;
import com.fivecentscdnanalytics.enums.PlayerType;
import com.fivecentscdnanalytics.enums.VideoStartFailedReason;
import com.fivecentscdnanalytics.features.Feature;
import com.fivecentscdnanalytics.features.FeatureFactory;
import com.fivecentscdnanalytics.license.FeatureConfigContainer;
import com.fivecentscdnanalytics.listener.DefaultAnalyticsListener;
import com.fivecentscdnanalytics.listener.DefaultPlayerEventListener;
import com.fivecentscdnanalytics.manipulators.BitrateEventDataManipulator;
import com.fivecentscdnanalytics.stateMachines.PlayerState;
import com.fivecentscdnanalytics.stateMachines.PlayerStateMachine;
import com.fivecentscdnanalytics.stateMachines.PlayerStates;
import com.fivecentscdnanalytics.utils.DownloadSpeedMeter;
import com.fivecentscdnanalytics.utils.ExoUtil;
import com.fivecentscdnanalytics.utils.FCCLog;
import com.fivecentscdnanalytics.utils.NetworkUtil;
import com.fivecentscdnanalytics.utils.Util;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Tracks;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.analytics.PlaybackStats;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.LoadEventInfo;
import com.google.android.exoplayer2.source.MediaLoadData;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.hls.HlsManifest;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.fivecentscdnanalytics.error.ExceptionMapper;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class ExoPlayerAdapter implements PlayerAdapter, EventDataManipulator,
        SessionEvents,
        PlayerEvents, AnalyticsCallback, RebufferListener {
    private static final String TAG = "ExoPlayerAdapter";

    private static final String DASH_MANIFEST_CLASSNAME =
            "com.google.android.exoplayer2.source.dash.manifest.DashManifest";
    private static final String HLS_MANIFEST_CLASSNAME =
            "com.google.android.exoplayer2.source.hls.HlsManifest";

    private Boolean _isDashManifestClassLoaded;
    private Boolean _isHlsManifestClassLoaded;


    private final FCCDNAnalyticsConfig fccdnAnalyticsConfig;

    FCCDNRequest fccdnRequest;
    private ExoPlayer exoplayer;

    DefaultBandwidthMeter defaultBandwidthMeter;
    private PlayerStateMachine stateMachine;
    private int totalDroppedVideoFrames;
    private boolean playerIsReady;
    private String manifestUrl;
    private ExceptionMapper<Throwable> exceptionMapper = new ExoPlayerExceptionMapper();
    private BitrateEventDataManipulator bitrateEventDataManipulator;
    private DownloadSpeedMeter meter = new DownloadSpeedMeter();
    private boolean isVideoAttemptedPlay = false;
    private boolean isPlaying = false;
    private boolean isInInitialBufferState = false;
    protected final DefaultAnalyticsListener defaultAnalyticsListener;
    protected final DefaultPlayerEventListener defaultPlayerEventListener;
    private FeatureFactory featureFactory;

    private long drmLoadStartTime = 0;
    private Long drmDownloadTime = null;
    private String drmType = null;

    private ViewerSessionEventPresenter viewerSessionEventPresenter;
    private PlayerInstancePresenter playerInstancePresenter;

    private InsightsReporter insightsReporter;

    DefaultBandwidthMeter bandwidthMeter;

    public ExoPlayerAdapter(
            ExoPlayer exoplayer,
            FCCDNAnalyticsConfig fccdnAnalyticsConfig,
            PlayerStateMachine stateMachine,
            FeatureFactory featureFactory) {
        this.featureFactory = featureFactory;
        this.defaultAnalyticsListener = createAnalyticsListener();
        this.defaultPlayerEventListener = createPlayerEventListener();
        this.stateMachine = stateMachine;
        this.stateMachine.getAnalytics().setAnalyticsCallback(this);
        this.stateMachine.setRebufferListener(this);
        this.exoplayer = exoplayer;
        this.exoplayer.addListener(defaultPlayerEventListener);
        this.fccdnAnalyticsConfig = fccdnAnalyticsConfig;

        this.fccdnRequest = fccdnAnalyticsConfig.getFccdnRequest();
        this.bitrateEventDataManipulator = new BitrateEventDataManipulator(exoplayer);
        attachAnalyticsListener();

        this.viewerSessionEventPresenter = new ViewerSessionEventPresenter();
        this.playerInstancePresenter = new PlayerInstancePresenter();

        this.insightsReporter = InsightsReporter.getInstance(this.fccdnAnalyticsConfig.getHashID(),this.fccdnAnalyticsConfig.getContext());

        this.insightsReporter.setViewerSession(ViewerSession.getInstance());

        createPlayerInstance(exoplayer);
    }

    private void createPlayerInstance(ExoPlayer exoplayer) {
        PlayerInstance playerInstance = PlayerInstance.getInstance();

        if(this.exoplayer != null
                && this.exoplayer.getMediaMetadata() != null
                && this.exoplayer.getMediaMetadata().artworkUri != null) {

            playerInstance.setMetaPageType(exoplayer.getMediaMetadata().artworkUri.toString());
        }else{
            playerInstance.setMetaPageType(Util.UNKNOWN);
        }


        if(this.exoplayer != null && this.exoplayer.getVideoSize().height != 0){
            playerInstance.setPlayerHeightPixels(this.exoplayer.getVideoSize().height);
            playerInstance.setPlayerWidthPixels(this.exoplayer.getVideoSize().width);
        }else{
            playerInstance.setPlayerHeightPixels(0);
            playerInstance.setPlayerWidthPixels(0);
        }



        playerInstance.setPlayerSoftware("ExoPlayer");
        playerInstance.setPropertyId(this.fccdnAnalyticsConfig.getHashID());

        if(this.fccdnAnalyticsConfig.getPlayerData() != null){
            playerInstance.setPlayerIntegrationVersion(this.fccdnAnalyticsConfig.getPlayerData().getPlayerIntegrationVersion());
            playerInstance.setPlayerName(this.fccdnAnalyticsConfig.getPlayerData().getPlayerName());
            playerInstance.setMetaPageType(this.fccdnAnalyticsConfig.getPlayerData().getMetaPageType());
        }



        if(this.fccdnAnalyticsConfig.getUserData() != null) {
            playerInstance.setCustomUserId(this.fccdnAnalyticsConfig.getUserData().getCustomUserId());
        }

        PlayerInstance.setPlayerInstance(playerInstance);
        this.insightsReporter.setPlayerInstance(playerInstance);



        if(NetworkUtil.isNetworkAvailable(fccdnAnalyticsConfig.getContext())
                && NetworkUtil.isDataAvailable(fccdnAnalyticsConfig.getContext())) {
            Log.e("here", "createPlayerInstance: " );
            playerInstancePresenter.playerInit(this, fccdnAnalyticsConfig.getFccdnRequest(), exoplayer);
        }


    }

    private boolean isHlsManifestClassLoaded() {
        if (this._isHlsManifestClassLoaded == null) {
            this._isHlsManifestClassLoaded =
                    Util.isClassLoaded(HLS_MANIFEST_CLASSNAME, this.getClass().getClassLoader());
        }
        return this._isHlsManifestClassLoaded;
    }

    private boolean isDashManifestClassLoaded() {
        if (this._isDashManifestClassLoaded == null) {
            this._isDashManifestClassLoaded =
                    Util.isClassLoaded(DASH_MANIFEST_CLASSNAME, this.getClass().getClassLoader());
        }
        return this._isDashManifestClassLoaded;
    }

    private void attachAnalyticsListener() {
        if (this.exoplayer != null) {
            exoplayer.addAnalyticsListener(defaultAnalyticsListener);
        }
    }

    private void startup(long position) {
        bitrateEventDataManipulator.setFormatsFromPlayer();
        stateMachine.transitionState(PlayerStates.STARTUP, position);
        isVideoAttemptedPlay = true;
    }

    @Override
    public Collection<Feature<FeatureConfigContainer, ?>> init() {
        this.totalDroppedVideoFrames = 0;
        this.playerIsReady = false;
        this.isInInitialBufferState = false;
        this.isVideoAttemptedPlay = false;
        isPlaying = false;
        checkAutoplayStartup();
        return featureFactory.createFeatures();
    }

    @Override
    public SourceMetadata getCurrentSourceMetadata() {
        /* Adapter doesn't support source-specific metadata */
        return null;
    }

    /*
     * Because of the late initialization of the Adapter we do not get the first
     * couple of events so in case the player starts a video due to autoplay=true we
     * need to transition into startup state manually
     */
    private void checkAutoplayStartup() {
        int playbackState = exoplayer.getPlaybackState();

        boolean isBufferingAndWillAutoPlay =
                exoplayer.getPlayWhenReady() && playbackState == Player.STATE_BUFFERING;
        /*
         * Even if flag was set as `player.setPlayWhenReady(false)`, when player is
         * playing, flags is returned as `true`
         */
        boolean isAlreadyPlaying =
                exoplayer.getPlayWhenReady() && playbackState == Player.STATE_READY;

        if (isBufferingAndWillAutoPlay || isAlreadyPlaying) {
            this.isPlaying = true;

            long position = getPosition();
            FCCLog.d(
                    TAG,
                    "Collector was attached while media source was already loading, transitioning to startup state.");
            startup(position);

            if (playbackState == Player.STATE_READY) {
                FCCLog.d(
                        TAG,
                        "Collector was attached while media source was already playing, transitioning to playing state");
                stateMachine.transitionState(PlayerStates.PLAYING, position);
            }
        }
    }



    @Override
    public void manipulate(@NotNull EventData data) {
        // duration
        long duration = exoplayer.getDuration();
        if (duration != TIME_UNSET) {
            data.setVideoDuration(duration);
        }

        // ad
        if (exoplayer.isPlayingAd()) {
            data.setAd(1);
        }

        // isLive
        data.setLive(
                Util.getIsLiveFromConfigOrPlayer(
                        playerIsReady, fccdnAnalyticsConfig.isLive(), exoplayer.isCurrentWindowDynamic()));

        // version
        data.setVersion(PlayerType.EXOPLAYER.toString() + "-" + ExoUtil.getPlayerVersion());

        // DroppedVideoFrames
        data.setDroppedFrames(this.totalDroppedVideoFrames);
        this.totalDroppedVideoFrames = 0;

        // streamFormat, mpdUrl, and m3u8Url
        Object manifest = exoplayer.getCurrentManifest();
        if (isDashManifestClassLoaded() && manifest instanceof DashManifest) {
            DashManifest dashManifest;
            dashManifest = (DashManifest) manifest;
            data.setStreamFormat(Util.DASH_STREAM_FORMAT);
            if (dashManifest.location == null) {
                data.setMpdUrl(this.manifestUrl);
            } else {
                data.setMpdUrl(dashManifest.location.toString());
            }
        } else if (isHlsManifestClassLoaded() && manifest instanceof HlsManifest) {
            HlsMasterPlaylist masterPlaylist = ((HlsManifest) manifest).masterPlaylist;
            HlsMediaPlaylist mediaPlaylist = ((HlsManifest) manifest).mediaPlaylist;
            data.setStreamFormat(Util.HLS_STREAM_FORMAT);
            if (masterPlaylist != null && masterPlaylist.baseUri != null) {
                data.setM3u8Url(masterPlaylist.baseUri);
            } else if (mediaPlaylist != null) {
                data.setM3u8Url(mediaPlaylist.baseUri);
            }
        }

        data.setDownloadSpeedInfo(meter.getInfo());

        // DRM Information
        data.setDrmType(drmType);
    }

    @Override
    public void release() {
        playerIsReady = false;
        this.isInInitialBufferState = false;
        manifestUrl = null;
        if (this.exoplayer != null) {
            this.exoplayer.removeListener(this.defaultPlayerEventListener);
        }
        if (this.exoplayer instanceof ExoPlayer) {
            ExoPlayer simpleExoPlayer = (ExoPlayer) this.exoplayer;
            simpleExoPlayer.removeAnalyticsListener(defaultAnalyticsListener);
        }
        meter.reset();
        bitrateEventDataManipulator.reset();
        stateMachine.resetStateMachine();
    }

    @Override
    public void resetSourceRelatedState() {
        bitrateEventDataManipulator.reset();
        // no Playlist transition event in older version of collector (v1)
    }

    @Override
    public void registerEventDataManipulators(EventDataManipulatorPipeline pipeline) {
        pipeline.registerEventDataManipulator(this);
        pipeline.registerEventDataManipulator(bitrateEventDataManipulator);
    }

    @Override
    public long getPosition() {
        Timeline timeline = this.exoplayer.getCurrentTimeline();

        int currentWindowIndex = this.exoplayer.getCurrentWindowIndex();

        if (currentWindowIndex >= 0 && currentWindowIndex < timeline.getWindowCount()) {

            Timeline.Window currentWindow = new Timeline.Window();
            timeline.getWindow(currentWindowIndex, currentWindow);

            int firstPeriodInWindowIndex = currentWindow.firstPeriodIndex;

            Timeline.Period firstPeriodInWindow = new Timeline.Period();

            if (firstPeriodInWindowIndex >= 0
                    && firstPeriodInWindowIndex < timeline.getPeriodCount()) {

                timeline.getPeriod(firstPeriodInWindowIndex, firstPeriodInWindow);
                long position =
                        (exoplayer.getCurrentPosition()
                                - firstPeriodInWindow.getPositionInWindowMs());
                if (position < 0) {
                    position = 0;
                }
                return position;
            }
        }
        return 0;
    }

    @Override
    public Long getDRMDownloadTime() {
        return drmDownloadTime;
    }

    @Override
    public void clearValues() {
        meter.reset();
    }

    private boolean rebufferStarted;
    private long seekStarted = 0L;
    private long seekProcessed = 0L;

    private DefaultAnalyticsListener createAnalyticsListener(){

        return new DefaultAnalyticsListener() {

            @Override
            public void onSeekProcessed(@NonNull EventTime eventTime) {
                long videoTime = getPosition();
                FCCLog.e(TAG,"####### onSeekProcessed()-->videoTime = "+videoTime);
                seekProcessed = videoTime;
                insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
                createAndCallSessionEvent(EVENT_SEEKED,eventTime);
            }

            @Override
            public void onIsLoadingChanged(@NonNull EventTime eventTime, boolean isLoading) {
                FCCLog.d(TAG, "onIsLoadingChanged");
            }

            @Override
            public void onPlayerError(@NonNull EventTime eventTime, @NonNull ExoPlaybackException error) {
                FCCLog.d(TAG, "onPlayerError");
                fccdnAnalyticsConfig.getFccdnRequest().setPlayerError(true);
                FCCLog.d("here", "onLoadError set");
            }

            @Override
            public void onSeekStarted(@NonNull EventTime eventTime) {
                FCCLog.e(TAG,"####### onSeekStarted()");
                try {
                    long videoTime = getPosition();
                    FCCLog.e(TAG,"####### onSeekStarted()-->videoTime = "+videoTime);
                    seekStarted = videoTime;
                    ExoPlayerAdapter.this.stateMachine.transitionState(
                            PlayerStates.SEEKING, videoTime);

                    if(!rebufferStarted && playbackStarted){
                        insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
                        createAndCallSessionEvent(EVENT_REBUFFER_START,eventTime);
                        rebufferStarted = true;

                        if(playbackUpdateEnabled){
                            disablePlaybackUpdate();
                            playbackUpdateDelay = 200;
                            playbackUpdateEnabled = false;
                        }
                    }

                } catch (Exception e) {
                    FCCLog.d(TAG, e.getMessage(), e);
                }


            }


            private long playBackUpdateTime = 0;

            @Override
            public void onLoadingChanged(@NonNull EventTime eventTime, boolean isLoading) {
                FCCLog.d(TAG, "onLoadingChanged()");
            }

            @Override
            public void onPlaybackStateChanged(@NonNull EventTime eventTime, int state) {
                FCCLog.e(TAG,"onPlaybackStateChanged()  --> state = "+state);


                if(stateMachine.getAnalyticsCallback() == null){
                    stateMachine.setAnalyticsCallback(ExoPlayerAdapter.this);
                }
                try {
                    long videoTime = getPosition();
                    FCCLog.e(
                            TAG,
                            String.format(
                                    "DefaultAnalyticsListener-->onPlaybackStateChanged()--> %s play When ready: %b isPlaying: %b",
                                    ExoUtil.exoStateToString(state),
                                    ExoPlayerAdapter.this.exoplayer.getPlayWhenReady(),
                                    ExoPlayerAdapter.this.exoplayer.isPlaying()));

                    ViewerSessionEvent viewerSessionEvent = insightsReporter.getViewerSessionEvent();


                    if(insightsReporter.getViewerSession() != null) {
                        viewerSessionEvent.setSessionId(insightsReporter.getViewerSession().getSessionId());
                        viewerSessionEvent.setPropertyId(insightsReporter.getViewerSession().getPropertyId());
                        viewerSessionEvent.setUserId(insightsReporter.getViewerSession().getUserId());
                    }else{
                        viewerSessionEvent.setSessionId(ViewerSession.getInstance().getSessionId());
                        viewerSessionEvent.setPropertyId(ViewerSession.getInstance().getPropertyId());
                        viewerSessionEvent.setUserId(ViewerSession.getInstance().getUserId());
                    }
                    viewerSessionEvent.setPlayerInstanceId(insightsReporter.getPlayerInstance().getPlayerInstanceId());

                    if(!viewerSessionEvent.isSetupEventDone()){
                        insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
                        createAndCallSessionEvent(EVENT_PLAYBACK_READY,eventTime);
                    }
                    switch (state) {
                        case Player.STATE_READY:
                            // if autoplay is enabled startup state is not yet finished
                            if (!stateMachine.isStartupFinished() && (stateMachine.getCurrentState() != PlayerStates.STARTUP && exoplayer.getPlayWhenReady())) {
                                stateMachine.transitionState(PlayerStates.READY, getPosition());
                                fccdnRequest.setPlayerInitEndTime(System.currentTimeMillis());

                                Log.e("here", "onPlaybackStateChanged: " + System.currentTimeMillis());
                            }else {

                            }
                            break;
                        case Player.STATE_BUFFERING:
                            if(!ExoPlayerAdapter.this.isPlaying && (stateMachine.getCurrentState() == PlayerStates.SEEKING
                                        /*|| stateMachine.getCurrentState() == PlayerStates.READY*/) && playbackStarted){

                                        if(rebufferStarted){
                                            callRebufferEnd();

                                            if(!playbackUpdateEnabled) {
                                                enablePlaybackUpdate();
                                                playbackUpdateEnabled = true;
                                            }
                                        }

                                    }else if(stateMachine.getCurrentState() == PlayerStates.PLAYING){
                                        insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
                                        createAndCallSessionEvent(EVENT_PLAYING,eventTime);

                            }else if(stateMachine.getCurrentState() == PlayerStates.PAUSE){
                                insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
                                createAndCallSessionEvent(EVENT_PAUSE,eventTime);

                                playbackPaused = true;
                                playbackUpdateDelay = 500;

                            }else if (!stateMachine.isStartupFinished()) {
                                // this is the case when there is no preloading
                                // player is now starting to get content before playing it
                                if (ExoPlayerAdapter.this.exoplayer.getPlayWhenReady()) {
                                    startup(videoTime);

                                    ExoPlayerAdapter.this.isInInitialBufferState = true;

                                } else {
                                    // this is the case when preloading of content is setup
                                    // so at this point player is getting content and will start
                                    // playing
                                    // once user preses play
                                    ExoPlayerAdapter.this.isInInitialBufferState = true;


                                }
                            } else if (ExoPlayerAdapter.this.isPlaying
                                    && stateMachine.getCurrentState() != PlayerStates.SEEKING) {

                                ExoPlayerAdapter.this.stateMachine.transitionState(PlayerStates.BUFFERING, videoTime);
                                insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
                                createAndCallSessionEvent(EVENT_REBUFFER_START,eventTime);
                                rebufferStarted = true;

                                if(playbackUpdateEnabled){
                                    disablePlaybackUpdate();
                                    playbackUpdateDelay = 200;
                                    playbackUpdateEnabled = false;
                                }

                            }


                            break;
                        case Player.STATE_IDLE:
                            // TODO check what this state could mean for insights?
                            break;
                        case STATE_ENDED:
                            // TODO this is equivalent to BMPs PlaybackFinished Event
                            // should we setup new impression here
                            // onIsPlayingChanged is triggered after this event and does transition
                            // to PAUSE
                            // state

                            playerInstancePresenter.postCompleted(fccdnRequest, ExoPlayerAdapter.this);
                            if(!endEventCalled) {
                                callEndEvent(eventTime);
                            }
                            break;
                        default:
                            FCCLog.d(TAG, "Unknown Player PlayerState encountered");
                    }
                } catch (Exception e) {
                    FCCLog.d(TAG, e.getMessage(), e);
                }
            }

            @Override
            public void onDrmKeysLoaded(@NonNull EventTime eventTime) {
                FCCLog.e(TAG,"onDrmKeysLoaded()");
                try {
                    drmDownloadTime = eventTime.realtimeMs - drmLoadStartTime;
                    FCCLog.d(TAG, String.format("DRM Keys loaded %d", eventTime.realtimeMs));
                } catch (Exception e) {
                    FCCLog.d(TAG, e.getMessage(), e);
                }
            }

            @Override
            public void onPlaybackSuppressionReasonChanged(@NonNull EventTime eventTime, int playbackSuppressionReason) {
                FCCLog.d(TAG, "onPlaybackSuppressionReasonChanged");
            }

            @Override
            public void onSkipSilenceEnabledChanged(@NonNull EventTime eventTime, boolean skipSilenceEnabled) {
                FCCLog.d(TAG, "onSkipSilenceEnabledChanged");
            }

            @Override
            public void onIsPlayingChanged(@NonNull EventTime eventTime, boolean isPlaying) {

                FCCLog.e(TAG,"###### onIsPlayingChanged()-->isPlaying = "+isPlaying);
                FCCLog.e(TAG,"###### onIsPlayingChanged()-->PlayerState = "+stateMachine.getCurrentState());
                FCCLog.e(TAG,"###### onIsPlayingChanged()-->rebufferStarted = "+rebufferStarted);

                if(isPlaying){
                    if (fccdnAnalyticsConfig.getFccdnRequest().isShowCV() && !fccdnRequest.isPlayerError()){
                        playerInstancePresenter.postIfShowCV(fccdnAnalyticsConfig.getFccdnRequest(), fccdnAnalyticsConfig);
                    }

                    if(rebufferStarted){
                        callRebufferEnd();
                    }

                    if(!playbackUpdateEnabled) {
                        enablePlaybackUpdate();
                        playbackUpdateEnabled = true;
                    }


                }else{
                    if (stateMachine.getCurrentState() != PlayerStates.SEEKING
                            && stateMachine.getCurrentState() != PlayerStates.BUFFERING) {

                        ExoPlayerAdapter.this.stateMachine.transitionState(
                                PlayerStates.PAUSE, getPosition());


                        ViewerSessionEvent viewerSessionEvent = insightsReporter.getViewerSessionEvent();

                        if(viewerSessionEvent != null
                                && viewerSessionEvent.getEvent() != null
                                && previousEvent != null
                                && !endEventCalled
                                && !(viewerSessionEvent.getEvent().trim().equalsIgnoreCase(EVENT_SETUP)
                                || viewerSessionEvent.getEvent().trim().equalsIgnoreCase(EVENT_PLAYER_READY)
                                || viewerSessionEvent.getEvent().trim().equalsIgnoreCase(EVENT_PLAYBACK_READY))){


                            if(rebufferStarted){
                                callRebufferEnd();

                            }else{
                                insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
                                createAndCallSessionEvent(EVENT_PAUSE,eventTime);
                                playbackPaused = true;
                                playbackUpdateDelay = 500;

                                if(playbackUpdateEnabled) {
                                    disablePlaybackUpdate();
                                    playbackUpdateEnabled = false;
                                }
                            }
                        }

                    }else if(stateMachine.getCurrentState() == PlayerStates.PAUSE && !endEventCalled){

                        if(!insightsReporter.getViewerSessionEvent().getEvent().equalsIgnoreCase(EVENT_PAUSE)) {

                            insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
                            createAndCallSessionEvent(EVENT_PAUSE, eventTime);

                            playbackPaused = true;
                            playbackUpdateDelay = 500;

                            if (playbackUpdateEnabled) {
                                disablePlaybackUpdate();
                                playbackUpdateEnabled = false;
                            }
                        }
                    }
                }

            }

            @Override
            public void onVolumeChanged(@NonNull EventTime eventTime, float volume) {
                FCCLog.d(TAG, "onVolumeChanged");
            }

            @Override
            public void onDrmKeysRestored(@NonNull EventTime eventTime) {
                FCCLog.d(TAG, "onDrmKeysRestored");
            }

            @Override
            public void onDecoderDisabled(@NonNull EventTime eventTime, int trackType, @NonNull DecoderCounters decoderCounters) {
                FCCLog.d(TAG, "onDecoderDisabled");
            }

            @Override
            public void onShuffleModeChanged(@NonNull EventTime eventTime, boolean shuffleModeEnabled) {
                FCCLog.d(TAG, "onShuffleModeChanged");
            }

            @Override
            public void onDecoderInputFormatChanged(@NonNull EventTime eventTime, int trackType, @NonNull Format format) {
                FCCLog.d(TAG, "onDecoderInputFormatChanged");
            }

            @Override
            public void onAudioSessionId(@NonNull EventTime eventTime, int audioSessionId) {
                FCCLog.d(TAG, "onAudioSessionId");
            }

            @Override
            public void onVideoInputFormatChanged(@NonNull EventTime eventTime, @NonNull Format format) {

                FCCLog.e(TAG,"5CentsCDN_ANALYTICS - onVideoInputFormatChanged()");
                FCCLog.d(TAG, String.format("onVideoInputFormatChanged: Bitrate: %d", format.bitrate));
                try {
                    long videoTime = getPosition();
                    PlayerState<?> originalState = stateMachine.getCurrentState();

                    try {
                        if (stateMachine.getCurrentState() != PlayerStates.PLAYING) return;
                        if (!stateMachine.isQualityChangeEventEnabled()) return;
                        if (!bitrateEventDataManipulator.hasVideoFormatChanged(format)) return;


                        stateMachine.transitionState(PlayerStates.QUALITYCHANGE, videoTime);

                    } finally {
                        bitrateEventDataManipulator.setCurrentVideoFormat(format);
                    }
                    stateMachine.transitionState(originalState, videoTime);
                } catch (Exception e) {
                    FCCLog.d(TAG, e.getMessage(), e);
                }
            }

            @Override
            public void onSurfaceSizeChanged(@NonNull EventTime eventTime, int width, int height) {
                FCCLog.d(TAG, "onSurfaceSizeChanged");
            }

            @Override
            public void onAudioPositionAdvancing(@NonNull EventTime eventTime, long playoutStartSystemTimeMs) {
                FCCLog.d(TAG, "onAudioPositionAdvancing");
            }


            @Override
            public void onTracksChanged(@NonNull EventTime eventTime, @NonNull Tracks tracks) {
                super.onTracksChanged(eventTime, tracks);
                FCCLog.d(TAG, "onTracksChanged");
            }

            @Override
            public void onUpstreamDiscarded(@NonNull EventTime eventTime, @NonNull MediaLoadData mediaLoadData) {
                FCCLog.d(TAG, "onUpstreamDiscarded");
            }

            @Override
            public void onAudioDecoderInitialized(@NonNull EventTime eventTime, @NonNull String decoderName, long initializationDurationMs) {
                FCCLog.d(TAG, "onAudioDecoderInitialized");
            }

            @Override
            public void onLoadCanceled(@NonNull EventTime eventTime, @NonNull LoadEventInfo loadEventInfo, @NonNull MediaLoadData mediaLoadData) {
                FCCLog.d(TAG, "onLoadCanceled");
                fccdnAnalyticsConfig.getFccdnRequest().setPlayerError(true);
                FCCLog.d(TAG, "onLoadError set");
            }

            @Override
            public void onDecoderInitialized(@NonNull EventTime eventTime, int trackType, @NonNull String decoderName, long initializationDurationMs) {
                FCCLog.d(TAG, "onDecoderInitialized");
            }

            @Override
            public void onDroppedVideoFrames(@NonNull EventTime eventTime, int droppedFrames, long elapsedMs) {
                FCCLog.e(TAG,"onDroppedVideoFrames()");
                try {
                    ExoPlayerAdapter.this.totalDroppedVideoFrames += droppedFrames;
                } catch (Exception e) {
                    FCCLog.d(TAG, e.getMessage(), e);
                }
            }

            @Override
            public void onDecoderEnabled(@NonNull EventTime eventTime, int trackType, @NonNull DecoderCounters decoderCounters) {
                FCCLog.d(TAG, "onDecoderEnabled");
            }

            @Override
            public void onAudioUnderrun(@NonNull EventTime eventTime, int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
                FCCLog.d(TAG, "onAudioUnderrun");
            }

            @Override
            public void onMediaItemTransition(@NonNull EventTime eventTime, @Nullable MediaItem mediaItem, int reason) {
                FCCLog.d(TAG, "onMediaItemTransition");
                FCCLog.d(TAG, "onMediaItemTransition = EVENT_PLAYER_READY -> isEventSetup = "+insightsReporter.getViewerSessionEvent().isSetupEventDone());
                FCCLog.d(TAG, "onMediaItemTransition = eventTime.currentPlaybackPositionMs ="+eventTime.currentPlaybackPositionMs);

                if(insightsReporter.getViewerSessionEvent().isSetupEventDone()
                        && eventTime.currentPlaybackPositionMs == 0) {

                    insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
                    createAndCallSessionEvent(EVENT_PLAYER_READY,eventTime);
                    try {
                        Thread.sleep(300);
                    }catch (Exception ex){

                    }
                    insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
                    createAndCallSessionEvent(EVENT_PLAYBACK_READY,eventTime);

                }

            }

            @Override
            public void onLoadCompleted(@NonNull EventTime eventTime, @NonNull LoadEventInfo loadEventInfo,
                                        @NonNull MediaLoadData mediaLoadData) {


                try {
                    if (mediaLoadData.dataType == DATA_TYPE_MANIFEST) {
                        ExoPlayerAdapter.this.manifestUrl = loadEventInfo.dataSpec.uri.toString();
                    } else if (mediaLoadData.dataType == DATA_TYPE_MEDIA
                            && mediaLoadData.trackFormat != null
                            && mediaLoadData.trackFormat.drmInitData != null
                            && drmType == null) {
                        addDrmType(mediaLoadData);
                    }

                    if (mediaLoadData.trackFormat != null
                            && mediaLoadData.trackFormat.containerMimeType != null
                            && mediaLoadData.trackFormat.containerMimeType.startsWith("video")) {
                        addSpeedMeasurement(loadEventInfo);

                    }
                } catch (Exception e) {
                    FCCLog.d(TAG, e.getMessage(), e);
                }
            }
            // The timer that will report download bytes every 10 seconds.
            private final Handler handler = new Handler();
            private final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (!fccdnRequest.isPlayerError()){
                        playerInstancePresenter.pollEvery10Sec(fccdnRequest, ExoPlayerAdapter.this, fccdnRequest.getBytes());
                    }
                    if (fccdnAnalyticsConfig.getFccdnRequest().isShowCV() && !fccdnRequest.isPlayerError()){
                        playerInstancePresenter.postIfShowCV(fccdnAnalyticsConfig.getFccdnRequest(), fccdnAnalyticsConfig);
                    }
                    handler.postDelayed(this, 10000);
                }
            };

            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onPlayerStateChanged(@NonNull EventTime eventTime, boolean playWhenReady, int playbackState) {
                FCCLog.e(TAG,"onPlayerStateChanged()  --> playbackState = "+playbackState);

                // Start the timer when playback starts
                switch (playbackState) {
                    case Player.STATE_IDLE:
                    case Player.STATE_BUFFERING:
                    case Player.STATE_ENDED:
                        // Stop the timer when playback ends
                        // Stop the timer when buffering or idle
                        handler.removeCallbacks(runnable);
                        fccdnRequest.setPlaying(false);
                        break;
                    case Player.STATE_READY:
                        if (playWhenReady && !handler.hasCallbacks(runnable)) {
                            // Start the timer when playback starts
                            handler.postDelayed(runnable, 10000);
                            fccdnRequest.setPlaying(true);
                        } else if (!playWhenReady) {
                            // Stop the timer and methods runnable when playback is paused
                            handler.removeCallbacks(runnable);
                            fccdnRequest.setPlaying(false);
                        }
                        break;
                }

                if(PlaybackStats.PLAYBACK_STATE_JOINING_FOREGROUND == playbackState){

                    if(!playWhenReady && insightsReporter.getViewerSessionEvent().isSetupEventDone()){
                        FCCLog.e(TAG, "###### onPlayerStateChanged()-->EVENT_PLAY");
                        insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
                        createAndCallSessionEvent(EVENT_PLAY,eventTime);

                    }

                }else if(PlaybackStats.PLAYBACK_STATE_PLAYING == playbackState){
                    if(insightsReporter.getViewerSessionEvent().isSetupEventDone()
                            && exoplayer.isPlaying()){

                        if(playbackPaused){
                            FCCLog.e(TAG, "###### onPlayerStateChanged()-->EVENT_PLAY");
                            insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
                            createAndCallSessionEvent(EVENT_PLAY,eventTime);
                            playbackPaused = false;
                            adjustPauseBuffer = true;

                        }else if(rebufferStarted){

                            if(rebufferStarted){
                                callRebufferEnd();
                            }
                            if(!playbackUpdateEnabled) {
                                enablePlaybackUpdate();
                                playbackUpdateEnabled = true;
                            }
                        }else {
                            FCCLog.e(TAG, "###### onPlayerStateChanged()-->EVENT_PLAYING");
                            insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
                            createAndCallSessionEvent(EVENT_PLAYING, eventTime);
                        }
                    }

                }
            }


            @Override
            public void onDrmKeysRemoved(@NonNull EventTime eventTime) {
                FCCLog.d(TAG, "onDrmKeysRemoved");
            }

            @Override
            public void onMetadata(@NonNull EventTime eventTime, @NonNull Metadata metadata) {
                FCCLog.d(TAG, "onMetadata");
            }

            @Override
            public void onPlaybackParametersChanged(@NonNull EventTime eventTime, @NonNull PlaybackParameters playbackParameters) {
                FCCLog.d(TAG, "onPlaybackParametersChanged");
            }

            @Override
            public void onDownstreamFormatChanged(@NonNull EventTime eventTime, @NonNull MediaLoadData mediaLoadData) {
                FCCLog.d(TAG, "onDownstreamFormatChanged");
            }

            @Override
            public void onVideoDecoderInitialized(@NonNull EventTime eventTime, @NonNull String decoderName, long initializationDurationMs) {
                FCCLog.d(TAG, "onVideoDecoderInitialized");
            }

            @Override
            public void onRenderedFirstFrame(@NonNull EventTime eventTime, @Nullable Surface surface) {
                FCCLog.d(TAG, "onRenderedFirstFrame");
                playerIsReady = true;
            }

            @Override
            public void onRenderedFirstFrame(EventTime eventTime, Object output, long renderTimeMs) {
                FCCLog.e(TAG,"onRenderedFirstFrame() -- renderTimeMs = "+renderTimeMs);
                if(renderTimeMs > 0) {

                    if(playbackUpdateEnabled){
                        disablePlaybackUpdate();
                        playbackUpdateEnabled = false;
                    }else{
                        enablePlaybackUpdate();
                        playbackUpdateEnabled = true;
                    }

                }
            }

            @Override
            public void onBandwidthEstimate(@NonNull EventTime eventTime, int totalLoadTimeMs, long totalBytesLoaded, long bitrateEstimate) {
                FCCLog.d(TAG, "onBandwidthEstimate");
                Log.e(TAG, "onBandwidthEstimate: " + totalLoadTimeMs);
                Log.e(TAG, "onBandwidthEstimate: " + totalBytesLoaded);
                Log.e(TAG, "onBandwidthEstimate: " + bitrateEstimate);

            }



            @Override
            public void onAudioAttributesChanged(@NonNull EventTime eventTime, @NonNull AudioAttributes audioAttributes) {
                FCCLog.d(TAG, "onAudioAttributesChanged");
            }

            @Override
            public void onVideoEnabled(@NonNull EventTime eventTime, @NonNull DecoderCounters counters) {
                FCCLog.d(TAG, "onVideoEnabled");
            }

            @Override
            public void onDrmSessionAcquired(@NonNull EventTime eventTime) {

                FCCLog.d(TAG, "onDrmSessionAcquired");

                try {
                    drmLoadStartTime = eventTime.realtimeMs;
                    FCCLog.d(TAG, String.format("DRM Session aquired %d", eventTime.realtimeMs));
                } catch (Exception e) {
                    FCCLog.d(TAG, e.getMessage(), e);
                }
            }

            @Override
            public void onVideoDisabled(@NonNull EventTime eventTime, @NonNull DecoderCounters counters) {
                FCCLog.d(TAG, "onVideoDisabled");
            }

            @Override
            public void onAudioDisabled(@NonNull EventTime eventTime, @NonNull DecoderCounters counters) {
                FCCLog.d(TAG, "onAudioDisabled");
            }

            @Override
            public void onDrmSessionManagerError(@NonNull EventTime eventTime, @NonNull Exception error) {
                FCCLog.d(TAG, "onDrmSessionManagerError");
            }

            @Override
            public void onLoadStarted(@NonNull EventTime eventTime,
                                      @NonNull LoadEventInfo loadEventInfo,
                                      @NonNull MediaLoadData mediaLoadData) {
                FCCLog.d(TAG, "onLoadStarted");
            }

            @Override
            public void onPlayWhenReadyChanged(@NonNull EventTime eventTime, boolean playWhenReady, int reason) {

                FCCLog.e(TAG,"onPlayWhenReadyChanged()");
                FCCLog.d(TAG, String.format("onPlayWhenReadyChanged: %b, %d", playWhenReady, reason));
                // if player preload is setup without autoplay being enabled
                // this gets triggered after user clicks play

                if (ExoPlayerAdapter.this.isInInitialBufferState
                        && playWhenReady
                        && !stateMachine.isStartupFinished()) {
                    startup(getPosition());
                }
            }

            @Override
            public void onPositionDiscontinuity(@NonNull EventTime eventTime, int reason) {
                FCCLog.d(TAG, "onPositionDiscontinuity");
            }

            @Override
            public void onRepeatModeChanged(@NonNull EventTime eventTime, int repeatMode) {
                FCCLog.d(TAG, "onRepeatModeChanged");
            }

            @Override
            public void onDrmSessionReleased(@NonNull EventTime eventTime) {
                FCCLog.d(TAG, "onDrmSessionReleased");
            }

            @Override
            public void onTimelineChanged(@NonNull EventTime eventTime, int reason) {
                FCCLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onVideoFrameProcessingOffset(@NonNull EventTime eventTime, long totalProcessingOffsetUs, int frameCount) {
                FCCLog.d(TAG, "onVideoFrameProcessingOffset");
            }

            @Override
            public void onVideoSizeChanged(@NonNull EventTime eventTime,
                                           int width, int height,
                                           int unappliedRotationDegrees,
                                           float pixelWidthHeightRatio) {

                FCCLog.d(
                        TAG,
                        String.format(
                                "####### On Video Sized Changed: %d x %d Rotation Degrees: %d, PixelRation: %f",
                                width, height, unappliedRotationDegrees, pixelWidthHeightRatio));
            }

            @Override
            public void onAudioEnabled(@NonNull EventTime eventTime, @NonNull DecoderCounters counters) {
                FCCLog.d(TAG, "onAudioEnabled");
            }

            @Override
            public void onAudioInputFormatChanged(@NonNull EventTime eventTime, @NonNull Format format) {

                FCCLog.d(TAG, String.format("onAudioInputFormatChanged: Bitrate: %d", format.bitrate));
                try {
                    long videoTime = getPosition();
                    PlayerState<?> originalState = stateMachine.getCurrentState();
                    try {
                        if (stateMachine.getCurrentState() != PlayerStates.PLAYING) return;
                        if (!stateMachine.isQualityChangeEventEnabled()) return;
                        if (!bitrateEventDataManipulator.hasAudioFormatChanged(format)) return;
                        stateMachine.transitionState(PlayerStates.QUALITYCHANGE, videoTime);



                    } finally {
                        bitrateEventDataManipulator.setCurrentAudioFormat(format);
                    }
                    stateMachine.transitionState(originalState, videoTime);
                } catch (Exception e) {
                    FCCLog.d(TAG, e.getMessage(), e);
                }
            }

            @Override
            public void onLoadError(@NonNull EventTime eventTime, @NonNull LoadEventInfo loadEventInfo, @NonNull MediaLoadData mediaLoadData, @NonNull IOException error, boolean wasCanceled) {
                FCCLog.d(TAG, "onLoadError");
                fccdnAnalyticsConfig.getFccdnRequest().setPlayerError(true);
                FCCLog.d(TAG, "onLoadError set");
            }
        };
    }

    private void callEndEvent(AnalyticsListener.EventTime eventTime) {
        insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
        if(eventTime != null) {
            createAndCallSessionEvent(Events.EVENT_ENDED, eventTime);
        }else{
            createAndCallSessionEvent(Events.EVENT_ENDED, null);
        }
        disablePlaybackUpdate();
        endEventCalled = true;
    }

    private DefaultPlayerEventListener createPlayerEventListener(){

        return new DefaultPlayerEventListener() {
            @Override
            public void onPlaybackParametersChanged(@NonNull PlaybackParameters playbackParameters) {
                FCCLog.d(TAG, "onPlaybackParametersChanged()");
            }

            @Override
            public void onSeekProcessed() {
                FCCLog.d(TAG, "onSeekProcessed()");
            }


            @Override
            public void onTracksChanged(@NonNull Tracks tracks) {
                super.onTracksChanged(tracks);
                FCCLog.d(TAG, "onTracksChanged()");
            }

            @Override
            public void onIsLoadingChanged(boolean isLoading) {
                FCCLog.d(TAG, "onIsLoadingChanged()");
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                try {
                    FCCLog.d(TAG, "onPlayerError");
                    long videoTime = getPosition();
                    error.printStackTrace();
                    ErrorCode errorCode = exceptionMapper.map(error);
                    if (!stateMachine.isStartupFinished() && isVideoAttemptedPlay) {
                        stateMachine.setVideoStartFailedReason(VideoStartFailedReason.PLAYER_ERROR);
                    }
                    ExoPlayerAdapter.this.stateMachine.error(videoTime, errorCode);

                } catch (Exception e) {
                    FCCLog.d(TAG, e.getMessage(), e);
                }
            }

            @Override
            public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
                FCCLog.d(TAG, "onPlayWhenReadyChanged()");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                FCCLog.d(TAG, "onLoadingChanged()");
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                FCCLog.d(TAG, "onPositionDiscontinuity()");
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                FCCLog.d(TAG, "onRepeatModeChanged()");
            }

            @Override
            public void onPlaybackStateChanged(int state) {
                FCCLog.d(TAG, "onPlaybackStateChanged()");
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                FCCLog.d(TAG, "onShuffleModeEnabledChanged()");
            }

            @Override
            public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {
                FCCLog.d(TAG, "onPlaybackSuppressionReasonChanged()");
            }

            @Override
            public void onTimelineChanged(@NonNull Timeline timeline, int reason) {
                FCCLog.d(TAG, "onTimelineChanged()");
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                FCCLog.d(TAG, "onPlayerStateChanged()");
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                FCCLog.d(TAG, "onIsPlayingChanged()");
            }

            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                FCCLog.d(TAG, "onMediaItemTransition()");
            }
        };
    }

    private void addDrmType(MediaLoadData mediaLoadData) {
        String drmType = null;
        for (int i = 0;
                drmType == null && i < mediaLoadData.trackFormat.drmInitData.schemeDataCount;
                i++) {
            DrmInitData.SchemeData data = mediaLoadData.trackFormat.drmInitData.get(i);
            drmType = getDrmTypeFromSchemeData(data);
        }
        this.drmType = drmType;
    }

    private void addSpeedMeasurement(LoadEventInfo loadEventInfo) {
        SpeedMeasurement measurement = new SpeedMeasurement();
        measurement.setTimestamp(new Date());
        measurement.setDuration(loadEventInfo.loadDurationMs);
        measurement.setSize(loadEventInfo.bytesLoaded);
        meter.addMeasurement(measurement);
    }

    private String getDrmTypeFromSchemeData(DrmInitData.SchemeData data) {
        if (data == null) {
            return null;
        }

        String drmType = null;
        if (data.matches(WIDEVINE_UUID)) {
            drmType = DRMType.WIDEVINE.getValue();
        } else if (data.matches(CLEARKEY_UUID)) {
            drmType = DRMType.CLEARKEY.getValue();
        } else if (data.matches(PLAYREADY_UUID)) {
            drmType = DRMType.PLAYREADY.getValue();
        }
        return drmType;
    }

    @Override
    public void onSessionEventSuccess() {

    }

    @Override
    public void onSessionEventSuccess(String event) {

    }

    @Override
    public void onPlayerInitSuccess() {
    }

    private long millisFromPreviousEvent;
    private ViewerSessionEvent previousEvent;

    private void createAndCallSessionEvent(String event, AnalyticsListener.EventTime eventTime) {

        ViewerSessionEvent viewerSessionEvent = null;

        viewerSessionEvent = insightsReporter.getViewerSessionEvent();
        if(event != null && event.trim().equalsIgnoreCase(Events.EVENT_SETUP)) {
            previousEvent = null;
        }
        if(previousEvent != null){
            viewerSessionEvent.setPreviousEvent(previousEvent.getEvent());
        }

        viewerSessionEvent.setSessionId(ViewerSession.getInstance().getSessionId());
        viewerSessionEvent.setPropertyId(ViewerSession.getInstance().getPropertyId());
        viewerSessionEvent.setUserId(ViewerSession.getInstance().getUserId());
        viewerSessionEvent.setPlayerInstanceId(this.insightsReporter.getPlayerInstance().getPlayerInstanceId());
        viewerSessionEvent.setEvent(event);

        insightsReporter.setViewerSessionEvent(viewerSessionEvent);

        viewerSessionEvent.setCustomVideoId(fccdnAnalyticsConfig.getVideoId());
        viewerSessionEvent.setCustomVideoTitle(fccdnAnalyticsConfig.getTitle());



        if(exoplayer.getVideoSize() != null
                && ViewerSession.getInstance() != null
                && ViewerSession.getInstance().getMetaDeviceDisplayWidth() != null
                && exoplayer.getVideoSize().width != 0){

            int playerWidth = ViewerSession.getInstance().getMetaDeviceDisplayWidth();
            int videoWidth = exoplayer.getVideoSize().width;

            int difference = playerWidth - videoWidth;
            double scaling = (difference*1.0)/videoWidth;
            double upscale = 0;
            double downscale = 0;
            if(scaling>0) {
                upscale = scaling*100;
                downscale = 0;
            }else if(scaling < 0) {
                upscale = 0;
                downscale = scaling*(-1.0)*100;
            }else{
                upscale = 0 ;
                downscale = 0;
            }
            viewerSessionEvent.setVideoDownscalePercentage(downscale);
            viewerSessionEvent.setVideoUpscalePercentage(upscale);
        }


        if(stateMachine.getCurrentEventData() != null){

            EventData eventData = stateMachine.getCurrentEventData();
            viewerSessionEvent.setVideoSourceUrl(eventData.getM3u8Url());

            viewerSessionEvent.setEventDataTime(eventData.getTime());

            viewerSessionEvent.setFrom(eventData.getVideoTimeStart());
            viewerSessionEvent.setTo(eventData.getVideoTimeEnd());

            viewerSessionEvent.setBitrateMbps((float)eventData.getVideoBitrate());

            viewerSessionEvent.setCustomEncodingVariant(eventData.getVideoCodec());
            viewerSessionEvent.setMuted(eventData.isMuted());
            viewerSessionEvent.setCasting(eventData.isCasting());
            viewerSessionEvent.setCustomVideoLanguage(eventData.getLanguage());

            if(!TextUtils.isEmpty(eventData.getM3u8Url())) {
                viewerSessionEvent.setVideoSourceUrl(eventData.getM3u8Url());
            }else if(!TextUtils.isEmpty(eventData.getMpdUrl())){
                viewerSessionEvent.setVideoSourceUrl(eventData.getMpdUrl());
            }
            viewerSessionEvent.setCustomVideoTitle(eventData.getVideoTitle());
            viewerSessionEvent.setFullscreen(false);

        }else{
            viewerSessionEvent.setPlaybackTimeInstantMillis(0l);
        }

        //Exoplayer Event
        if(!viewerSessionEvent.getEvent().trim().equalsIgnoreCase(Events.EVENT_SETUP)) {
            viewerSessionEvent.setVideoWidthPixels(exoplayer.getVideoSize().width);
            viewerSessionEvent.setVideoHeightPixels(exoplayer.getVideoSize().height);
            viewerSessionEvent.setPlaybackTimeInstantMillis(exoplayer.getCurrentPosition());
            viewerSessionEvent.setCustomVideoDurationMillis(exoplayer.getDuration() < 0 ? 0 : exoplayer.getDuration());
            viewerSessionEvent.setVideoTotalDurationMillis(exoplayer.getDuration() < 0 ? 0 : exoplayer.getDuration());
            if (exoplayer.getCurrentMediaItem() != null && exoplayer.getCurrentMediaItem().playbackProperties != null) {
                viewerSessionEvent.setVideoSourceUrl("" + exoplayer.getCurrentMediaItem().playbackProperties.uri);
            } else {
                viewerSessionEvent.setVideoSourceUrl("");
            }
        }else{
            viewerSessionEvent.setVideoWidthPixels(0);
            viewerSessionEvent.setVideoHeightPixels(0);
            viewerSessionEvent.setPlaybackTimeInstantMillis(0l);
            viewerSessionEvent.setCustomVideoDurationMillis(0l);
            viewerSessionEvent.setVideoTotalDurationMillis(0l);
            viewerSessionEvent.setVideoSourceUrl("");
        }

        millisFromPreviousEvent = viewerSessionEvent.getPlaybackTimeInstantMillis();

        if(previousEvent != null) {

            if(eventTime != null && eventTime.currentPlaybackPositionMs>0 && exoplayer.isPlaying()){
                long millisFromPreviousEvent = viewerSessionEvent.getPlaybackTimeInstantMillis() - previousEvent.getPlaybackTimeInstantMillis();

                if(millisFromPreviousEvent == 0L){
                    millisFromPreviousEvent = viewerSessionEvent.getTimestamp() - previousEvent.getTimestamp();
                }
                viewerSessionEvent.setMillisFromPreviousEvent(millisFromPreviousEvent);

            }else {
                long millisFromPreviousEvent = viewerSessionEvent.getTimestamp() - previousEvent.getTimestamp();
                viewerSessionEvent.setMillisFromPreviousEvent(millisFromPreviousEvent);
            }
        }else{

            if(millisFromPreviousEvent !=0){
                viewerSessionEvent.setMillisFromPreviousEvent(viewerSessionEvent.getTimestamp() - millisFromPreviousEvent);
            }else {
                viewerSessionEvent.setMillisFromPreviousEvent(0L);
            }
        }



        if(viewerSessionEvent.getEvent().trim().equalsIgnoreCase(Events.EVENT_SEEKED)){
            viewerSessionEvent.setFrom(seekStarted);
            viewerSessionEvent.setTo(seekProcessed);

        }if(previousEvent != null && previousEvent.getEvent().trim().equalsIgnoreCase(Events.EVENT_SEEKED)){
            seekStarted = 0L;
            seekProcessed = 0L;

            if(stateMachine.getCurrentEventData() != null){
                FCCLog.e(TAG,"PREVIOUS_EVENT - EVENT_SEEKED");
                FCCLog.e(TAG,"PREVIOUS_EVENT - EVENT_SEEKED  - FROM "+stateMachine.getCurrentEventData().getVideoTimeStart());
                FCCLog.e(TAG,"PREVIOUS_EVENT - EVENT_SEEKED  - TO "+stateMachine.getCurrentEventData().getVideoTimeEnd());
                FCCLog.e(TAG,"PREVIOUS_EVENT - MILLIS FROM PREVIOUS EVENT "+viewerSessionEvent.getMillisFromPreviousEvent());
                viewerSessionEvent.setFrom(stateMachine.getCurrentEventData().getVideoTimeStart());
                viewerSessionEvent.setTo(stateMachine.getCurrentEventData().getVideoTimeEnd());
            }
        }else{
            if(stateMachine.getCurrentEventData() != null){
                viewerSessionEvent.setFrom(stateMachine.getCurrentEventData().getVideoTimeStart());
                viewerSessionEvent.setTo(stateMachine.getCurrentEventData().getVideoTimeEnd());
            }
        }


        if(this.fccdnAnalyticsConfig.getVideoMetadata() != null){
            VideoMetadata metadata = this.fccdnAnalyticsConfig.getVideoMetadata();

            viewerSessionEvent.setCustomContentType(metadata.getCustomContentTye());
            viewerSessionEvent.setCustomVideoDurationMillis(metadata.getCustomVideoDurationMillis());
            viewerSessionEvent.setCustomEncodingVariant(metadata.getCustomEncodingVariant());
            viewerSessionEvent.setCustomVideoLanguage(metadata.getCustomVideoLanguage());
            viewerSessionEvent.setCustomVideoId(metadata.getCustomVideoId());
            viewerSessionEvent.setCustomVideoSeries(metadata.getCustomVideoSeries());
            viewerSessionEvent.setCustomVideoProducer(metadata.getCustomVideoProducer());
            viewerSessionEvent.setCustomVideoTitle(metadata.getCustomVideoTitle());
            viewerSessionEvent.setCustomVideoVariantName(metadata.getCustomVideoVariantName());
            viewerSessionEvent.setCustomVideoVariant(metadata.getCustomVideoVariant());

        }

        insightsReporter.setViewerSessionEvent(viewerSessionEvent);

        if(NetworkUtil.isNetworkAvailable(fccdnAnalyticsConfig.getContext()) && NetworkUtil.isDataAvailable(fccdnAnalyticsConfig.getContext())) {

//            viewerSessionEventPresenter.sessionEvent(insightsReporter.getViewerSessionEvent(), ExoPlayerAdapter.this);

        }

        previousEvent = insightsReporter.getViewerSessionEvent().clone();
        millisFromPreviousEvent = Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public void onPlayerEventSuccess(String requestId) {

    }

    @Override
    public void onPlayerAttached() {
        FCCLog.d(TAG,"onPlayerAttached()");
        insightsReporter.setViewerSessionEvent(null);

        ViewerSessionEvent viewerSessionEvent = null;
        insightsReporter.resetPlaybackId();
        viewerSessionEvent = insightsReporter.getViewerSessionEvent();

        if(viewerSessionEvent != null && !viewerSessionEvent.isSetupEventDone()) {

            viewerSessionEvent.setSessionId(ViewerSession.getInstance().getSessionId());
            viewerSessionEvent.setPropertyId(ViewerSession.getInstance().getPropertyId());
            viewerSessionEvent.setUserId(ViewerSession.getInstance().getUserId());
            viewerSessionEvent.setTimestamp(Calendar.getInstance().getTimeInMillis());

            if(this.getCurrentSourceMetadata()!= null && !TextUtils.isEmpty(this.getCurrentSourceMetadata().getM3u8Url())) {
                viewerSessionEvent.setVideoSourceUrl(this.getCurrentSourceMetadata().getM3u8Url());
            }else if(this.getCurrentSourceMetadata()!= null && !TextUtils.isEmpty(this.getCurrentSourceMetadata().getMpdUrl())){
                viewerSessionEvent.setVideoSourceUrl(this.getCurrentSourceMetadata().getMpdUrl());
            }else{
                viewerSessionEvent.setVideoSourceUrl("");
            }
            insightsReporter.setViewerSessionEvent(viewerSessionEvent);
            insightsReporter.getViewerSessionEvent().setSetupEventDone(true);

            previousEvent = null;
            millisFromPreviousEvent = 0;
            playbackStarted = false;
            playbackPaused = false;
            adjustPauseBuffer = false;
            rebufferStarted = false;
            endEventCalled = false;
            stateMachine.resetStateMachine();
            stateMachine.setCurrentEventData(null);
            stateMachine.setAnalyticsCallback(this);
            stateMachine.setRebufferListener(this);

            if(fccdnAnalyticsConfig.getPlaybackUpdateHandler() != null) {
                disablePlaybackUpdate();
            }

            createAndCallSessionEvent(EVENT_SETUP,null);


            try {
                Thread.sleep(500);
            }catch (Exception ex){

            }
        }

    }

    @Override
    public void onPlayerDetached() {
        FCCLog.d(TAG,"onPlayerDetached()");
    }

    @Override
    public void onPlayerStartup(EventData data) {
        FCCLog.d(TAG,"onPlayerStartup()");
    }

    @Override
    public void onPlayerPauseExit(EventData data) {
        FCCLog.d(TAG,"onPlayerPauseExit()");
    }

    @Override
    public void onPlayerPlayExit(EventData data) {
        FCCLog.d(TAG,"onPlayerPlayExit()");
    }

    @Override
    public void onPlayerRebuffering(EventData data) {
        FCCLog.d(TAG,"onPlayerRebuffering()");
    }

    @Override
    public void onPlayerError(EventData data) {
        FCCLog.d(TAG,"onPlayerError()");
    }

    @Override
    public void onPlayerSeekCompleted(EventData data) {
        FCCLog.d(TAG,"onPlayerSeekCompleted()");
    }

    @Override
    public void onPlayerHeartBeat(EventData data) {
        FCCLog.d(TAG,"onPlayerHeartBeat()");
    }

    @Override
    public void onBufferingStarted(EventData data) {
        FCCLog.d(TAG,"onBufferingStarted()");
    }

    @Override
    public void onBufferingEnd(EventData data) {
        FCCLog.d(TAG,"onBufferingEnd()");
    }



    private long playbackUpdateDelay = 100;
    private boolean playbackUpdateEnabled = false;


    private Runnable playbackRunnable = new Runnable() {
        @Override
        public void run() {
            playbackUpdateEnabled = true;
            updatePlayBack();
            fccdnAnalyticsConfig.getPlaybackUpdateHandler().postDelayed(this, playbackUpdateDelay);
        }
    };

    private void enablePlaybackUpdate(){
        fccdnAnalyticsConfig.getPlaybackUpdateHandler().postDelayed(playbackRunnable,playbackUpdateDelay);
    }

    private void disablePlaybackUpdate(){
        fccdnAnalyticsConfig.getPlaybackUpdateHandler().removeCallbacksAndMessages(null);
        playbackUpdateEnabled = false;
    }

    private boolean playbackStarted;
    private boolean playbackPaused;
    private boolean adjustPauseBuffer;
    private boolean endEventCalled;

    private void updatePlayBack() {

        if(exoplayer.isPlaying()) {

            if(exoplayer.getCurrentPosition() != 0 && !playbackStarted){
                ViewerSessionEvent viewerSessionEvent = insightsReporter.getViewerSessionEvent();
                viewerSessionEvent.setTimestamp(Calendar.getInstance().getTimeInMillis());
                createAndCallSessionEvent(Events.EVENT_PLAYBACK_STARTED, null);

                playbackUpdateDelay = 3000;
                playbackStarted = true;

            }else if(playbackStarted
                    && rebufferStarted
                    && exoplayer.getCurrentPosition() != 0){

                callRebufferEnd();

                if(!playbackUpdateEnabled) {
                    enablePlaybackUpdate();
                    playbackUpdateEnabled = true;
                }

            }else if(playbackStarted && !playbackPaused && adjustPauseBuffer && exoplayer.getCurrentPosition() != 0){

                adjustPauseBuffer = false;
                insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
                createAndCallSessionEvent(EVENT_PLAYING,null);
                playbackUpdateDelay = 3000;

            } else if(exoplayer.getCurrentPosition() != 0 && playbackStarted) {
                ViewerSessionEvent viewerSessionEvent = insightsReporter.getViewerSessionEvent();

                viewerSessionEvent.setTimestamp(Calendar.getInstance().getTimeInMillis());
                createAndCallSessionEvent(EVENT_PLAYBACK_UPDATE, null);

            }
        }else if(!exoplayer.isPlaying() && !exoplayer.getPlayWhenReady()
                && !playbackPaused
                && playbackStarted
                && previousEvent != null
                && exoplayer.getCurrentPosition() == previousEvent.getPlaybackTimeInstantMillis()){

            ViewerSessionEvent viewerSessionEvent = insightsReporter.getViewerSessionEvent();

            if(playbackStarted && viewerSessionEvent != null
                    && viewerSessionEvent.getEvent() != null
                    && previousEvent != null
                    && !(viewerSessionEvent.getEvent().trim().equalsIgnoreCase(EVENT_SETUP)
                    || viewerSessionEvent.getEvent().trim().equalsIgnoreCase(EVENT_PLAYER_READY)
                    || viewerSessionEvent.getEvent().trim().equalsIgnoreCase(EVENT_PLAYBACK_READY))){

                insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
                createAndCallSessionEvent(EVENT_PAUSE,null);
                playbackPaused = true;
                playbackUpdateDelay = 500;

                if(playbackUpdateEnabled) {
                    disablePlaybackUpdate();
                    playbackUpdateEnabled = false;
                }

            }
        }else if(playbackStarted
                && !exoplayer.isPlaying()
                && !endEventCalled
                && previousEvent != null
                && playbackUpdateEnabled){

            if(exoplayer.getCurrentPosition() == exoplayer.getDuration()
                    && exoplayer.getDuration() != 0
                    && exoplayer.getCurrentPosition()!=0){
                callEndEvent(null);
            }
        }
    }

    private void callRebufferEnd() {
        FCCLog.e(TAG, "callRebufferEnd()");

        insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
        createAndCallSessionEvent(EVENT_REBUFFER_END, null);
        rebufferStarted = false;
        playbackUpdateDelay = 3000;

    }


    @Override
    public void reBufferStarted() {
        FCCLog.e(TAG,"reBufferStarted()-->event state ="+stateMachine.getCurrentState());

        if(!rebufferStarted) {
            insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
            createAndCallSessionEvent(EVENT_REBUFFER_START, null);
            rebufferStarted = true;

        }
    }

    @Override
    public void reBufferStopped() {
        FCCLog.e(TAG,"reBufferStopped()-->event state ="+stateMachine.getCurrentState());
        if(rebufferStarted) {
            insightsReporter.getViewerSessionEvent().setTimestamp(Calendar.getInstance().getTimeInMillis());
            createAndCallSessionEvent(EVENT_REBUFFER_END,null);
            rebufferStarted = false;
        }
    }

}
