package com.example.myapplication.Handlers.RegisterActivityHandler;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.Activities.RegisterActivity.RegisterActivity;
import com.example.myapplication.R;
import com.example.myapplication.Validation.RegisterActivityValidator.RegisterActivityValidator;

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

    void configureFields(){
        configureFirstName();
        configureLastName();
        configureEmail();
        configurePassword();
        configureConfirmPassword();
    }

    void validateAllFields(){
        boolean validFirstName = registerActivityValidator.validateFirstNameTextChanged();
        boolean validLastName = registerActivityValidator.validateLastNameTextChanged();
        boolean validEmail = registerActivityValidator.validateEmailTextChanged();
        boolean validPassword = registerActivityValidator.validatePasswordTextChanged();
        boolean validConfirmPassword = registerActivityValidator.validateConfirmPasswordTextChanged();

        if(validFirstName && validLastName && validEmail && validPassword && validConfirmPassword){
            Log.d("Print", "Registration valid");
            Log.d("Print", firstName + " " + lastName + " " + email + " " + password + " " + confirmPassword);
        }else{
            Log.d("Print", "Registration invalid");
        }
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
                email = editable.toString();
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
}
