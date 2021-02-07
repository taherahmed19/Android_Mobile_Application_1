package com.example.myapplication.Activities.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.Handlers.LoginActivityHandler.LoginActivityHandler;
import com.example.myapplication.Interfaces.LoginListener.LoginListener;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Tools.Tools;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tomtom.online.sdk.common.util.StringUtils;

import java.nio.charset.Charset;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    LoginActivityHandler loginActivityHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        this.loginActivityHandler = new LoginActivityHandler(this);
        this.loginActivityHandler.configure();
    }

    @Override
    public void handleSignInAttempt(boolean valid, int userId, String userFirstName, String userLastName, String userEmail) {
        this.loginActivityHandler.handleSignInAttempt(valid, userId, userFirstName, userLastName, userEmail);
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
        Objects.requireNonNull(this).registerReceiver(receiver, new IntentFilter(LoginActivity.class.toString()));
    }

    @Override
    public void onStop() {
        super.onStop();
        try{
            Objects.requireNonNull(this).unregisterReceiver(receiver);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String token;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String currentToken = FirebaseInstanceId.getInstance().getToken();
            Log.d("Token ", "Current token " + currentToken);

            //if current token does not exist get the generated token otherwise this.token = current token
            if(TextUtils.isEmpty(currentToken)){
                token = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("token"));
            }else{
                token = currentToken;
            }
        }
    };

    public String getToken() {
        return token;
    }
}