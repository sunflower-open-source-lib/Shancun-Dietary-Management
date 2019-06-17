package com.example.administrator.newfridge.okhttp;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.administrator.newfridge.tool.MsgManager;
import com.example.administrator.newfridge.tool.MyHandlerMsg;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


class HttpRequest implements MyHandlerMsg{

    private RequestBody requestBody;
    private String key;
    private String url;
    private String TAG;
    private String cookie;
    private int msgNum;
    private Handler myHandler;
    private UrlMap urlMap = UrlMap .getUrlMap ();
    private String result;
    private SharedPreferences sharedPreferences;


    /**
     * @author LG32
     * 对于有返回cookie，不携带cookie的post请求
     * @param requestBody 请求体
     * @param key 获取url的key
     * @param myHandler 页面的handler对象
     * @param sharedPreferences 读取本地信息
     */
    HttpRequest(RequestBody requestBody, String key, Handler myHandler, SharedPreferences sharedPreferences){
        this.requestBody = requestBody;
        this.myHandler = myHandler;
        this.key = key;
        this.sharedPreferences = sharedPreferences;

        TAG = key + "Request";
        Log.i ( TAG, "PostRequest key: " + key );
        setUrl ();
        setMsg();
        noCookiePostRequest ();
    }

    /**
     * @author LG32
     * 没cookie的post请求
     * @param requestBody 请求体
     * @param key 获取url的key
     * @param myHandler 页面的handler对象
     */
    HttpRequest( RequestBody requestBody, String key, Handler myHandler ){
        this.requestBody = requestBody;
        this.myHandler = myHandler;
        this.key = key;

        TAG = key + "Request";
        Log.i ( TAG, "PostRequest key: " + key );
        setUrl ();
        setMsg();
        noCookieRequest ();
    }

    /**
     * @author LG32
     * 带cookie post请求
     * @param requestBody 请求体
     * @param key 获取url的key
     * @param myHandler 页面的handler对象
     * @param cookie 用户cookie
     */
    HttpRequest(RequestBody requestBody, String key, Handler myHandler, String cookie){
        this.myHandler = myHandler;
        this.key = key;
        this.cookie = cookie;
        this.requestBody = requestBody;

        TAG = key + "Request";
        Log.i ( TAG, "PostRequest key: " + key );
        setUrl ();
        setMsg();
        cookiePostRequest ();
    }

    /**
     * @author LG32
     * get方法自定义参数
     * @param request 自定义请求参数
     * @param key 获取url的key
     * @param myHandler 页面的handler对象
     */
    HttpRequest( Request request, String key, Handler myHandler ){
        this.myHandler = myHandler;
        this.key = key;

        TAG = key + "Request";
        Log.i ( TAG, "PostRequest key: " + key );
        setUrl ();
        setMsg();
        getRequest (request);
    }

    private void setUrl(){
        url = urlMap.getUrl ( key );
        Log.i ( TAG, "set url" + url );
    }

    private void setMsg(){
        MsgManager msgManager = new MsgManager ();
        msgNum = msgManager.getMsg ( key );
    }

    /**
     * @date 2019/2/4
     * @author LG32
     * @param request get请求体
     * get请求
     */
    private void getRequest(Request request){

        OkHttpClient client = new OkHttpClient ();
        HttpRequestThread httpRequestThread = new HttpRequestThread ( client, request );
        httpRequestThread.start ();

    }

    /**
     * 登陆
     */
    private void noCookiePostRequest(){

        OkHttpClient client = GetClientCookie.getHttpClient (sharedPreferences);

        Request request = new Request.Builder()
                .url(url)
                .post ( requestBody )
                .build();

        HttpRequestThread httpRequestThread  = new HttpRequestThread ( client, request );
        httpRequestThread.start();
    }

    private void noCookieRequest(){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post ( requestBody )
                .build();

        HttpRequestThread httpRequestThread  = new HttpRequestThread ( client, request );
        httpRequestThread.start();
    }

    private void cookiePostRequest(){

        OkHttpClient client = new OkHttpClient ();

        Request request = new Request.Builder()
                .url(url)
                .header ( "Cookie", cookie )
                .post ( requestBody )
                .build();

        HttpRequestThread httpRequestThread  = new HttpRequestThread ( client, request );
        httpRequestThread.start();
    }

    private class HttpRequestThread extends Thread{

        private  OkHttpClient client;

        private Request request;

        HttpRequestThread(OkHttpClient client, Request request){
            this.client = client;
            this.request = request;
        }

        public void run(){

            Log.i ( TAG, "http request thread run! " );

            Call call = client.newCall( request );
            call.enqueue(new Callback () {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i ( TAG, "onFailure: 网络请求失败" );
                    Message msg = new Message ();
                    msg.what = REQUEST_FAIL;
                    myHandler.sendMessage ( msg );
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        result = response.body().string();
                        Log.i ( TAG, "返回值" + result );
                        Message msg = new Message ();
                        msg.what = msgNum;
                        msg.obj = result;
                        myHandler.sendMessage ( msg );
                    }else {
                        result = response.body ().string ();
                        Log.i ( TAG, "错误为" + result );
                    }
                }
            });
        }
    }

}
