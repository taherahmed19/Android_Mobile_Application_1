package com.example.myapplication.Activities.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.myapplication.Handlers.LoginActivityHandler.LoginActivityHandler;
import com.example.myapplication.Interfaces.LoginListener.LoginListener;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Tools.Tools;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class LoginActivity extends AppCompatActivity implements LoginListener {

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

    @Override
    public void handleSignInAttempt(boolean valid) {
        this.loginActivityHandler.handleSignInAttempt(valid);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());    }

}