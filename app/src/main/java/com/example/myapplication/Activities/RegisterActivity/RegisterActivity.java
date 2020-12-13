package com.example.myapplication.Activities.RegisterActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.myapplication.Handlers.RegisterActivityHandler.RegisterActivityHandler;
import com.example.myapplication.Interfaces.RegisterListener.RegisterListener;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Tools.Tools;

public class RegisterActivity extends AppCompatActivity implements RegisterListener {

    RegisterActivityHandler registerActivityHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        this.registerActivityHandler = new RegisterActivityHandler(this);
        this.registerActivityHandler.configure();
    }

    @Override
    public void handleRegistrationAttempt(boolean valid, int userId, String userFirstName, String userLastName, String userEmail) {
        this.registerActivityHandler.handleRegistrationAttempt(valid, userId, userFirstName, userLastName, userEmail);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
}