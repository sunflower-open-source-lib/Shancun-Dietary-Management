package com.example.administrator.newfridge.okhttp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * @author RollingZ
 * cookie类，单例模式
 */
public class GetClientCookie {

    private static volatile OkHttpClient okHttpClient;
    private static final String TAG = "cookie";
    private static final String JSESSIONID = "JSESSIONID";


    public GetClientCookie(){

    }

    static public OkHttpClient getHttpClient(final SharedPreferences sharedPreferences){

        if( okHttpClient == null ){
            okHttpClient = new OkHttpClient.Builder()
                    .cookieJar ( new CookieJar () {
                        private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<> ();

                        @SuppressLint("CommitPrefEdits")
                        @Override
                        public void saveFromResponse( HttpUrl url, List<Cookie> cookies ) {
                            cookieStore.put(url, cookies);
                            cookieStore.put(HttpUrl.parse("http://47.95.247.236:8080/login/"), cookies);
                            for (Cookie cookie : cookies) {
                                System.out.println("cookie Name:" + cookie.name() + cookie.value ());
                                System.out.println("cookie Path:" + cookie.path ());
                                SharedPreferences.Editor editor = sharedPreferences.edit ();
                                editor.putString ( "cookie", cookie.toString () );
                                editor.putString ( JSESSIONID, cookie.value () );
                                editor.apply ();
                            }
                            Log.i ( TAG, "saveFromResponse: " + "保存cookie" );
                        }

                        @Override
                        public List<Cookie> loadForRequest(HttpUrl url) {
                            List<Cookie> cookies = cookieStore.get(HttpUrl.parse("http://47.95.247.236:8080/login/"));
                            if (cookies == null) {
                                System.out.println("没加载到内存cookie");
                                String cookieStr= sharedPreferences.getString ( JSESSIONID, "" );
                                if (cookieStr.equals ( "" )){
                                    Log.i ( TAG, "loadForRequest: 加载到本地文件的cookie" );

                                }
                            }else
                                Log.i ( TAG, "loadForRequest: " + "加载到cookie" );
                            return cookies != null ? cookies : new ArrayList<Cookie> ();
                        }
                    })
                    .build();
        }
        return okHttpClient;
    }
}