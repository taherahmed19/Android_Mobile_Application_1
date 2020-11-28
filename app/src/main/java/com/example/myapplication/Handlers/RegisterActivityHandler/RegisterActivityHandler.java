package com.example.myapplication.Handlers.RegisterActivityHandler;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.Activities.RegisterActivity.RegisterActivity;
import com.example.myapplication.R;

public class RegisterActivityHandler {

    private RegisterActivity registerActivity;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;

    public RegisterActivityHandler(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    public void configure(){
        configureFields();
        configureRegisterButton();
        configureReturnButton();
    }

    void configureRegisterButton(){
        Button registerButton = this.registerActivity.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Print", firstName + " " + lastName + " " + email + " " + password + " " + confirmPassword);

            }
        });
    }

    void configureReturnButton(){
        ImageButton registerReturnButton = this.registerActivity.findViewById(R.id.registerReturnButton);

        registerReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerActivity.finish();
            }
        });
    }

    void configureFields(){
        configureFirstName();
        configureLastName();
        configureEmail();
        configurePassword();
        configureConfirmPassword();

    }

    void configureFirstName(){
        EditText registerFirstName = this.registerActivity.findViewById(R.id.registerFirstName);

        registerFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                firstName = editable.toString();
            }
        });
    }

    void configureLastName(){
        EditText registerLastName = this.registerActivity.findViewById(R.id.registerLastName);

        registerLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                lastName = editable.toString();
            }
        });
    }

    void configureEmail(){
        EditText registerEmail = this.registerActivity.findViewById(R.id.registerEmail);

        registerEmail.addTextChangedListener(new TextWatcher() {
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
        EditText registerPassword = this.registerActivity.findViewById(R.id.registerPassword);

        registerPassword.addTextChangedListener(new TextWatcher() {
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

    void configureConfirmPassword(){
        EditText registerConfirmPassword = this.registerActivity.findViewById(R.id.registerConfirmPassword);

        registerConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                confirmPassword = editable.toString();
            }
        });
    }
}
