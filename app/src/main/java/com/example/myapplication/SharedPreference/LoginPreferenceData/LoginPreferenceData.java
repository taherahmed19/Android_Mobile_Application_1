package com.example.myapplication.SharedPreference.LoginPreferenceData;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LoginPreferenceData {

    private static final String PREF_USER_ID = "user_id";
    private static final String PREF_USER_FIRST_NAME = "user_first_name";
    private static final String PREF_USER_LAST_NAME = "user_last_name";
    private static final String PREF_USER_EMAIL = "user_last_name";

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUserId(Context context, int userId)
    {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(PREF_USER_ID, userId);
        editor.apply();
    }

    public static void setFirstName(Context context, String firstName){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_FIRST_NAME, firstName);
        editor.apply();
    }

    public static void setLastName(Context context, String lastName){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_LAST_NAME, lastName);
        editor.apply();
    }

    public static void setEmail(Context context, String email){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_EMAIL, email);
        editor.apply();
    }

    public static int getUserId(Context context)
    {
        return getSharedPreferences(context).getInt(PREF_USER_ID, 0);
    }

    public static String getUserFirstName(Context context)
    {
        return getSharedPreferences(context).getString(PREF_USER_FIRST_NAME, "");
    }

    public static String getUserLastName(Context context)
    {
        return getSharedPreferences(context).getString(PREF_USER_LAST_NAME, "");
    }

    public static String getUserEmail(Context context)
    {
        return getSharedPreferences(context).getString(PREF_USER_EMAIL, "");
    }
}
