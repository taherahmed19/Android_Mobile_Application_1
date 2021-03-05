package com.example.myapplication.Handlers.LoginActivityHandler;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Activities.LoginActivity.LoginActivity;
import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.Activities.RegisterActivity.RegisterActivity;
import com.example.myapplication.Fragments.ConfirmFragment.ConfirmFragment;
import com.example.myapplication.Fragments.ErrorFragment.ErrorFragment;
import com.example.myapplication.HttpRequest.HttpFirebaseToken.HttpFirebaseToken;
import com.example.myapplication.HttpRequest.HttpLoginUser.HttpLoginUser;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.Utils.HashingTool.HashingTool;
import com.example.myapplication.Validators.LoginActivityValidator.LoginActivityValidator;

public class LoginActivityHandler {

    private LoginActivity loginActivity;
    private LoginActivityValidator loginActivityValidator;
    private String email;
    private String password;

    public LoginActivityHandler(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
        this.loginActivityValidator = new LoginActivityValidator(loginActivity);
        this.email = "";
        this.password = "";
    }

    public void handleSignInAttempt(boolean valid, int userId, String userFirstName, String userLastName, String userEmail) {
        if(valid){
            LoginPreferenceData.SaveLoginState(this.loginActivity, true, userId, userFirstName, userLastName, userEmail);

            //send firebase token
            HttpFirebaseToken httpFirebaseToken = new HttpFirebaseToken(loginActivity.getApplicationContext(),
                    LoginPreferenceData.getUserId(loginActivity.getApplicationContext()), loginActivity.getToken());
            httpFirebaseToken.execute();

            enterApplication();
        }else{
            Toast.makeText(loginActivity.getApplicationContext(), loginActivity.getApplicationContext().getString(R.string.login_error_body), Toast.LENGTH_LONG).show();
        }
    }

    public void configure(){
        configureLoginSubmitButton();
        configureRegisterButton();
        configureEmail();
        configurePassword();
    }

    void enterApplication(){
        FragmentTransition.StartActivity(this.loginActivity, MainActivity.class);
    }

    void configureLoginSubmitButton(){
        Button loginSubmitButton = (Button) this.loginActivity.findViewById(R.id.loginSubmitButton);

        loginSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAllFields();
            }
        });
    }

    boolean validateAllFields(){
        boolean validEmail = this.loginActivityValidator.validateEmailTextChanged();
        boolean validPassword = this.loginActivityValidator.validatePasswordTextChanged();

        if(validEmail && validPassword){
            this.hashPassword();
            HttpLoginUser httpLoginUser = new HttpLoginUser(this.loginActivity.getApplicationContext(), this, this.loginActivity);
            httpLoginUser.execute("");
            return true;
        }

        return false;
    }

    void hashPassword(){
        password = HashingTool.HashString(password);
    }

    void configureRegisterButton(){
        Button loginRegisterButton = this.loginActivity.findViewById(R.id.loginRegisterButton);

        loginRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.getBaseContext(), RegisterActivity.class);
                intent.putExtra("token", loginActivity.getToken());
                loginActivity.startActivity(intent);
            }
        });
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
                loginActivityValidator.validateEmailTextChanged();
            }
        });

        loginEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    loginActivityValidator.validateEmailFocusChanged();
                }
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
                loginActivityValidator.validatePasswordTextChanged();
            }
        });

        loginPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    loginActivityValidator.validatePasswordFocusChange();
                }
            }
        });
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
