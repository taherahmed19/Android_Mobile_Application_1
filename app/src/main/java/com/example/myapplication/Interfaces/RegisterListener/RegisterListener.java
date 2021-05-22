package com.example.myapplication.Interfaces.RegisterListener;

import com.example.myapplication.Models.User.User;

public interface RegisterListener {

    void handleRegistrationAttempt(boolean valid, User user);
}
