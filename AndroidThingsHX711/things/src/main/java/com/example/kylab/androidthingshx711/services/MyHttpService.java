package com.example.kylab.androidthingshx711.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.kylab.androidthingshx711.tools.Material;
import com.example.kylab.androidthingshx711.tools.MaterialLab;
import com.example.kylab.androidthingshx711.tools.Family;
import com.example.kylab.androidthingshx711.tools.MenuDetail;
import com.example.kylab.androidthingshx711.tools.familydetail;
import com.example.kylab.androidthingshx711.tools.foodMenu;
import com.example.kylab.androidthingshx711.tools.fooddetail;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class MyHttpService extends Service {
    public static final int LOAD_DATA = 1;
    public static final int LOAD_ONE_DATA = 2;
    public static final int LOAD_FAMILY = 3;
    public static final int DELETE_FOOD = 4;
    public static final int LOAD_MENU_DATA = 5;
    private String httpurl = "http://www.gavinwang.cn:8080/";
    private Material mMaterial = new Material();
    private Family family = new Family();
    private boolean threadDisable = false;
    List<foodMenu> mFoodMenus = new ArrayList<foodMenu>();
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case LOAD_DATA:
                    Log.d("handler", "handleMessage:data loading is done");
                    Toast.makeText(getApplicationContext(), "data are down", Toast.LENGTH_SHORT).show();
                    break;
                case LOAD_ONE_DATA:
                    Log.d("handler", "handleMessage:data loading is done");
                    Toast.makeText(getApplicationContext(), "one data is down", Toast.LENGTH_SHORT).show();
                    if (!threadDisable) {
                        Intent intent = new Intent();
                        intent.putExtra("material", mMaterial);
                        intent.setAction("com.example.kylab.androidthingshx711.services.MyHttpService");
                        sendBroadcast(intent);
                    }
                    break;
                case LOAD_FAMILY:
                    Log.d("handler", "handleMessage:data loading is done");
                    Toast.makeText(getApplicationContext(), "Family data is down", Toast.LENGTH_SHORT).show();
                    if (!threadDisable) {
                        Intent intent = new Intent();
                        intent.putExtra("Family", family);
                        intent.setAction("com.example.kylab.androidthingshx711.services.MyHttpService");
                        sendBroadcast(intent);
                    }
                    break;
                case DELETE_FOOD:
                    Log.d("handler", "handleMessage:data loading is done");
                    Toast.makeText(getApplicationContext(), "food delete/add is down", Toast.LENGTH_SHORT).show();
                    Log.d("handler", "handleMessage:delete or add mseeage is"+msg.obj.toString());
                    //未处理，可把msg.obj传到显示界面显示删除情况
                    break;
                case LOAD_MENU_DATA:
                    Log.d("handler", "handleMessage:data loading is done");
                    Toast.makeText(getApplicationContext(), "Family data is down", Toast.LENGTH_SHORT).show();
                    if (!threadDisable) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Menu",(Serializable)mFoodMenus);
                        intent.putExtras(bundle);
                        intent.setAction("com.example.kylab.androidthingshx711.services.MyHttpService");
                        sendBroadcast(intent);
                    }
                    break;
                default:
                    break;

            }
        }
    };
    public MyHttpService() {
    }
    private LoadfoodBinder mBinder = new LoadfoodBinder();
    public class LoadfoodBinder extends Binder {

        public void startloaddetail(final String uuid){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        RequestBody requestBody = new FormBody.Builder()
                                .add("uuid",uuid).build();
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(httpurl+"foods/getfood")
                                .post(requestBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        String data = response.body().string();
                        showOneResponse(data);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
            Log.d(TAG, "startloaddetail: executed");
        }
        public void startDownload(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        RequestBody requestBody = new FormBody.Builder()
                                .add("macip","89f3218e64df440aa2a556fd2e5aabc1").build();
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(httpurl+"/foods/getallfoodlist")
                                .post(requestBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        String data = response.body().string();
                        showResponse(data);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
            Log.d(TAG, "startDownload: executed");

        }
        public void deleteFood(final String foodUuid){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        RequestBody requestBody = new FormBody.Builder()
                                .add("macip","89f3218e64df440aa2a556fd2e5aabc1")
                                .add("uuid",foodUuid)
                                .build();
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(httpurl+"outfood/")
                                .post(requestBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        String data = response.body().string();

                        showDeleteMes(data);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
            Log.d(TAG, "startDelete: executed");

        }
        public int  getProess(){
            Log.d(TAG, "getProess: executed");
                        return 0;
                    }
        public void startuploadfood(final Material material){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{

                        RequestBody requestBody = new FormBody.Builder()
                                .add("macip","89f3218e64df440aa2a556fd2e5aabc1")
                                .add("foodName",material.getName())
                                .add("uuid",material.getId())//material.getId()
                                .add("comment",material.getMcomment())
                                .add("foodTime",material.getMtime())
                                .add("type",material.getMtype())
                                .add("opFlag","1")
                                .add("opDate",material.getMstartTime())
                                .add("foodParent","2")
                                .add("foodPhoto","null")
                                .add("foodWeight",material.getMweight())
                                .add("foodPercent",material.getMpercent())
                                .add("taretWeight",material.getTareweight())
                                .build();
                        Log.d(TAG, "run: taretWeight"+material.getTareweight());
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(httpurl+"foods/putFoodDataIn")
                                .post(requestBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        String data = response.body().string();
                        showDeleteMes(data);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
            Log.d(TAG, "startloaddetail: executed");
        }
        public void downloadFamily(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        RequestBody requestBody = new FormBody.Builder()
                                .add("macip","89f3218e64df440aa2a556fd2e5aabc1").build();
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(httpurl+"families/getfamily")
                                .post(requestBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        String data = response.body().string();
                        showFamilyResponse(data);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
            Log.d(TAG, "downloadFamily: executed");

        }
        public void startLoadMenu(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        RequestBody requestBody = new FormBody.Builder()
                                .add("macip","89f3218e64df440aa2a556fd2e5aabc1").build();
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(httpurl+"menus/")
                                .post(requestBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        String data = response.body().string();
                        showMenuResponse(data);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
            Log.d(TAG, "startLoadMenu: executed");

        }

    }

    private void showResponse(final String data) {
        Log.d("mainActivity", data);
        Gson gson = new Gson();
        fooddetail foodobject = gson.fromJson(data,fooddetail.class);
        List<fooddetail.UserBean> userbeans = foodobject.getData();
            MaterialLab.get(getApplicationContext()).cleanMaterial();
            for(fooddetail.UserBean food:userbeans){
                //  btyeToImg(food);
                Material material = new Material();
                material.setId(food.getFoodId());
                material.setName(food.getFoodname());
                material.setMcomment(food.getComment());
                material.setMpercent(food.getPercent());
                material.setMstartTime(food.getStartTime());
                material.setMtime(food.getTime());
                material.setMtype(food.getType());
                material.setMweight(food.getWeight());
                material.setPhotoUrl(food.getFoodPhotoUrl());
                material.setUrl(food.getFoodurl());
                material.setTareweight(food.getTareWeight());
                MaterialLab.get(getApplicationContext()).addMaterial(material);
                Log.d(TAG, "!!!!!!!!!!!!!!!!!!"+material.getName());

            }


        mHandler.sendEmptyMessage(LOAD_DATA);
    }
    private void showMenuResponse(final String data) {
        mFoodMenus.clear();
        Log.d("mainActivity", data);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        MenuDetail menuobject = gson.fromJson(data,MenuDetail.class);
        //String menubeans = gson.fromJson(Smenubean,String.class);
       // Log.d(TAG, "showMenuResponse: menubeans"+gson.fromJson(Smenubean,String.class));
        List<String> Smenubeans = menuobject.getData();
        for(String sm:Smenubeans){
            try {
                MenuDetail.menuBean menubeans = gson.fromJson(sm, MenuDetail.menuBean.class);
                String temp = "";
                foodMenu foodmenu = new foodMenu();
                foodmenu.setMenuname(menubeans.getName());
                foodmenu.setMenuimg(menubeans.getImage());
                List<String> mate = menubeans.getRecipeIngredient();
                for (String i : mate) {
                    temp = temp + " " + i;
                }
                foodmenu.setFmaterial(temp);
                foodmenu.setDescribtion(menubeans.getRecipeInstructions());
                mFoodMenus.add(foodmenu);
            }catch (JsonSyntaxException je){
                je.printStackTrace();
            }
        }



        mHandler.sendEmptyMessage(LOAD_MENU_DATA);
    }
    private void showFamilyResponse(final String data) {
        Log.d("mainActivity", data);
        String normalMem = "";
        Gson gson = new Gson();
        familydetail familyobject = gson.fromJson(data,familydetail.class);
        family.setFid("http://gavinwang.cn/ice.jpg");
        List<familydetail.FamilyBean.member> members = familyobject.getData().getUser();
        for(familydetail.FamilyBean.member mem:members){
            Log.d(TAG, "showFamilyResponse: mem.gatrole = "+mem.getRole());
            if(mem.getRole().equals("1")){
                family.setManager(mem.getUserName());
            }else{
                normalMem = normalMem + "  "+mem.getUserName();
            }
        }
        family.setCommon(normalMem);
        mHandler.sendEmptyMessage(LOAD_FAMILY);
    }
    private void showDeleteMes(final String data) {
        Log.d("showDeleteMes", data);
        String mes ="——";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
            mes = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Message message = new Message();
        message.obj = mes;
        message.what = DELETE_FOOD;
        mHandler.sendMessage(message);
    }
    private void showOneResponse(final String data) {
        Log.d("mainActivityOneResponse", data);
        Gson gson = new Gson();
        fooddetail foodobject = gson.fromJson(data,fooddetail.class);
        List<fooddetail.UserBean> userbeans = foodobject.getData();
        for(fooddetail.UserBean food:userbeans){
            mMaterial.setId(food.getFoodId());
            mMaterial.setName(food.getFoodname());
            mMaterial.setMcomment(food.getComment());
            mMaterial.setMpercent(food.getPercent());
            mMaterial.setMstartTime(food.getStartTime());
            mMaterial.setMtime(food.getTime());
            mMaterial.setMtype(food.getType());
            mMaterial.setMweight(food.getWeight());
            mMaterial.setPhotoUrl(food.getFoodPhotoUrl());
            mMaterial.setUrl(food.getFoodurl());
            mMaterial.setTareweight(food.getTareWeight());
            Log.d(TAG, "showOneResponse: setTareweight"+food.getTareWeight());

        }
        mHandler.sendEmptyMessage(LOAD_ONE_DATA);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: executed");
        threadDisable = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    }


