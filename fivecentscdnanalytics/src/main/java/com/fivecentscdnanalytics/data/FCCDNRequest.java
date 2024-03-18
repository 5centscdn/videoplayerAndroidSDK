package com.fivecentscdnanalytics.data;

import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

public class FCCDNRequest {
    @SerializedName("hash_id")
    String hashID;
    @SerializedName("url")
    String url;
    @SerializedName("title")
    String title;
    @SerializedName("tags")
    String[] tags;
    @SerializedName("showcv")
    boolean showCV;
    @SerializedName("viewer_id")
    String viewerID;
    @SerializedName("view_id")
    String viewID;
    @SerializedName("userAgent")
    String userAgent;
    @SerializedName("type")
    String requestType;
    @SerializedName("referrer")
    String referrer;
    @SerializedName("value1")
    long valueOne;
    @SerializedName("value2")
    long valueTwo;

    long playerInitStartTime, playerInitEndTime, bufferStartTime, bufferEndTime, bytes;

    boolean playerError, isPlaying;

    TextView logTV;
    boolean isLog;


    public FCCDNRequest() {
    }

    public FCCDNRequest(String hashID, String url, String title, String[] tags, boolean showCV, String viewerID, String viewID, String userAgent, String requestType,
                        String referrer, long valueOne, long valueTwo) {
        this.hashID = hashID;
        this.url = url;
        this.title = title;
        this.tags = tags;
        this.showCV = showCV;
        this.viewerID = viewerID;
        this.viewID = viewID;
        this.userAgent = userAgent;
        this.requestType = requestType;
        this.referrer = referrer;
        this.valueOne = valueOne;
        this.valueTwo = valueTwo;
    }

    public TextView getLogTV() {
        return logTV;
    }

    public void setLogTV(TextView logTV) {
        this.logTV = logTV;
    }

    public boolean isLog() {
        return isLog;
    }

    public void setLog(boolean log) {
        isLog = log;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isPlayerError() {
        return playerError;
    }

    public void setPlayerError(boolean playerError) {
        this.playerError = playerError;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public String getHashID() {
        return hashID;
    }

    public void setHashID(String hashID) {
        this.hashID = hashID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JsonArray getTags() {
        JsonArray tagsArray = new JsonArray();
        for (String tag : tags) {
            tagsArray.add(tag);
        }
        return tagsArray;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public boolean isShowCV() {
        return showCV;
    }

    public void setShowCV(boolean showCV) {
        this.showCV = showCV;
    }

    public String getViewerID() {
        return viewerID;
    }

    public void setViewerID(String viewerID) {
        this.viewerID = viewerID;
    }

    public String getViewID() {
        return viewID;
    }

    public void setViewID(String viewID) {
        this.viewID = viewID;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public long getValueOne() {
        return valueOne;
    }

    public void setValueOne(long valueOne) {
        this.valueOne = valueOne;
    }

    public long getValueTwo() {
        return valueTwo;
    }

    public void setValueTwo(long valueTwo) {
        this.valueTwo = valueTwo;
    }

    public long getPlayerInitStartTime() {
        return playerInitStartTime;
    }

    public void setPlayerInitStartTime(long playerInitStartTime) {
        this.playerInitStartTime = playerInitStartTime;
    }

    public long getPlayerInitEndTime() {
        return playerInitEndTime;
    }

    public void setPlayerInitEndTime(long playerInitEndTime) {
        this.playerInitEndTime = playerInitEndTime;
    }

    public long getBufferStartTime() {
        return bufferStartTime;
    }

    public void setBufferStartTime(long bufferStartTime) {
        this.bufferStartTime = bufferStartTime;
    }

    public long getBufferEndTime() {
        return bufferEndTime;
    }

    public void setBufferEndTime(long bufferEndTime) {
        this.bufferEndTime = bufferEndTime;
    }
    public long getInitTime(){
        return playerInitEndTime - playerInitStartTime;
    }
    public long getBufferTime(){
        return bufferEndTime - bufferStartTime;
    }
}
