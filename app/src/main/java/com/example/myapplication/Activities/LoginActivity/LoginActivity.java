package com.example.myapplication.Activities.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.Activities.RegisterActivity.RegisterActivity;
import com.example.myapplication.Interfaces.LoginContract.LoginContract;
import com.example.myapplication.Models.BroadcastReceiverToken.BroadcastReceiverToken;
import com.example.myapplication.Models.User.User;
import com.example.myapplication.Presenters.LoginPresenter.LoginPresenter;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    LoginPresenter loginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPresenter = new LoginPresenter(this);
        configureEmail();
        configureLoginSubmitButton();
        configurePassword();
        configureRegisterButton();
        hideKeyboard();
    }

    @Override
    public boolean validateEmailTextChanged() {
        EditText loginEmail = (EditText) this.findViewById(R.id.loginEmail);
        TextView emailErrorMessage = this.findViewById(R.id.loginEmailErrorMessage);
        String email = loginEmail.getText().toString();

        if(email.length() > 0){
            hideErrorMessages(loginEmail, emailErrorMessage, R.drawable.ic_login_email_icon);

            if(email.matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+")){
                hideErrorMessages(loginEmail, emailErrorMessage, R.drawable.ic_login_email_icon);
            }
        }else{
            showErrorMessage(loginEmail,  emailErrorMessage, R.drawable.ic_login_email_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    @Override
    public boolean validatePasswordTextChanged() {
        EditText loginPassword = (EditText) this.findViewById(R.id.loginPassword);
        TextView emailErrorMessage = this.findViewById(R.id.loginPasswordErrorMessage);
        String password = loginPassword.getText().toString();

        if(password.length() > 0){
            hideErrorMessages(loginPassword, emailErrorMessage, R.drawable.ic_login_password_icon);
        }else{
            showErrorMessage(loginPassword,  emailErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    @Override
    public void validateEmailFocusChanged() {
        EditText loginEmail = (EditText) this.findViewById(R.id.loginEmail);
        TextView emailErrorMessage = this.findViewById(R.id.loginEmailErrorMessage);
        String email = loginEmail.getText().toString();

        if(email.length() == 0){
            showErrorMessage(loginEmail,  emailErrorMessage, R.drawable.ic_login_email_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
        }else if(!email.matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+")){
            showErrorMessage(loginEmail,  emailErrorMessage, R.drawable.ic_login_email_icon, R.drawable.ic_login_register_error_icon, "Enter a valid email");
        }

    }

    @Override
    public void validatePasswordFocusChange() {
        EditText registerPassword = this.findViewById(R.id.loginPassword);
        TextView passwordErrorMessage = this.findViewById(R.id.loginPasswordErrorMessage);

        String password = registerPassword.getText().toString();

        if(registerPassword.getText().length() == 0){
            showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
        }

    }

    @Override
    public void submitSignIn(){
        if(validateAllFields()){
            loginPresenter.hashPassword();
        }
    }

    @Override
    public void startRegisterActivity(BroadcastReceiverToken token) {
        Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
        intent.putExtra("token", token.getToken());
        startActivity(intent);
    }

    @Override
    public void handleSignInAttempt(boolean valid, User user) {
        if(valid){
            LoginPreferenceData.SaveLoginState(this, true, user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());

            //send firebase token
//            HttpFirebaseToken httpFirebaseToken = new HttpFirebaseToken(loginActivity.getApplicationContext(),
//                    LoginPreferenceData.getUserId(loginActivity.getApplicationContext()), loginActivity.getToken());
//            httpFirebaseToken.execute();

            enterApplication();
        }else{
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.login_error_body), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onResume() {
        super.onResume();
        this.registerReceiver(receiver, new IntentFilter(LoginActivity.class.toString()));
    }

    @Override
    public void onStop() {
        super.onStop();
        try{
            this.unregisterReceiver(receiver);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void enterApplication(){
        FragmentTransition.StartActivity(this, MainActivity.class);
    }

    void configureLoginSubmitButton(){
        Button loginSubmitButton = (Button) this.findViewById(R.id.loginSubmitButton);

        loginSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.submitSignIn(getApplicationContext());
            }
        });
    }

    void configureRegisterButton(){
        Button loginRegisterButton = this.findViewById(R.id.loginRegisterButton);

        loginRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.startRegisterActivity();
            }
        });
    }

    void configureEmail(){
        EditText loginEmail = (EditText) this.findViewById(R.id.loginEmail);

        loginEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                loginPresenter.updateEmail(editable.toString());
                loginPresenter.validateEmailTextChanged();
            }
        });

        loginEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    loginPresenter.validateEmailFocusChanged();
                }
            }
        });
    }

    void configurePassword(){
        EditText loginPassword = (EditText) this.findViewById(R.id.loginPassword);

        loginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                loginPresenter.updatePassword(editable.toString());
                loginPresenter.validatePasswordTextChanged();
            }
        });

        loginPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    loginPresenter.validatePasswordFocusChange();
                }
            }
        });
    }

    boolean validateAllFields(){
        boolean validEmail = loginPresenter.validateEmailTextChanged();
        boolean validPassword = loginPresenter.validatePasswordTextChanged();

        return validEmail && validPassword;
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

    void hideKeyboard(){
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String currentToken = FirebaseInstanceId.getInstance().getToken();
            String token;

            //if current token does not exist get the generated token otherwise this.token = current token
            if(TextUtils.isEmpty(currentToken)){
                token = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("token"));
            }else{
                token = currentToken;
            }

            loginPresenter.updateBroadcastReceiverToken(token);
        }
    };
}