package com.fivecentscdnanalytics.stateMachines;

import android.os.CountDownTimer;
import android.os.Handler;
import com.fivecentscdnanalytics.FCCDNAnalytics;
import com.fivecentscdnanalytics.config.FCCDNAnalyticsConfig;
import com.fivecentscdnanalytics.calls.AnalyticsCallback;
import com.fivecentscdnanalytics.calls.RebufferListener;
import com.fivecentscdnanalytics.data.ErrorCode;
import com.fivecentscdnanalytics.data.EventData;
import com.fivecentscdnanalytics.enums.AnalyticsErrorCodes;
import com.fivecentscdnanalytics.enums.VideoStartFailedReason;
import com.fivecentscdnanalytics.utils.FCCLog;
import com.fivecentscdnanalytics.utils.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerStateMachine {
    private static final String TAG = "PlayerStateMachine";
    private final FCCDNAnalyticsConfig config;
    private List<StateMachineListener> listeners = new ArrayList<StateMachineListener>();
    private PlayerState<?> currentState;
    private PlayerState<?> previousState;
    private EventData currentEventData;

    private long elapsedTimeOnEnter = 0;
    private long startupTime = 0;
    // Setting a playerStartupTime of 1 to workaround dashboard issue (only for the
    // first startup sample, in case the collector supports multiple sources)
    private long playerStartupTime = 1L;
    private boolean startupFinished = false;
    private long elapsedTimeSeekStart = 0;
    private long videoTimeStart;
    private long videoTimeEnd;
    private String impressionId;
    private Handler heartbeatHandler = new Handler();
    private int currentRebufferingIntervalIndex = 0;
    private static List<Integer> rebufferingIntervals =
            Arrays.asList(3000, 5000, 10000, 30000, 59700);
    private int heartbeatDelay = 59700; // default to 60 seconds
    private final FCCDNAnalytics analytics;
    private VideoStartFailedReason videoStartFailedReason;
    private int qualityChangeCount = 0;
    protected boolean isQualityChangeTimerRunning = false;

    private AnalyticsCallback analyticsCallback;
    private RebufferListener rebufferListener;


    public AnalyticsCallback getAnalyticsCallback() {
        return analyticsCallback;
    }

    public void setAnalyticsCallback(AnalyticsCallback analyticsCallback) {
        this.analyticsCallback = analyticsCallback;

        if(this.analyticsCallback != null){
            this.analytics.setAnalyticsCallback(this.analyticsCallback);
        }
    }

    public FCCDNAnalytics getAnalytics() {
        return analytics;
    }

    public PlayerStateMachine(FCCDNAnalyticsConfig config, FCCDNAnalytics analytics) {
        this.config = config;
        this.analytics = analytics;
        this.heartbeatDelay = this.config.getHeartbeatInterval();
        resetStateMachine();
    }

    public void enableHeartbeat() {
        heartbeatHandler.postDelayed(
                new Runnable() {
                    public void run() {
                        triggerHeartbeat();
                        heartbeatHandler.postDelayed(this, heartbeatDelay);
                    }
                },
                heartbeatDelay);
    }

    public void disableHeartbeat() {
        heartbeatHandler.removeCallbacksAndMessages(null);
    }

    public void enableRebufferHeartbeat() {
        heartbeatHandler.postDelayed(
                new Runnable() {
                    public void run() {
                        triggerHeartbeat();
                        currentRebufferingIntervalIndex =
                                Math.min(
                                        currentRebufferingIntervalIndex + 1,
                                        rebufferingIntervals.size() - 1);
                        heartbeatHandler.postDelayed(
                                this, rebufferingIntervals.get(currentRebufferingIntervalIndex));
                    }
                },
                rebufferingIntervals.get(currentRebufferingIntervalIndex));
    }

    public void disableRebufferHeartbeat() {
        currentRebufferingIntervalIndex = 0;
        heartbeatHandler.removeCallbacksAndMessages(null);
    }

    private void triggerHeartbeat() {
        long elapsedTime = Util.getElapsedTime();
        videoTimeEnd = analytics.getPosition();
        for (StateMachineListener listener : getListeners()) {
            listener.onHeartbeat(elapsedTime - elapsedTimeOnEnter);
        }
        elapsedTimeOnEnter = elapsedTime;
        videoTimeStart = videoTimeEnd;
    }

    private void resetSourceRelatedState() {
        disableHeartbeat();
        disableRebufferHeartbeat();
        this.impressionId = Util.getUUID();
        this.videoStartFailedReason = null;
        startupTime = 0;
        startupFinished = false;
        videoStartTimeout.cancel();
        qualityChangeResetTimeout.cancel();
        rebufferingTimeout.cancel();
        resetQualityChangeCount();
        analytics.resetSourceRelatedState();
    }

    public void resetStateMachine() {
        resetSourceRelatedState();
        setCurrentState(PlayerStates.READY);
        setPreviousState(null);
    }

    public void sourceChange(long oldVideoTime, long newVideoTime, boolean shouldStartup) {
        transitionState(PlayerStates.SOURCE_CHANGED, oldVideoTime, null);
        resetSourceRelatedState();

        if (shouldStartup) {
            transitionState(PlayerStates.STARTUP, newVideoTime, null);
        }
    }

    public synchronized <T> void transitionState(
            PlayerState<T> destinationPlayerState, long videoTime) {
        transitionState(destinationPlayerState, videoTime, null);
    }

    public synchronized <T> void transitionState(
            PlayerState<T> destinationPlayerState, long videoTime, T data) {
        if (!this.isTransitionAllowed(currentState, destinationPlayerState)) {
            return;
        }

        long elapsedTime = Util.getElapsedTime();
        videoTimeEnd = videoTime;

       FCCLog.d(
                TAG,
                "Transitioning from "
                        + currentState.toString()
                        + " to "
                        + destinationPlayerState.toString());

        currentState.onExitState(this, elapsedTime, destinationPlayerState);

        if(currentState!= PlayerStates.BUFFERING
                && destinationPlayerState == PlayerStates.BUFFERING){

            if(rebufferListener != null){
                rebufferListener.reBufferStarted();
            }
        }else if(currentState == PlayerStates.BUFFERING
                && destinationPlayerState != PlayerStates.BUFFERING){

            if(rebufferListener != null){
                rebufferListener.reBufferStopped();
            }
        }
        this.elapsedTimeOnEnter = elapsedTime;
        videoTimeStart = videoTimeEnd;
        setPreviousState(this.currentState);
        destinationPlayerState.onEnterState(this, data);
        setCurrentState(destinationPlayerState);
    }

    public boolean isTransitionAllowed(PlayerState<?> currentState, PlayerState<?> destination) {
        if (destination == this.currentState) {
            return false;
        } else if (this.currentState == PlayerStates.EXITBEFOREVIDEOSTART) {
            return false;
        }
        // no state transitions like PLAYING or PAUSE during AD
        else if (currentState == PlayerStates.AD
                && (destination != PlayerStates.ERROR && destination != PlayerStates.ADFINISHED)) {
            return false;
        } else if (currentState == PlayerStates.READY
                && (destination != PlayerStates.ERROR
                        && destination != PlayerStates.EXITBEFOREVIDEOSTART
                        && destination != PlayerStates.STARTUP
                        && destination != PlayerStates.AD)) {
            return false;
        }

        return true;
    }

    public void error(long videoTime, ErrorCode errorCode) {
        transitionState(PlayerStates.ERROR, videoTime, errorCode);
    }

    public boolean isStartupFinished() {
        return startupFinished;
    }

    public void setStartupFinished(boolean startupFinished) {
        this.startupFinished = startupFinished;
    }

    public void addListener(StateMachineListener toAdd) {
        listeners.add(toAdd);
    }

    public void removeListener(StateMachineListener toRemove) {
        listeners.remove(toRemove);
    }

    List<StateMachineListener> getListeners() {
        return listeners;
    }

    public PlayerState<?> getCurrentState() {
        return currentState;
    }

    private void setCurrentState(final PlayerState<?> newPlayerState) {
        this.currentState = newPlayerState;
    }

    private void setPreviousState(final PlayerState<?> oldState) {
        this.previousState = oldState;
    }

    public PlayerState<?> getPreviousState() {
        return previousState;
    }

    public long getStartupTime() {
        return startupTime;
    }

    public void addStartupTime(long elapsedTime) {
        this.startupTime += elapsedTime;
    }

    public String getImpressionId() {
        return impressionId;
    }

    public long getVideoTimeStart() {
        return videoTimeStart;
    }

    public long getVideoTimeEnd() {
        return videoTimeEnd;
    }

    public long getElapsedTimeOnEnter() {
        return elapsedTimeOnEnter;
    }

    public long getElapsedTimeSeekStart() {
        return elapsedTimeSeekStart;
    }

    public long getAndResetPlayerStartupTime() {
        long playerStartupTime = this.playerStartupTime;
        this.playerStartupTime = 0;
        return playerStartupTime;
    }

    public void setElapsedTimeSeekStart(long elapsedTimeSeekStart) {
        this.elapsedTimeSeekStart = elapsedTimeSeekStart;
    }

    public VideoStartFailedReason getVideoStartFailedReason() {
        return videoStartFailedReason;
    }

    public void setVideoStartFailedReason(VideoStartFailedReason videoStartFailedReason) {
        this.videoStartFailedReason = videoStartFailedReason;
    }

    protected CountDownTimer videoStartTimeout =
            new CountDownTimer(Util.VIDEOSTART_TIMEOUT, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {}

                @Override
                public void onFinish() {
                   FCCLog.d(TAG, "VideoStartTimeout finish");
                    setVideoStartFailedReason(VideoStartFailedReason.TIMEOUT);
                    transitionState(PlayerStates.EXITBEFOREVIDEOSTART, 0, null);
                }
            };

    public void pause(long position) {
        if (isStartupFinished()) {
            transitionState(PlayerStates.PAUSE, position);
        } else {
            transitionState(PlayerStates.READY, position);
        }
    }

    public void startAd(long position) {
        transitionState(PlayerStates.AD, position);
        startupTime = 0;
    }

    public boolean isQualityChangeEventEnabled() {
        return this.qualityChangeCount <= Util.ANALYTICS_QUALITY_CHANGE_COUNT_THRESHOLD;
    }

    public void increaseQualityChangeCount() {
        this.qualityChangeCount++;
    }

    protected void resetQualityChangeCount() {
        this.qualityChangeCount = 0;
    }


    public EventData getCurrentEventData() {
        return currentEventData;
    }

    public void setCurrentEventData(EventData currentEventData) {
        this.currentEventData = currentEventData;
    }

    protected CountDownTimer qualityChangeResetTimeout =
            new CountDownTimer(Util.ANALYTICS_QUALITY_CHANGE_COUNT_RESET_INTERVAL, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    isQualityChangeTimerRunning = true;
                }

                @Override
                public void onFinish() {
                   FCCLog.d(TAG, "qualityChangeResetTimeout finish");
                    resetQualityChangeCount();
                    isQualityChangeTimerRunning = false;
                }
            };

    protected CountDownTimer rebufferingTimeout =
            new CountDownTimer(Util.REBUFFERING_TIMEOUT, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {}

                @Override
                public void onFinish() {
                   FCCLog.d(TAG, "rebufferingTimeout finish");
                    error(
                            analytics.getPosition(),
                            AnalyticsErrorCodes.ANALYTICS_BUFFERING_TIMEOUT_REACHED.getErrorCode());
                    disableRebufferHeartbeat();
                    resetStateMachine();
                }
            };


    public RebufferListener getRebufferListener() {
        return rebufferListener;
    }

    public void setRebufferListener(RebufferListener rebufferListener) {
        this.rebufferListener = rebufferListener;
    }

}
