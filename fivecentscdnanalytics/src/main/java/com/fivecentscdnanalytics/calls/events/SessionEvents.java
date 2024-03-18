package com.fivecentscdnanalytics.calls.events;

public interface SessionEvents extends BaseEvent{
    void onSessionEventSuccess();
    void onSessionEventSuccess(String event);
}
