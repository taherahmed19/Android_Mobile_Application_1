package com.example.myapplication.Utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil {


    //Break into the classes and delete class
    public static int getInt(JSONObject jsonObject, String property){
        int num = -1;

        try{
            num = jsonObject.getInt(property);
        }catch (Exception e){
            e.printStackTrace();
        }

        return num;
    }

    public static String getString(JSONObject jsonObject, String property){
        String str = "";

        try{
            str= jsonObject.getString(property);
        }catch (Exception e){
            e.printStackTrace();
        }

        return str;
    }

    public static JSONArray getJsonArray(JSONObject jsonObject, String property){
        JSONArray jsonArray = null;

        try{
            jsonArray = jsonObject.getJSONArray(property);
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonArray;
    }

    public static JSONObject getJsonObject(JSONObject jsonObject, String property){
        JSONObject jsonObjectNew = null;

        try{
            jsonObjectNew = jsonObject.getJSONObject(property);
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonObjectNew;
    }
}
