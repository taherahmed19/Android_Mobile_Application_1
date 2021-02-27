package com.example.myapplication.Activities.BlankActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.Handlers.BlankActivityHandler.BlankActivityHandler;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.google.firebase.iid.FirebaseInstanceId;

public class BlankActivity extends AppCompatActivity {

    BlankActivityHandler blankActivityHandler;

    public BlankActivity() {
        this.blankActivityHandler = new BlankActivityHandler(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

        this.blankActivityHandler.loadStartActivity();

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Print", "Token= " + token);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.blankActivityHandler.loadStartActivity();
    }

}