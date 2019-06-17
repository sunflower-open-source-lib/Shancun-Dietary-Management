package com.example.administrator.newfridge.okhttp;

import android.os.Handler;
import android.util.Log;

import okhttp3.RequestBody;

public class GetIdenRequest {
    private static final String TAG = "GetIdenRequest";
    private static final String KEY = "getiden";

    public GetIdenRequest(RequestBody requestBody, Handler handler){
        Log.i ( TAG, "开始验证码请求！" );
        new HttpRequest ( requestBody, KEY, handler );
    }
}
