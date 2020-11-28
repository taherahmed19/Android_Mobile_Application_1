package com.example.myapplication.Activities.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.Handlers.LoginActivityHandler.LoginActivityHandler;
import com.example.myapplication.R;

public class LoginActivity extends AppCompatActivity {

    LoginActivityHandler loginActivityHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.loginActivityHandler = new LoginActivityHandler(this);
        this.loginActivityHandler.configure();
    }
}