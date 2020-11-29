package com.example.myapplication.Handlers.LoginActivityHandler;

import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.Activities.LoginActivity.LoginActivity;
import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.Activities.RegisterActivity.RegisterActivity;
import com.example.myapplication.R;

public class LoginActivityHandler {

    private LoginActivity loginActivity;
    private String email;
    private String password;

    public LoginActivityHandler(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public void configure(){
        configureLoginSubmitButton();
        configureRegisterButton();
        configureFields();
    }

    void configureLoginSubmitButton(){
        Button loginSubmitButton = (Button) this.loginActivity.findViewById(R.id.loginSubmitButton);

        loginSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Print", "" + email + " " + password);
                Intent intent = new Intent(loginActivity.getBaseContext(), MainActivity.class);
                loginActivity.startActivity(intent);
            }
        });
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

    void configureFields(){
        configureEmail();
        configurePassword();
    }

    void configureEmail(){
        EditText loginEmail = (EditText) this.loginActivity.findViewById(R.id.loginEmail);

        loginEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                email = editable.toString();
            }
        });
    }

    void configurePassword(){
        EditText loginPassword = (EditText) this.loginActivity.findViewById(R.id.loginPassword);

        loginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                password = editable.toString();
            }
        });
    }


}
