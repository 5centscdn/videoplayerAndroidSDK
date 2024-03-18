package com.fivecentscdnanalytics.utils;

import android.util.Log;
import com.fivecentscdnanalytics.calls.api.ApiConfig;

public class FCCLog {

    public static void e(String tag,String message){

        if(ApiConfig.PRODUCTION_ENV){
            return;
        }
        Log.e(tag,message);

    }

    public static void e(String tag,String message,Throwable e){

        if(ApiConfig.PRODUCTION_ENV){
            return;
        }
        Log.e(tag,message,e);

    }

    public static void d(String tag,String message){
        if(ApiConfig.PRODUCTION_ENV){
            return;
        }
       Log.d(tag,message);
    }

    public static void d(String tag,String message,Throwable e){
        if(ApiConfig.PRODUCTION_ENV){
            return;
        }
       Log.d(tag,message,e);
    }
}
