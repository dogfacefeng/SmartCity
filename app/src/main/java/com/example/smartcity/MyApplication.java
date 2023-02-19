package com.example.smartcity;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;

public class MyApplication  extends Application {
    private static Context context;
    private  static  Gson gson;

    public static String getMyUrl() {
        return "http://61.171.62.134:10001/prod-api";
    }

    public static Gson getGson() {
        return gson;
    }

    public static String getToken() {
        return "eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjEwOTM2NjM2LWRiNGEtNGIzZC04NjZmLWI2Y2VlMWRiMjg2YSJ9.mlJz-" +
                "O6oIoX3r1WjYol3svdffYv3hykG9AimhTrN3yaF9zEMHDFkjG0wfhDU4KsiCbfXCNBB5LDJWiamIgYwMw";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context= getApplicationContext();
        gson  = new Gson();
    }

    public static Context getContext(){
        return  context;
    }
}
