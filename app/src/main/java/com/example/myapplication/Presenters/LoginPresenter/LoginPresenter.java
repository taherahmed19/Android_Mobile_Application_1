package com.example.myapplication.Presenters.LoginPresenter;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.Interfaces.LoginContract.LoginContract;
import com.example.myapplication.Interfaces.LoginListener.LoginListener;
import com.example.myapplication.Models.LoginUser.LoginUser;
import com.example.myapplication.Models.User.User;
import com.example.myapplication.R;
import com.example.myapplication.Utils.HashingTool.HashingTool;

public class LoginPresenter implements LoginContract.Presenter, LoginListener {

    private LoginContract.View view;
    private LoginUser loginUser;

    public LoginPresenter(LoginContract.View view) {
        this.loginUser = new LoginUser(this);
        this.view = view;
    }

    @Override
    public void handleSignInAttempt(boolean valid, User user) {
        view.handleSignInAttempt(valid, user);
    }

    public void updateEmail(String email){
        loginUser.setEmail(email);
    }

    public void updatePassword(String password){
        loginUser.setPassword(password);
    }

    public void hashPassword(){
        loginUser.setPassword(HashingTool.HashString(loginUser.getPassword()));
    }

    public boolean validateEmailTextChanged(){
        return view.validateEmailTextChanged();
    }

    public boolean validatePasswordTextChanged(){
        return view.validatePasswordTextChanged();
    }

    public void validateEmailFocusChanged(){
        view.validateEmailFocusChanged();
    }

    public void validatePasswordFocusChange(){
        view.validatePasswordFocusChange();
    }

    public void submitSignIn(Context context){
        view.submitSignIn();
        loginUser.makeApiCall(context);
    }
}
