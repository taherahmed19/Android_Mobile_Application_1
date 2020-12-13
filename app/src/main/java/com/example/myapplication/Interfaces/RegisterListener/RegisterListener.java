package com.example.myapplication.Interfaces.RegisterListener;

public interface RegisterListener {

    void handleRegistrationAttempt(boolean valid, int userId, String userFirstName, String userLastName, String email);
}
