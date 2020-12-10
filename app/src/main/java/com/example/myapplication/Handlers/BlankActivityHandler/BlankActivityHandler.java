package com.example.myapplication.Handlers.BlankActivityHandler;

import android.app.Activity;
import android.content.Intent;

import com.example.myapplication.Activities.BlankActivity.BlankActivity;
import com.example.myapplication.Activities.LoginActivity.LoginActivity;
import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;

public class BlankActivityHandler {

    BlankActivity blankActivity;

    public BlankActivityHandler(BlankActivity blankActivity) {
        this.blankActivity = blankActivity;
    }

    public void loadStartActivity(){
        boolean userLoggedIn = LoginPreferenceData.getUserLoggedIn(this.blankActivity);
        final Class<? extends Activity> activity;

        if(userLoggedIn){
            activity = MainActivity.class;
        }else{
            activity = LoginActivity.class;
        }

        Intent newActivity = new Intent(this.blankActivity, activity);
        this.blankActivity.startActivity(newActivity);
    }
}
