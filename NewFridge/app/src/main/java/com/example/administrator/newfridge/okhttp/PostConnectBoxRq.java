package com.example.administrator.newfridge.okhttp;

import android.os.Handler;
import android.util.Log;

import okhttp3.RequestBody;

public class PostConnectBoxRq {

    private static final String TAG = "PostConnectBoxRq";
    private static final String KEY = "connectBox";

    public PostConnectBoxRq(RequestBody requestBody, Handler myHandler, String cookie){
        Log.i ( TAG, "开始注册请求！" );
        new HttpRequest ( requestBody, KEY, myHandler, cookie );
    }
}
