package com.example.myapplication.Interfaces.LoginContract;

import android.content.Context;

import com.example.myapplication.Models.BroadcastReceiverToken.BroadcastReceiverToken;
import com.example.myapplication.Models.User.User;

public interface LoginContract {

    interface View{
        void handleSignInAttempt(boolean valid, User user);
        boolean submitSignIn();
        void startRegisterActivity(BroadcastReceiverToken token);
        void validateEmailFocusChanged();
        void validatePasswordFocusChange();
        boolean validateEmailTextChanged();
        boolean validatePasswordTextChanged();
        Context getContext();
    }

    interface Presenter{

    }

    interface Model{

    }

}
