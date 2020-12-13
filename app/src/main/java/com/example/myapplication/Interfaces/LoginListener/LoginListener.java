package com.example.myapplication.Interfaces.LoginListener;

public interface LoginListener {

    void handleSignInAttempt(boolean valid, int userId, String userFirstName, String userLastName, String email);
}
