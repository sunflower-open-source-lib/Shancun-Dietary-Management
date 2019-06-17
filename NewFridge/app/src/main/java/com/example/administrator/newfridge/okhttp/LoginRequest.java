package com.example.administrator.newfridge.okhttp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.administrator.newfridge.view.activity.LoginActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//18846833759/123456
public class LoginRequest{
    private static final String TAG = "LoginRequest";
    private static final String KEY = "login";

    public LoginRequest(RequestBody requestBody, Handler loginHandler, SharedPreferences sharedPreferences ){
        Log.i ( TAG, "开始登陆请求！" );
        new HttpRequest ( requestBody, KEY, loginHandler, sharedPreferences );
    }

}
