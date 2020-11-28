package com.example.myapplication.Handlers.LoginActivityHandler;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Activities.LoginActivity.LoginActivity;
import com.example.myapplication.Activities.RegisterActivity.RegisterActivity;
import com.example.myapplication.R;

public class LoginActivityHandler {

    LoginActivity loginActivity;

    public LoginActivityHandler(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public void configure(){
        configureRegisterButton();
    }

    void configureRegisterButton(){
        Button loginRegisterButton = this.loginActivity.findViewById(R.id.loginRegisterButton);

        loginRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.getBaseContext(), RegisterActivity.class);
                loginActivity.startActivity(intent);
            }
        });
    }


}
