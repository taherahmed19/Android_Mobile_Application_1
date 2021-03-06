package com.example.myapplication.Activities.LoginActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.Activities.RegisterActivity.RegisterActivity;
import com.example.myapplication.Interfaces.LoginContract.LoginContract;
import com.example.myapplication.Models.BroadcastReceiverToken.BroadcastReceiverToken;
import com.example.myapplication.Models.User.User;
import com.example.myapplication.Presenters.LoginPresenter.LoginPresenter;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.Webservice.HttpFirebaseToken.HttpFirebaseToken;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    LoginPresenter loginPresenter;

    /**
     * initial method to execute for activity
     * @param savedInstanceState instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPresenter = new LoginPresenter(this);
        configureEmail();
        configureLoginSubmitButton();
        configurePassword();
        configureRegisterButton();
        configureFirebaseToken();
        hideKeyboard();
    }

    /**
     * validation for email field
     * @return boolean
     */
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

    /**
     * validation for password field
     * @return boolean
     */
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

    /**
     * validation for email - once user clicks off edit text
     */
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

    /**
     * validation for password - once user clicks off edit text
     */
    @Override
    public void validatePasswordFocusChange() {
        EditText registerPassword = this.findViewById(R.id.loginPassword);
        TextView passwordErrorMessage = this.findViewById(R.id.loginPasswordErrorMessage);

        if(registerPassword.getText().length() == 0){
            showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
        }

    }

    /**
     * Handle sign in and ensure password is hashed for DB.
     * @return
     */
    @Override
    public boolean submitSignIn(){
        if(validateAllFields()){
            loginPresenter.hashPassword();
            return true;
        }

        return true;
    }

    /**
     * Open new activity for registration
     * @param token Firebase token
     */
    @Override
    public void startRegisterActivity(BroadcastReceiverToken token) {
        Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
        intent.putExtra("token", token.getToken());
        startActivity(intent);
    }

    /**
     * Send token to web service or output error for validation
     * @param valid valid inptus
     * @param user user object data
     */
    @Override
    public void handleSignInAttempt(boolean valid, User user) {
        if(valid){
            LoginPreferenceData.SaveLoginState(this, true, user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());

            //send firebase token
            HttpFirebaseToken httpFirebaseToken = new HttpFirebaseToken(this.getApplicationContext(),
                    LoginPreferenceData.getUserId(this.getApplicationContext()), loginPresenter.getToken().getToken());
            httpFirebaseToken.execute();

            enterApplication();
        }else{
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.login_error_body), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * application context for activity - different from main context
     * @return context
     */
    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }

    /**
     * recycle view
     */
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    /**
     * register broadcast receiver
     */
    @Override
    public void onResume() {
        super.onResume();
        this.registerReceiver(receiver, new IntentFilter(LoginActivity.class.toString()));
    }

    /**
     * lifecycle
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * unregister broadcast receiver
     */
    @Override
    public void onStop() {
        super.onStop();
        try{
            this.unregisterReceiver(receiver);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * allow user to enter into the main application - main activity to render google map
     */
    void enterApplication(){
        FragmentTransition.StartActivity(this, MainActivity.class);
    }

    /**
     * update presenter with users unique FB token
     */
    void configureFirebaseToken(){
        loginPresenter.updateBroadcastReceiverToken(FirebaseInstanceId.getInstance().getToken());
    }

    /**
     * configure XML
     */
    void configureLoginSubmitButton(){
        Button loginSubmitButton = (Button) this.findViewById(R.id.loginSubmitButton);

        loginSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.submitSignIn(getApplicationContext());
            }
        });
    }

    /**
     * configure XML
     */
    void configureRegisterButton(){
        Button loginRegisterButton = this.findViewById(R.id.loginRegisterButton);

        loginRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.startRegisterActivity();
            }
        });
    }

    /**
     * configure XML
     */
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

    /**
     * configure XML
     */
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

    /**
     * fields validation
     * @return boolean
     */
    boolean validateAllFields(){
        boolean validEmail = loginPresenter.validateEmailTextChanged();
        boolean validPassword = loginPresenter.validatePasswordTextChanged();

        return validEmail && validPassword;
    }

    /**
     * error messaging
     * @param field edit text
     * @param errorMessage messaging
     * @param iconLeft icons
     * @param iconRight icons
     * @param message custom message from string.xml
     */
    void showErrorMessage(EditText field, TextView errorMessage, int iconLeft, int iconRight, String message){
        field.setCompoundDrawablesWithIntrinsicBounds(iconLeft, 0, iconRight, 0);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }

    /**
     * hide error message - does not remove from XML
     * @param field edit text xml
     * @param errorMessage custom message
     * @param iconLeft icons
     */
    void hideErrorMessages(EditText field, TextView errorMessage, int iconLeft){
        errorMessage.setVisibility(View.INVISIBLE);
        field.setCompoundDrawablesWithIntrinsicBounds(iconLeft, 0, 0, 0);
    }

    /**
     * Execute to prevent keyboard remaining open from other views.
     */
    void hideKeyboard(){
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }


    /**
     * Gets token when new token is generated from {FirebaseService.java}
     */
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