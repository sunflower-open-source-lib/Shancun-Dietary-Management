package com.example.administrator.newfridge.okhttp;

import android.os.Handler;
import android.util.Log;

import okhttp3.Request;

/**
 * @author LG32
 * 2019/2/4
 * get请求方法创建新家庭组
 */
public class GetCreateFamilyRq {

    private static final String TAG = "GetCreateFamilyRq";
    private static final String KEY = "createFamily";

    public GetCreateFamilyRq(Handler handler, String cookie, String newFamilyName){
        Log.i ( TAG, "开始创建新家庭组请求！" + newFamilyName );
        UrlMap urlMap = UrlMap.getUrlMap ();
        String url = urlMap.getUrl ( KEY );
        String requestUrl = url + newFamilyName;
        Log.i ( TAG, "GetCreateFamilyRq: " + requestUrl );
        Request request = new Request.Builder ()
                .url ( requestUrl )
                .header ( "Cookie", cookie )
                .get ()
                .build ();

        new HttpRequest ( request, KEY, handler);
    }
}
