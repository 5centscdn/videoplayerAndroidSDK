package com.fivecentscdnanalytics.data;

import com.google.gson.annotations.SerializedName;

public class ShowCVResponse {
    @SerializedName("count")
    int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
