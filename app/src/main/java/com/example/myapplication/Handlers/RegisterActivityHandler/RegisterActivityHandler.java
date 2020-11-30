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
import android.widget.TextView;

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

    void showErrorMessage(EditText field, TextView errorMessage, int iconLeft, int iconRight, String message){
        field.setTag(-1);
        field.setCompoundDrawablesWithIntrinsicBounds(iconLeft, 0, iconRight, 0);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }

    void hideErrorMessages(EditText field, TextView errorMessage, int iconLeft){
        field.setTag(1);
        errorMessage.setVisibility(View.INVISIBLE);
        field.setCompoundDrawablesWithIntrinsicBounds(iconLeft, 0, 0, 0);
    }

    void configureFirstName(){
        EditText registerFirstName = this.registerActivity.findViewById(R.id.registerFirstName);
        TextView firstNameErrorMessage = this.registerActivity.findViewById(R.id.registerFirstNameErrorMessage);

        registerFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0 ){
                    firstName = editable.toString();

                    if(registerFirstName.getTag() != null && (int)registerFirstName.getTag() == -1){
                        hideErrorMessages(registerFirstName, firstNameErrorMessage, R.drawable.ic_register_user);
                    }
                }else{
                    showErrorMessage(registerFirstName,  firstNameErrorMessage, R.drawable.ic_register_user, R.drawable.ic_login_register_error_icon, "Field must be inputted");
                }
            }
        });

        registerFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //lost focused and field is empty. Add error message
                    if(registerFirstName.getText().length() == 0){
                        showErrorMessage(registerFirstName,  firstNameErrorMessage, R.drawable.ic_register_user, R.drawable.ic_login_register_error_icon, "Field must be inputted");
                    }
                }
            }
        });
    }

    void configureLastName(){
        EditText registerLastName = this.registerActivity.findViewById(R.id.registerLastName);
        TextView lastNameErrorMessage = this.registerActivity.findViewById(R.id.registerLastNameErrorMessage);

        registerLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0 ){
                    lastName = editable.toString();

                    if(registerLastName.getTag() != null && (int)registerLastName.getTag() == -1){
                        hideErrorMessages(registerLastName, lastNameErrorMessage, R.drawable.ic_register_user);
                    }
                }else{
                    showErrorMessage(registerLastName,  lastNameErrorMessage, R.drawable.ic_register_user, R.drawable.ic_login_register_error_icon, "Field must be inputted");
                }
            }
        });

        registerLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(registerLastName.getText().length() == 0){
                        showErrorMessage(registerLastName,  lastNameErrorMessage, R.drawable.ic_register_user, R.drawable.ic_login_register_error_icon, "Field must be inputted");
                    }
                }
            }
        });

    }

    void configureEmail(){
        EditText registerEmail = this.registerActivity.findViewById(R.id.registerEmail);
        TextView emailErrorMessage = this.registerActivity.findViewById(R.id.registerEmailErrorMessage);

        registerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0 ){
                    email = editable.toString();

                    if(registerEmail.getTag() != null && (int)registerEmail.getTag() == -1 && email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                        hideErrorMessages(registerEmail, emailErrorMessage, R.drawable.ic_login_email_icon);
                    }
                }else{
                    showErrorMessage(registerEmail,  emailErrorMessage, R.drawable.ic_login_email_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
                }
            }
        });

        registerEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(registerEmail.getText().length() == 0){
                        showErrorMessage(registerEmail,  emailErrorMessage, R.drawable.ic_login_email_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
                    }else if(!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                        showErrorMessage(registerEmail,  emailErrorMessage, R.drawable.ic_login_email_icon, R.drawable.ic_login_register_error_icon, "Enter a valid email");
                    }
                }
            }
        });
    }

    void configurePassword(){
        EditText registerConfirmPassword = this.registerActivity.findViewById(R.id.registerConfirmPassword);
        TextView confirmPasswordErrorMessage = this.registerActivity.findViewById(R.id.registerConfirmPasswordErrorMessage);
        EditText registerPassword = this.registerActivity.findViewById(R.id.registerPassword);
        TextView passwordErrorMessage = this.registerActivity.findViewById(R.id.registerPasswordErrorMessage);

        registerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0 ){
                    password = editable.toString();

                    if(registerPassword.getTag() != null && (int)registerPassword.getTag() == -1 && confirmPassword.equals(password)){
                        hideErrorMessages(registerPassword, passwordErrorMessage, R.drawable.ic_login_password_icon);
                        hideErrorMessages(registerConfirmPassword, confirmPasswordErrorMessage, R.drawable.ic_login_password_icon);
                    }
                }else{
                    showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
                }
            }
        });

        registerPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(registerPassword.getText().length() == 0){
                        showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
                    }else if(!confirmPassword.equals(password)){
                        showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
                        showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
                    }
                }
            }
        });
    }

    void configureConfirmPassword(){
        EditText registerConfirmPassword = this.registerActivity.findViewById(R.id.registerConfirmPassword);
        TextView confirmPasswordErrorMessage = this.registerActivity.findViewById(R.id.registerConfirmPasswordErrorMessage);
        EditText registerPassword = this.registerActivity.findViewById(R.id.registerPassword);
        TextView passwordErrorMessage = this.registerActivity.findViewById(R.id.registerPasswordErrorMessage);

        registerConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0 ){
                    confirmPassword = editable.toString();

                    if(confirmPassword.equals(password) && registerPassword.getTag() != null && (int)registerConfirmPassword.getTag() == -1){
                        hideErrorMessages(registerConfirmPassword, confirmPasswordErrorMessage, R.drawable.ic_login_password_icon);
                        hideErrorMessages(registerPassword, passwordErrorMessage, R.drawable.ic_login_password_icon);
                    }
                }else{
                    showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
                }
            }
        });

        registerConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(registerConfirmPassword.getText().length() == 0){
                        showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
                    }else if(!confirmPassword.equals(password)){
                        showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
                        showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
                    }
                }
            }
        });
    }
}
