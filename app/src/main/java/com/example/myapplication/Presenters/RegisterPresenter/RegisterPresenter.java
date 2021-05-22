package com.example.myapplication.Presenters.RegisterPresenter;

import com.example.myapplication.Interfaces.RegisterContract.RegisterContract;
import com.example.myapplication.Interfaces.RegisterListener.RegisterListener;
import com.example.myapplication.Models.RegisterUser.RegisterUser;
import com.example.myapplication.Models.User.User;
import com.example.myapplication.Utils.HashingTool.HashingTool;

public class RegisterPresenter implements RegisterContract.Presenter, RegisterListener {

    RegisterContract.View view;
    RegisterUser registerUser;

    public RegisterPresenter(RegisterContract.View view) {
        this.view = view;
        this.registerUser = new RegisterUser(this);
    }

    @Override
    public void handleRegistrationAttempt(boolean valid, User user) {
        view.handleRegistrationAttempt(valid, user);
    }

    public void registerAccount(){
        registerUser.makeApiCall(view.getContext());
    }

    public void hashPassword(){
        this.registerUser.setPassword(HashingTool.HashString(this.registerUser.getPassword()));
    }

    public void updateFirstName(String firstName){
        this.registerUser.setFirstName(firstName);
    }

    public void updateLastName(String lastName){
        this.registerUser.setLastName(lastName);
    }

    public void updateEmail(String email){
        this.registerUser.setEmail(email);
    }

    public void updatePassword(String password){
        this.registerUser.setPassword(password);
    }

    public void updateConfirmationPassword(String confirmationPassword){
        this.registerUser.setConfirmationPassword(confirmationPassword);
    }

    public boolean validateFirstNameTextChanged(){
        return view.validateFirstNameTextChanged();
    }

    public boolean validateFirstNameFocusChange(){
        return view.validateFirstNameFocusChange();
    }

    public boolean validateLastNameTextChanged(){
       return view.validateLastNameTextChanged();
    }

    public boolean validateLastNameFocusChange(){
       return view.validateLastNameFocusChange();
    }

    public boolean validateEmailTextChanged(){
        return view.validateEmailTextChanged();
    }

    public boolean validateEmailFocusChange(){
        return view.validateEmailFocusChange();
    }

    public boolean validatePasswordTextChanged(){
        return view.validatePasswordTextChanged();
    }

    public boolean validatePasswordFocusChange(){
        return view.validatePasswordFocusChange();
    }

    public boolean validateConfirmPasswordTextChanged(){
        return view.validateConfirmPasswordTextChanged();
    }

    public boolean validateConfirmPasswordFocusChange(){
        return view.validateConfirmPasswordFocusChange();
    }

    public void validateAllFields(){
        view.validateAllFields();
    }
}
