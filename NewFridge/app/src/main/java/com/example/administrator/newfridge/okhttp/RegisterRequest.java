package com.example.administrator.newfridge.okhttp;

import android.os.Handler;
import android.util.Log;
import okhttp3.RequestBody;

public class RegisterRequest {

    private static final String TAG = "RegisterRequest";
    private static final String KEY = "register";

    public RegisterRequest(RequestBody requestBody, Handler loginHandler){
        Log.i ( TAG, "开始注册请求！" );
        new HttpRequest ( requestBody, KEY, loginHandler );
    }

}
