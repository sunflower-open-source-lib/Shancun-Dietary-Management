package com.example.administrator.newfridge.okhttp;

import android.os.Handler;
import android.util.Log;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author LG32
 * 2018/12/10
 * get请求方法获得冰箱id
 */
public class GetBoxIdRequest {
    private static final String TAG = "GetBoxIdRequest";
    private static final String KEY = "getBoxId";

    public GetBoxIdRequest( Handler handler, String cookie){
        Log.i ( TAG, "开始冰箱id请求！" );
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
