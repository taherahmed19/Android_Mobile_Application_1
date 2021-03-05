package com.example.myapplication.Handlers.RegisterActivityHandler;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.Activities.RegisterActivity.RegisterActivity;
import com.example.myapplication.Fragments.ConfirmFragment.ConfirmFragment;
import com.example.myapplication.Fragments.ErrorFragment.ErrorFragment;
import com.example.myapplication.HttpRequest.HttpFirebaseToken.HttpFirebaseToken;
import com.example.myapplication.HttpRequest.HttpRegisterUser.HttpRegisterUser;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.Utils.HashingTool.HashingTool;
import com.example.myapplication.Validators.RegisterActivityValidator.RegisterActivityValidator;

public class RegisterActivityHandler {

    private RegisterActivity registerActivity;
    private RegisterActivityValidator registerActivityValidator;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;

    public RegisterActivityHandler(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.password = "";
        this.confirmPassword = "";
        this.registerActivityValidator = new RegisterActivityValidator(registerActivity);
    }

    public void handleRegistrationAttempt(boolean valid, int userId, String userFirstName, String userLastName, String userEmail){
        if(valid){
            LoginPreferenceData.SaveLoginState(this.registerActivity, true, userId, userFirstName, userLastName, userEmail);

            HttpFirebaseToken httpFirebaseToken = new HttpFirebaseToken(registerActivity.getApplicationContext(),
                    LoginPreferenceData.getUserId(registerActivity.getApplicationContext()), registerActivity.getToken());
            httpFirebaseToken.execute();

            enterApplication();
        }else{
            Toast.makeText(registerActivity.getApplicationContext(), registerActivity.getApplicationContext().getString(R.string.registration_error_body), Toast.LENGTH_LONG).show();
        }
    }

    void enterApplication(){
        FragmentTransition.StartActivity(this.registerActivity, MainActivity.class);
    }

    public void configure(){
        configureFirstName();
        configureLastName();
        configureEmail();
        configurePassword();
        configureConfirmPassword();
        configureRegisterButton();
        configureReturnButton();
    }

    void configureRegisterButton(){
        Button registerButton = this.registerActivity.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAllFields();
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

    void validateAllFields(){
        boolean validFirstName = registerActivityValidator.validateFirstNameTextChanged();
        boolean validLastName = registerActivityValidator.validateLastNameTextChanged();
        boolean validEmail = registerActivityValidator.validateEmailTextChanged();
        boolean validPassword = registerActivityValidator.validatePasswordTextChanged();
        boolean validConfirmPassword = registerActivityValidator.validateConfirmPasswordTextChanged();

        if(validFirstName && validLastName && validEmail && validPassword && validConfirmPassword){
            this.hashPassword();
            HttpRegisterUser httpRegisterUser = new HttpRegisterUser(this.registerActivity.getApplicationContext(), this, this.registerActivity);
            httpRegisterUser.execute("");
        }
    }

    void hashPassword(){
        password = HashingTool.HashString(password);
    }

    void configureFirstName(){
        EditText registerFirstName = this.registerActivity.findViewById(R.id.registerFirstName);

        registerFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                firstName = editable.toString();
                registerActivityValidator.validateFirstNameTextChanged();
            }
        });

        registerFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    registerActivityValidator.validateFirstNameFocusChange();
                }
            }
        });
    }

    void configureLastName(){
        EditText registerLastName = this.registerActivity.findViewById(R.id.registerLastName);

        registerLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                lastName = editable.toString();
                registerActivityValidator.validateLastNameTextChanged();
            }
        });

        registerLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    registerActivityValidator.validateLastNameFocusChange();
                }
            }
        });

    }

    void configureEmail(){
        EditText registerEmail = this.registerActivity.findViewById(R.id.registerEmail);

        registerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                email = editable.toString().toLowerCase();
                registerActivityValidator.validateEmailTextChanged();
            }
        });

        registerEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    registerActivityValidator.validateEmailFocusChange();
                }
            }
        });
    }

    void configurePassword(){
        EditText registerPassword = this.registerActivity.findViewById(R.id.registerPassword);

        registerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                password = editable.toString();
                registerActivityValidator.validatePasswordTextChanged();
            }
        });

        registerPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    registerActivityValidator.validatePasswordFocusChange();
                }
            }
        });
    }

    void configureConfirmPassword(){
        EditText registerConfirmPassword = this.registerActivity.findViewById(R.id.registerConfirmPassword);

        registerConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                confirmPassword = editable.toString();
                registerActivityValidator.validateConfirmPasswordTextChanged();
            }
        });

        registerConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    registerActivityValidator.validateConfirmPasswordFocusChange();
                }
            }
        });
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
