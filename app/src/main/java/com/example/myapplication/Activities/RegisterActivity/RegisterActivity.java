package com.example.myapplication.Activities.RegisterActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.Handlers.RegisterActivityHandler.RegisterActivityHandler;
import com.example.myapplication.R;

public class RegisterActivity extends AppCompatActivity {

    RegisterActivityHandler registerActivityHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.registerActivityHandler = new RegisterActivityHandler(this);
        this.registerActivityHandler.configure();
    }
}