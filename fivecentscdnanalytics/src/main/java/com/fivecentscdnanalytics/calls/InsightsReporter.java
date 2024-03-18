package com.fivecentscdnanalytics.calls;

import android.content.Context;
import android.os.Handler;

import com.fivecentscdnanalytics.data.PlayerInstance;
import com.fivecentscdnanalytics.data.PlayerNetworkRequest;
import com.fivecentscdnanalytics.data.ViewerSession;
import com.fivecentscdnanalytics.data.ViewerSessionEvent;

public class InsightsReporter {
    private ViewerSession viewerSession;
    private PlayerInstance playerInstance;
    private ViewerSessionEvent viewerSessionEvent;
    private PlayerNetworkRequest playerNetworkRequest;
    private Handler playbackUpdateHandler = new Handler();


    private String propertyId;
    private Context context;

    private static InsightsReporter insightsReporter;
    public static InsightsReporter getInstance(String propertyId,Context context){

        if(insightsReporter == null){
            insightsReporter = new InsightsReporter(propertyId,context);
        }
        return insightsReporter;
    }

    private InsightsReporter(String propertyId, Context context){
        this.propertyId = propertyId;
        this.context = context;
    }
    public ViewerSession getViewerSession() {
        return viewerSession;
    }

    public void setViewerSession(ViewerSession viewerSession) {
        this.viewerSession = viewerSession;
    }

    public PlayerInstance getPlayerInstance() {
        return playerInstance;
    }

    public void setPlayerInstance(PlayerInstance playerInstance) {
        this.playerInstance = playerInstance;
    }

    public ViewerSessionEvent getViewerSessionEvent() {
        return viewerSessionEvent;
    }

    public void setViewerSessionEvent(ViewerSessionEvent viewerSessionEvent) {
        this.viewerSessionEvent = viewerSessionEvent;
    }

    public PlayerNetworkRequest getPlayerNetworkRequest() {
        return playerNetworkRequest;
    }

    public void setPlayerNetworkRequest(PlayerNetworkRequest playerNetworkRequest) {
        this.playerNetworkRequest = playerNetworkRequest;
    }


    public void resetPlaybackId(){
        viewerSessionEvent = new ViewerSessionEvent(this.context);
        viewerSessionEvent.setPropertyId(this.propertyId);
        viewerSessionEvent.setSetupEventDone(false);
        setViewerSessionEvent(viewerSessionEvent);
    }


    public Handler getPlaybackUpdateHandler() {
        return playbackUpdateHandler;
    }
}
