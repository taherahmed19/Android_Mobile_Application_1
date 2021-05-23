package com.example.myapplication.Models.RegisterUser;

import android.content.Context;

import com.example.myapplication.Webservice.HttpRegisterUser.HttpRegisterUser;
import com.example.myapplication.Interfaces.RegisterListener.RegisterListener;

public class RegisterUser {

    private String firstName;
    private String lastName;
    private String password;
    private String confirmationPassword;
    private String email;

    RegisterListener registerListener;

    public RegisterUser(RegisterListener registerListener) {
        this.registerListener = registerListener;
    }

    public void makeApiCall(Context context){
        HttpRegisterUser httpRegisterUser = new HttpRegisterUser(context, registerListener, this);
        httpRegisterUser.execute("");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
