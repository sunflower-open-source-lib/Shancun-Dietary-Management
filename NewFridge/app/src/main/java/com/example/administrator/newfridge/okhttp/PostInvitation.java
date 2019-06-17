package com.example.administrator.newfridge.okhttp;

import android.os.Handler;
import android.util.Log;

import okhttp3.RequestBody;

public class PostInvitation {

    private static final String TAG = "PostInvitation";
    private static final String KEY = "invitation";

    public PostInvitation(RequestBody requestBody, Handler myHandler, String cookie) {
        Log.i ( TAG, "开始添加家庭组请求！" );
        new HttpRequest ( requestBody, KEY, myHandler, cookie );
    }
}
