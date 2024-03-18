package com.fivecentscdnanalytics.utils;

import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class UserAgentGenerator {
    Context context;

    public UserAgentGenerator(Context context) {
        this.context = context;
    }

    public String generateUserAgent() {
        WebView webView = new WebView(context);
        WebSettings settings = webView.getSettings();
        String userAgent = settings.getUserAgentString();
        webView.destroy();
        return userAgent;
    }
}
