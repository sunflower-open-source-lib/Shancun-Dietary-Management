package com.example.administrator.newfridge.okhttp;

import android.os.Handler;
import android.util.Log;

import okhttp3.RequestBody;

public class PostCreateIceBoxRq {

    private static final String TAG = "CreateIceRequest";
    private static final String KEY = "createIce";

    public PostCreateIceBoxRq(RequestBody requestBody, Handler myHandler, String cookie){
        Log.i ( TAG, "开始注册请求！" );
        new HttpRequest ( requestBody, KEY, myHandler, cookie );
    }
}
