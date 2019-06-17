package com.example.administrator.newfridge.okhttp;

import android.os.Handler;
import android.util.Log;

import okhttp3.RequestBody;

public class PostDelectIceB {
    private static final String TAG = "PostDelectIceB";
    private static final String KEY = "delectIce";

    public PostDelectIceB(RequestBody requestBody, Handler myHandler, String cookie) {
        Log.i ( TAG, "开始删除冰箱请求！" );
        new HttpRequest ( requestBody, KEY, myHandler, cookie );
    }
}
