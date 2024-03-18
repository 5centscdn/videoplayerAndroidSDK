package com.fivecentscdnanalytics.calls.api;


import com.fivecentscdnanalytics.data.ViewerSessionEvent;
import com.fivecentscdnanalytics.enums.Events;

public class ApiParameterBasicStability {

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static Boolean isValidPropertyId(String propertyId){
        if(isNullOrEmpty(propertyId)){
            return false;
        }
        return true;
    }

    public static Boolean isValidIdsProvidedInApiCall(ViewerSessionEvent viewerSessionEvent){

        if(isNullOrEmpty(viewerSessionEvent.getEventId())
                || isNullOrEmpty(viewerSessionEvent.getPlaybackId())
                || isNullOrEmpty(viewerSessionEvent.getSessionId())
                || isNullOrEmpty(viewerSessionEvent.getPlayerInstanceId())
                || isNullOrEmpty(viewerSessionEvent.getUserId())){
            return false;
        }
        return true;
    }

    public static Boolean timingWithEventAndPreviousEventCorrect(ViewerSessionEvent viewerSessionEvent){

        if(isNullOrEmpty(viewerSessionEvent.getEvent())
            ||(viewerSessionEvent.getEvent().equalsIgnoreCase(Events.EVENT_SETUP) && !isNullOrEmpty(viewerSessionEvent.getPreviousEvent()))
            ||(viewerSessionEvent.getEvent().equalsIgnoreCase(Events.EVENT_SETUP) && viewerSessionEvent.getMillisFromPreviousEvent() != 0)
            || ((viewerSessionEvent.getEvent().equalsIgnoreCase(Events.EVENT_SETUP)
                || viewerSessionEvent.getEvent().equalsIgnoreCase(Events.EVENT_PLAYER_READY)
                || viewerSessionEvent.getEvent().equalsIgnoreCase(Events.EVENT_PLAYBACK_READY))
                && viewerSessionEvent.getPlaybackTimeInstantMillis() != 0)
            || viewerSessionEvent.getTimestamp() == 0){

            return false;

        }

        return true;
    }

    public static Boolean scalingIsCorrect(ViewerSessionEvent viewerSessionEvent){
        
        if(viewerSessionEvent.getEvent().equalsIgnoreCase(Events.EVENT_SETUP)
                || viewerSessionEvent.getEvent().equalsIgnoreCase(Events.EVENT_PLAYER_READY)
                || viewerSessionEvent.getEvent().equalsIgnoreCase(Events.EVENT_PLAYBACK_READY)){
            return true;
        }else if(viewerSessionEvent.getVideoUpscalePercentage() == 0
                && viewerSessionEvent.getVideoDownscalePercentage() == 0){
            return false;
        }
        return true;
    }

    public static Boolean seekIsCorrect(ViewerSessionEvent viewerSessionEvent){

        if(viewerSessionEvent.getEvent().equalsIgnoreCase(Events.EVENT_SEEKED)
            && viewerSessionEvent.getFrom() == 0 && viewerSessionEvent.getTo() == 0){
            return false;
        }
        return true;
    }


    public static Boolean rebufferIsCorrect(ViewerSessionEvent viewerSessionEvent){

        if(viewerSessionEvent.getEvent().equalsIgnoreCase(Events.EVENT_REBUFFER_END)
                && viewerSessionEvent.getMillisFromPreviousEvent() == 0){
            return false;
        }
        return true;
    }


}
