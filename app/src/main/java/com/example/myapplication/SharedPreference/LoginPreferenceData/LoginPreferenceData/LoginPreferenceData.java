package com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LoginPreferenceData {

    private static final String PREF_USER_LOGGED_IN = "user_logged_in";
    private static final String PREF_USER_ID = "user_id";
    private static final String PREF_USER_FIRST_NAME = "user_first_name";
    private static final String PREF_USER_LAST_NAME = "user_last_name";
    private static final String PREF_USER_EMAIL = "user_email";

    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void SaveLoginState(Activity activity, boolean userLoggedIn, int userId, String userFirstName, String userLastName, String userEmail){
        LoginPreferenceData.setUserLoggedIn(activity.getApplicationContext(), userLoggedIn);
        LoginPreferenceData.setUserId(activity.getApplicationContext(), userId);
        LoginPreferenceData.setFirstName(activity.getApplicationContext(), userFirstName);
        LoginPreferenceData.setLastName(activity.getApplicationContext(), userLastName);
        LoginPreferenceData.setEmail(activity.getApplicationContext(), userEmail);
    }

    public static void clear(Context context){
        LoginPreferenceData.setUserLoggedIn(context, false);
        getSharedPreferences(context).edit().remove(PREF_USER_ID).apply();
        getSharedPreferences(context).edit().remove(PREF_USER_FIRST_NAME).apply();
        getSharedPreferences(context).edit().remove(PREF_USER_LAST_NAME).apply();
        getSharedPreferences(context).edit().remove(PREF_USER_EMAIL).apply();
    }

    public static void setUserLoggedIn(Context context, boolean loggedIn){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(PREF_USER_LOGGED_IN, loggedIn);
        editor.apply();
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

    public static boolean getUserLoggedIn(Context context)
    {
        return getSharedPreferences(context).getBoolean(PREF_USER_LOGGED_IN, false);
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
