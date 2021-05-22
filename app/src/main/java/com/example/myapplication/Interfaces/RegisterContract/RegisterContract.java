package com.example.myapplication.Interfaces.RegisterContract;

import android.content.Context;

import com.example.myapplication.Models.BroadcastReceiverToken.BroadcastReceiverToken;
import com.example.myapplication.Models.User.User;

public interface RegisterContract {

    interface View{
        void handleRegistrationAttempt(boolean valid, User user);
        boolean validateFirstNameTextChanged();
        boolean validateFirstNameFocusChange();
        boolean validateLastNameTextChanged();
        boolean validateLastNameFocusChange();
        boolean validateEmailTextChanged();
        boolean validateEmailFocusChange();
        boolean validatePasswordTextChanged();
        boolean validatePasswordFocusChange();
        boolean validateConfirmPasswordTextChanged();
        boolean validateConfirmPasswordFocusChange();
        Context getContext();
    }

    interface Presenter{

    }

    interface Model{

    }

}
