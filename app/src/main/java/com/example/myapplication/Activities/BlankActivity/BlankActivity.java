package com.example.myapplication.Activities.BlankActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.Handlers.BlankActivityHandler.BlankActivityHandler;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;

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
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        this.blankActivityHandler.loadStartActivity();
    }

}