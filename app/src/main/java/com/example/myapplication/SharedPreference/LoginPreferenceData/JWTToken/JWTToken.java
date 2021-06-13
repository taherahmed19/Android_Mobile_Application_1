package com.example.myapplication.SharedPreference.LoginPreferenceData.JWTToken;

import android.content.Context;
import android.content.SharedPreferences;

import static com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData.getSharedPreferences;

public class JWTToken {

    private static final String PREF_USER_TOKEN = "Jwt_token";

    public static void saveToken(Context context, String token){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_TOKEN, token);
        editor.apply();
    }

    public static void checkTokenExpired(){

    }

    public static String getToken(Context context)
    {
        return getSharedPreferences(context).getString(PREF_USER_TOKEN, "");
    }
}
