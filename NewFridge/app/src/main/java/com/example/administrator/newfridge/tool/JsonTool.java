package com.example.administrator.newfridge.tool;

import android.util.Log;

import com.example.administrator.newfridge.model.IceBox.IceBoxBody;
import com.example.administrator.newfridge.model.IceBox.IceBoxInfo;
import com.example.administrator.newfridge.model.foodmodel.FoodBean;
import com.example.administrator.newfridge.model.IceBox.IceBoxIdBean;
import com.example.administrator.newfridge.model.foodmodel.FoodList;
import com.example.administrator.newfridge.model.menus.MenuBody;
import com.example.administrator.newfridge.model.menus.MenuData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class JsonTool implements MyHandlerMsg {

    private int msgNum;
    private String json;
    private static String TAG = "JsonTool";

    public JsonTool(String json, int msgNum) {
        this.json = json;
        this.msgNum = msgNum;
    }

    public String judgeMsg() {
        switch (msgNum) {

            case REQUEST_SUCCESS:
                return getCode ( json );

            case GETICEID_SUCCESS:
                if (getIceId ( json ))
                    return "0";
                else
                    return "1";

            case GETFOOD_SUCCESS:
                if (getFood ( json ))
                    return "2";
                else
                    return "3";

            case GETFAMILYINFO_SUCCESS:
                if (getFamily ( json ))
                    return "4";
                else
                    return "5";
            default:
                return null;
        }
    }

    private String getCode(String json) {
        try {
            JsonObject jsonObject = (JsonObject) new JsonParser ().parse ( json );
            return jsonObject.get ( "code" ).getAsString ();
        } catch (Exception e) {
            e.printStackTrace ();
            return "404";
        }
    }

    private boolean getIceId(String json) {

        IceBoxIdBean iceBoxIdBean = IceBoxIdBean.getIceBoxIdBean ();

        iceBoxIdBean.clear ();

        Gson gson = new GsonBuilder ()
                .setPrettyPrinting ()
                .disableHtmlEscaping ()
                .create ();

        IceBoxBody body = gson.fromJson ( json, IceBoxBody.class );
        List<IceBoxInfo> iceBoxList = body.getData ();

        Log.i ( "iceBoxList", "jsonTool: " + body.getData () );
        iceBoxIdBean.setIceId_list ( iceBoxList );
        return true;
    }

    private boolean getFood(String json) {

        try {
            JsonObject jsonObject = (JsonObject) new JsonParser ().parse ( json );

            if ("1".equals ( jsonObject.get ( "code" ).getAsString () )) {
                JsonArray jsonArray = jsonObject.getAsJsonArray ( "data" );

                Gson gson = new Gson ();
                ArrayList<FoodBean> foodList = new ArrayList<> ();

                for (JsonElement food : jsonArray) {
                    FoodBean foodBean = gson.fromJson ( food,
                            new TypeToken<FoodBean> () {
                            }.getType () );
                    foodList.add ( foodBean );

                    Log.i ( TAG, "getFood: " + foodBean.getFoodName () );
                }

                FoodList foodList1 = FoodList.getFoodList ();
                foodList1.setList ( foodList );
                return true;
            } else
                return false;
        } catch (JsonSyntaxException e) {
            e.printStackTrace ();
            return false;
        }
    }

    private boolean getFamily(String json) {
        try {
            JsonObject jsonObject = (JsonObject) new JsonParser ().parse ( json );

            if ("1".equals ( jsonObject.get ( "code" ).getAsString () )) {

                return true;
            }
            return false;
        } catch (JsonSyntaxException e) {
            e.printStackTrace ();
            return false;
        }
    }
}
