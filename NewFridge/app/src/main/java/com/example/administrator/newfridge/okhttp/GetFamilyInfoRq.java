package com.example.administrator.newfridge.okhttp;

import android.os.Handler;
import android.util.Log;

import okhttp3.Request;

/**
 * @author LG32
 * @date 2018/12/12
 * Get方法请求家庭组信息
 */
public class GetFamilyInfoRq {

    private static final String TAG = "GetFamilyInfoRq";
    private static final String KEY = "getFamilyInfo";

    public GetFamilyInfoRq(Handler handler, String cookie){
        Log.i ( TAG, "开始家庭组信息请求！" );
        UrlMap urlMap = UrlMap.getUrlMap ();
        String url = urlMap.getUrl ( KEY );
        Request request = new Request.Builder ()
                .url ( url )
                .header ( "Cookie", cookie )
                .get ()
                .build ();

        new HttpRequest ( request, KEY, handler);
    }

}
