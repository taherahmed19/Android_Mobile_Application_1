package com.example.myapplication.Activities.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.myapplication.Handlers.LoginActivityHandler.LoginActivityHandler;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Tools.Tools;

public class LoginActivity extends AppCompatActivity {

    LoginActivityHandler loginActivityHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        this.loginActivityHandler = new LoginActivityHandler(this);
        this.loginActivityHandler.configure();
    }
}