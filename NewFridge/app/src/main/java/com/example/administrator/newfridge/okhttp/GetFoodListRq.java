package com.example.administrator.newfridge.okhttp;

import android.os.Handler;
import android.util.Log;

import okhttp3.RequestBody;

public class GetFoodListRq {

    private static final String TAG = "GetFoodListRq";
    private static final String KEY = "getFoodList";

    public GetFoodListRq(RequestBody requestBody, Handler handler, String cookie){
        Log.i ( TAG, "开始食物列表请求！" );
        new HttpRequest ( requestBody, KEY, handler, cookie );
    }
}
