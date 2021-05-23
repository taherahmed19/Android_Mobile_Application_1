package com.example.myapplication.Models.LoginUser;

import android.content.Context;

import com.example.myapplication.Webservice.HttpLoginUser.HttpLoginUser;
import com.example.myapplication.Interfaces.LoginContract.LoginContract;
import com.example.myapplication.Interfaces.LoginListener.LoginListener;

public class LoginUser implements LoginContract.Model {

    LoginListener loginListener;
    private String email;
    private String password;

    public LoginUser(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void makeApiCall(Context context){
        HttpLoginUser httpLoginUser = new HttpLoginUser(context, loginListener, this);
        httpLoginUser.execute("");
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
