package com.example.myapplication.Interfaces.LoginContract;

import android.content.Context;

import com.example.myapplication.Models.User.User;

public interface LoginContract {

    interface View{
        void handleSignInAttempt(boolean valid, User user);
        void submitSignIn();
        Context getContext();
        boolean validateEmailTextChanged();
        boolean validatePasswordTextChanged();
        void validateEmailFocusChanged();
        void validatePasswordFocusChange();
    }

    interface Presenter{

    }

    interface Model{

    }

}
