package com.example.administrator.newfridge.okhttp;

import java.util.HashMap;
import java.util.Map;

public class UrlMap {

    private Map<String, String> urlMap;

    private static UrlMap url;

    //    private String urlHead = "http://47.95.247.236:8080";
//    private String urlHead = "http://192.168.199.100:8080";
//    private String urlHead = " http://lg32web.natapp1.cc";
    private String urlHead = "http://www.gavinwang.cn:8080";

    private UrlMap() {
        urlMap = new HashMap<> ();
        initMap ();
    }

    public static UrlMap getUrlMap(){
        if (url == null) {
            url = new UrlMap ();
            return url;
        }
        else
            return url;
    }

    private void initMap() {
        urlMap.put ( "login", urlHead + "/login/" );
        urlMap.put ( "getiden", urlHead + "/validations/getiden" );
        urlMap.put ( "register", urlHead + "/validations/register" );
        urlMap.put ( "getBoxId", urlHead + "/iceboxes/getmyicebox" );
        urlMap.put ( "getFoodList", urlHead + "/foods/getallfoodlist/" );
        urlMap.put ( "getFamilyInfo", urlHead + "/families/getFamilyInfo" );
        urlMap.put ( "createFamily", urlHead + "/families/createfamily/" );
        urlMap.put ( "createIce", urlHead + "/iceboxes/createicebox" );
        urlMap.put ( "delectIce", urlHead + "/iceboxes/delect" );
        urlMap.put ( "outFamily", urlHead + "/families/outFamily" );
        urlMap.put ( "menus", urlHead + "/menus/" );
        urlMap.put ( "iceBoxInfo", urlHead + "/geticeboxinfo" );
        urlMap.put ( "invitation", urlHead + "/families/invitation" );
        urlMap.put ( "connectBox", urlHead + "/iceboxes/connectbox" );
    }

    public String getUrl(String key) {
        return urlMap.get ( key );
    }
}
