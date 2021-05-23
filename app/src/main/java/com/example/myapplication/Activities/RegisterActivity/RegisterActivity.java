package com.example.myapplication.Activities.RegisterActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.Webservice.HttpFirebaseToken.HttpFirebaseToken;
import com.example.myapplication.Interfaces.RegisterContract.RegisterContract;
import com.example.myapplication.Models.BroadcastReceiverToken.BroadcastReceiverToken;
import com.example.myapplication.Models.User.User;
import com.example.myapplication.Presenters.RegisterPresenter.RegisterPresenter;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View {

    BroadcastReceiverToken token;
    RegisterPresenter registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.registerPresenter = new RegisterPresenter(this);

        token = new BroadcastReceiverToken();
        token.setToken(getIntent().getStringExtra("token"));

        configureFirstName();
        configureLastName();
        configureEmail();
        configurePassword();
        configureConfirmPassword();
        configureRegisterButton();
        configureReturnButton();
        hideKeyboard();
    }

    @Override
    public void validateAllFields() {
        boolean validFirstName = registerPresenter.validateFirstNameTextChanged();
        boolean validLastName = registerPresenter.validateLastNameTextChanged();
        boolean validEmail = registerPresenter.validateEmailTextChanged();
        boolean validPassword = registerPresenter.validatePasswordTextChanged();
        boolean validConfirmPassword = registerPresenter.validateConfirmPasswordTextChanged();

        if(validFirstName && validLastName && validEmail && validPassword && validConfirmPassword){
            registerPresenter.hashPassword();
            registerPresenter.registerAccount();
        }
    }

    @Override
    public void handleRegistrationAttempt(boolean valid, User user) {
        if(valid){
            LoginPreferenceData.SaveLoginState(this, true, user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());

            HttpFirebaseToken httpFirebaseToken = new HttpFirebaseToken(getApplicationContext(),
                    LoginPreferenceData.getUserId(getApplicationContext()), token.getToken());
            httpFirebaseToken.execute();

            enterApplication();
        }else{
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.registration_error_body), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    void configureFirstName(){
        EditText registerFirstName = this.findViewById(R.id.registerFirstName);

        registerFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                registerPresenter.updateFirstName(editable.toString());
                registerPresenter.validateFirstNameTextChanged();
            }
        });

        registerFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    registerPresenter.validateFirstNameFocusChange();
                }
            }
        });
    }

    void configureLastName(){
        EditText registerLastName = this.findViewById(R.id.registerLastName);

        registerLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                registerPresenter.updateLastName(editable.toString());
                registerPresenter.validateLastNameTextChanged();
            }
        });

        registerLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    registerPresenter.validateLastNameFocusChange();
                }
            }
        });

    }

    void configureEmail(){
        EditText registerEmail = this.findViewById(R.id.registerEmail);

        registerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                registerPresenter.updateEmail(editable.toString().toLowerCase());
                registerPresenter.validateEmailTextChanged();
            }
        });

        registerEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    registerPresenter.validateEmailFocusChange();
                }
            }
        });
    }

    void configurePassword(){
        EditText registerPassword = this.findViewById(R.id.registerPassword);

        registerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                registerPresenter.updatePassword(editable.toString());
                registerPresenter.validatePasswordTextChanged();
            }
        });

        registerPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    registerPresenter.validatePasswordFocusChange();
                }
            }
        });
    }

    void configureConfirmPassword(){
        EditText registerConfirmPassword = this.findViewById(R.id.registerConfirmPassword);

        registerConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                registerPresenter.updateConfirmationPassword(editable.toString());
                registerPresenter.validateConfirmPasswordTextChanged();
            }
        });

        registerConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    registerPresenter.validateConfirmPasswordFocusChange();
                }
            }
        });
    }

    void configureRegisterButton(){
        Button registerButton = this.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerPresenter.validateAllFields();
            }
        });
    }

    void configureReturnButton(){
        ImageButton registerReturnButton = this.findViewById(R.id.registerReturnButton);

        registerReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void enterApplication(){
        FragmentTransition.StartActivity(this, MainActivity.class);
    }

    void hideKeyboard(){
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    void showErrorMessage(EditText field, TextView errorMessage, int iconLeft, int iconRight, String message){
        field.setCompoundDrawablesWithIntrinsicBounds(iconLeft, 0, iconRight, 0);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }

    void hideErrorMessages(EditText field, TextView errorMessage, int iconLeft){
        errorMessage.setVisibility(View.INVISIBLE);
        field.setCompoundDrawablesWithIntrinsicBounds(iconLeft, 0, 0, 0);
    }

    @Override
    public boolean validateFirstNameTextChanged() {
        EditText registerFirstName = this.findViewById(R.id.registerFirstName);
        TextView firstNameErrorMessage = this.findViewById(R.id.registerFirstNameErrorMessage);
        String firstName = registerFirstName.getText().toString();

        if(firstName.length() > 0){
            hideErrorMessages(registerFirstName, firstNameErrorMessage, R.drawable.ic_register_user);
        }else{
            showErrorMessage(registerFirstName,  firstNameErrorMessage, R.drawable.ic_register_user, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    @Override
    public boolean validateFirstNameFocusChange() {
        EditText registerFirstName = this.findViewById(R.id.registerFirstName);
        TextView firstNameErrorMessage = this.findViewById(R.id.registerFirstNameErrorMessage);

        if(registerFirstName.getText().length() == 0){
            showErrorMessage(registerFirstName,  firstNameErrorMessage, R.drawable.ic_register_user, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    @Override
    public boolean validateLastNameTextChanged() {
        EditText registerLastName = this.findViewById(R.id.registerLastName);
        TextView lastNameErrorMessage = this.findViewById(R.id.registerLastNameErrorMessage);
        String lastName = registerLastName.getText().toString();

        if(lastName.length() > 0){
            hideErrorMessages(registerLastName, lastNameErrorMessage, R.drawable.ic_register_user);
        }else{
            showErrorMessage(registerLastName,  lastNameErrorMessage, R.drawable.ic_register_user, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    @Override
    public boolean validateLastNameFocusChange() {
        EditText registerLastName = this.findViewById(R.id.registerLastName);
        TextView lastNameErrorMessage = this.findViewById(R.id.registerLastNameErrorMessage);

        if(registerLastName.getText().length() == 0){
            showErrorMessage(registerLastName,  lastNameErrorMessage, R.drawable.ic_register_user, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    @Override
    public boolean validateEmailTextChanged() {
        EditText registerEmail = this.findViewById(R.id.registerEmail);
        TextView emailErrorMessage = this.findViewById(R.id.registerEmailErrorMessage);
        String email = registerEmail.getText().toString();

        if(email.length() > 0 ){
            hideErrorMessages(registerEmail, emailErrorMessage, R.drawable.ic_login_email_icon);

            if(email.matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+")){
                hideErrorMessages(registerEmail, emailErrorMessage, R.drawable.ic_login_email_icon);
            }
        }else{
            showErrorMessage(registerEmail,  emailErrorMessage, R.drawable.ic_login_email_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    @Override
    public boolean validateEmailFocusChange() {
        EditText registerEmail = this.findViewById(R.id.registerEmail);
        TextView emailErrorMessage = this.findViewById(R.id.registerEmailErrorMessage);
        String email = registerEmail.getText().toString();

        if(registerEmail.getText().length() == 0){
            showErrorMessage(registerEmail,  emailErrorMessage, R.drawable.ic_login_email_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }else if(!email.matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+")){
            showErrorMessage(registerEmail,  emailErrorMessage, R.drawable.ic_login_email_icon, R.drawable.ic_login_register_error_icon, "Enter a valid email");
            return false;
        }

        return true;
    }

    @Override
    public boolean validatePasswordTextChanged() {
        EditText registerPassword = this.findViewById(R.id.registerPassword);
        TextView passwordErrorMessage = this.findViewById(R.id.registerPasswordErrorMessage);
        EditText registerConfirmPassword = this.findViewById(R.id.registerConfirmPassword);
        TextView confirmPasswordErrorMessage = this.findViewById(R.id.registerConfirmPasswordErrorMessage);

        String password = registerPassword.getText().toString();
        String confirmPassword = registerConfirmPassword.getText().toString();

        if(password.length() > 0){
            hideErrorMessages(registerPassword, passwordErrorMessage, R.drawable.ic_login_password_icon);

            if(confirmPassword.length() > 0 && !password.equals(confirmPassword)){
                showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
                showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
                return false;
            }

            if(password.equals(confirmPassword)){
                hideErrorMessages(registerConfirmPassword, confirmPasswordErrorMessage, R.drawable.ic_login_password_icon);
            }
        } else{
            showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    @Override
    public boolean validatePasswordFocusChange() {
        EditText registerPassword = this.findViewById(R.id.registerPassword);
        TextView passwordErrorMessage = this.findViewById(R.id.registerPasswordErrorMessage);
        EditText registerConfirmPassword = this.findViewById(R.id.registerConfirmPassword);
        TextView confirmPasswordErrorMessage = this.findViewById(R.id.registerConfirmPasswordErrorMessage);

        String password = registerPassword.getText().toString();
        String confirmPassword = registerConfirmPassword.getText().toString();

        if(registerPassword.getText().length() == 0){
            showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }else if(!confirmPassword.equals(password) && confirmPassword.length() > 0){
            showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
            showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
            return false;
        }

        return true;
    }

    @Override
    public boolean validateConfirmPasswordTextChanged() {
        EditText registerPassword = this.findViewById(R.id.registerPassword);
        TextView passwordErrorMessage = this.findViewById(R.id.registerPasswordErrorMessage);
        EditText registerConfirmPassword = this.findViewById(R.id.registerConfirmPassword);
        TextView confirmPasswordErrorMessage = this.findViewById(R.id.registerConfirmPasswordErrorMessage);

        String password = registerPassword.getText().toString();
        String confirmPassword = registerConfirmPassword.getText().toString();

        if(confirmPassword.length() > 0){
            hideErrorMessages(registerConfirmPassword, confirmPasswordErrorMessage, R.drawable.ic_login_password_icon);

            if(password.length() > 0 && !confirmPassword.equals(password)){
                showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
                showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
                return false;
            }

            if(confirmPassword.equals(password)){
                hideErrorMessages(registerPassword, passwordErrorMessage, R.drawable.ic_login_password_icon);
            }
        }else{
            showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    @Override
    public boolean validateConfirmPasswordFocusChange() {
        EditText registerPassword = this.findViewById(R.id.registerPassword);
        TextView passwordErrorMessage = this.findViewById(R.id.registerPasswordErrorMessage);
        EditText registerConfirmPassword = this.findViewById(R.id.registerConfirmPassword);
        TextView confirmPasswordErrorMessage = this.findViewById(R.id.registerConfirmPasswordErrorMessage);

        String password = registerPassword.getText().toString();
        String confirmPassword = registerConfirmPassword.getText().toString();

        if(registerConfirmPassword.getText().length() == 0){
            showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }else if(!confirmPassword.equals(password) && confirmPassword.length() > 0){
            showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
            showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
            return false;
        }
        return true;
    }

    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }
}