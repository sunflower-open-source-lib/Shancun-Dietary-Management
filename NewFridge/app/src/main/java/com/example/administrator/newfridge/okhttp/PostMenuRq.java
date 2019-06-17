package com.example.administrator.newfridge.okhttp;

import android.os.Handler;
import android.util.Log;

import okhttp3.RequestBody;

public class PostMenuRq {


    private static final String TAG = "PostMenuRq";
    private static final String KEY = "menus";

    public PostMenuRq(RequestBody requestBody, Handler myHandler){
        Log.i ( TAG, "开始注册请求！" );
        new HttpRequest ( requestBody, KEY, myHandler );
    }
}
