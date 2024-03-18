package com.fivecentscdnanalytics.calls.api;


import android.text.TextUtils;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

public class RequestUrlCreator {

    public static URL buildURL(String baseUrl , Map<String,String> requestParamsMap){

        URL requestUrl = null;
        try{
            requestUrl =  new URL(baseUrl+"?"+createParams(requestParamsMap));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return requestUrl;
    }

    private static String createParams(Map<String,String> reqParams){
        Set<String> keySet = reqParams.keySet();
        StringBuilder urlParams = new StringBuilder();
        try {

            boolean first = true;
            for (String key : keySet) {
                if(TextUtils.isEmpty(key) || TextUtils.isEmpty(reqParams.get(key))){
                    continue;
                }
                urlParams.append(first ? "" : "&");
                urlParams.append(URLEncoder.encode(key, "UTF-8")).append("=").append(URLEncoder.encode(reqParams.get(key), "UTF-8"));

                first = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlParams.toString();
    }
}
