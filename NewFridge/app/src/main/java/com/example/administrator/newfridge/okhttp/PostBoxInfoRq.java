package com.example.administrator.newfridge.okhttp;

import android.os.Handler;
import android.util.Log;

import okhttp3.RequestBody;

public class PostBoxInfoRq {

    private static final String TAG = "PostBoxInfoRq";
    private static final String KEY = "iceBoxInfo";

    public PostBoxInfoRq(RequestBody requestBody, Handler myHandler){
        Log.i ( TAG, "开始获取冰箱信息！" );
        new HttpRequest ( requestBody, KEY, myHandler );
    }
}
