package com.example.myapplication.Interfaces.LoginListener;

import com.example.myapplication.Models.User.User;

public interface LoginListener {

    void handleSignInAttempt(boolean valid, User user);
}
