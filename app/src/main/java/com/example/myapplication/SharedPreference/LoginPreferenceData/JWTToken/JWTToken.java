package com.example.myapplication.SharedPreference.LoginPreferenceData.JWTToken;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;

import static com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData.getSharedPreferences;

public class JWTToken {

    private static final String PREF_USER_TOKEN = "Jwt_token";

    public static void saveToken(Context context, String token){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_TOKEN, token);
        editor.apply();
    }

    public static void removeToken(Context context){
        getSharedPreferences(context).edit().remove(PREF_USER_TOKEN).apply();
    }

    public static String getToken(Context context)
    {
        return getSharedPreferences(context).getString(PREF_USER_TOKEN, "");
    }

    public static void removeTokenSharedPref(Activity activity){
        Toast.makeText(activity.getApplicationContext(), "Token expired. Logging out.", Toast.LENGTH_LONG).show();
        LoginPreferenceData.clear(activity.getApplicationContext());
        JWTToken.removeToken(activity.getApplicationContext());
        Intent returnIntent = new Intent();
        activity.setResult(Activity.RESULT_OK, returnIntent);
        activity.finish();
    }

}
