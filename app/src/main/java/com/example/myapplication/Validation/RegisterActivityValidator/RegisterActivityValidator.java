package com.example.myapplication.Validation.RegisterActivityValidator;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.Activities.RegisterActivity.RegisterActivity;
import com.example.myapplication.R;

public class RegisterActivityValidator {

    RegisterActivity registerActivity;

    public RegisterActivityValidator(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    public boolean validateFirstNameTextChanged(){
        EditText registerFirstName = this.registerActivity.findViewById(R.id.registerFirstName);
        TextView firstNameErrorMessage = this.registerActivity.findViewById(R.id.registerFirstNameErrorMessage);
        String firstName = registerFirstName.getText().toString();

        if(firstName.length() > 0){
            hideErrorMessages(registerFirstName, firstNameErrorMessage, R.drawable.ic_register_user);
        }else{
            showErrorMessage(registerFirstName,  firstNameErrorMessage, R.drawable.ic_register_user, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    public boolean validateFirstNameFocusChange(){
        EditText registerFirstName = this.registerActivity.findViewById(R.id.registerFirstName);
        TextView firstNameErrorMessage = this.registerActivity.findViewById(R.id.registerFirstNameErrorMessage);

        if(registerFirstName.getText().length() == 0){
            showErrorMessage(registerFirstName,  firstNameErrorMessage, R.drawable.ic_register_user, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    public boolean validateLastNameTextChanged(){
        EditText registerLastName = this.registerActivity.findViewById(R.id.registerLastName);
        TextView lastNameErrorMessage = this.registerActivity.findViewById(R.id.registerLastNameErrorMessage);
        String lastName = registerLastName.getText().toString();

        if(lastName.length() > 0){
            hideErrorMessages(registerLastName, lastNameErrorMessage, R.drawable.ic_register_user);
        }else{
            showErrorMessage(registerLastName,  lastNameErrorMessage, R.drawable.ic_register_user, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    public boolean validateLastNameFocusChange(){
        EditText registerLastName = this.registerActivity.findViewById(R.id.registerLastName);
        TextView lastNameErrorMessage = this.registerActivity.findViewById(R.id.registerLastNameErrorMessage);

        if(registerLastName.getText().length() == 0){
            showErrorMessage(registerLastName,  lastNameErrorMessage, R.drawable.ic_register_user, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    public boolean validateEmailTextChanged(){
        EditText registerEmail = this.registerActivity.findViewById(R.id.registerEmail);
        TextView emailErrorMessage = this.registerActivity.findViewById(R.id.registerEmailErrorMessage);
        String email = registerEmail.getText().toString();

        if(email.length() > 0 ){
            hideErrorMessages(registerEmail, emailErrorMessage, R.drawable.ic_login_email_icon);

            if(email.matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+")){
                hideErrorMessages(registerEmail, emailErrorMessage, R.drawable.ic_login_email_icon);
            }
        }else{
            showErrorMessage(registerEmail,  emailErrorMessage, R.drawable.ic_login_email_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    public boolean validateEmailFocusChange(){
        EditText registerEmail = this.registerActivity.findViewById(R.id.registerEmail);
        TextView emailErrorMessage = this.registerActivity.findViewById(R.id.registerEmailErrorMessage);
        String email = registerEmail.getText().toString();

        if(registerEmail.getText().length() == 0){
            showErrorMessage(registerEmail,  emailErrorMessage, R.drawable.ic_login_email_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }else if(!email.matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+")){
            showErrorMessage(registerEmail,  emailErrorMessage, R.drawable.ic_login_email_icon, R.drawable.ic_login_register_error_icon, "Enter a valid email");
            return false;
        }

        return true;
    }

    public boolean validatePasswordTextChanged(){
        EditText registerPassword = this.registerActivity.findViewById(R.id.registerPassword);
        TextView passwordErrorMessage = this.registerActivity.findViewById(R.id.registerPasswordErrorMessage);
        EditText registerConfirmPassword = this.registerActivity.findViewById(R.id.registerConfirmPassword);
        TextView confirmPasswordErrorMessage = this.registerActivity.findViewById(R.id.registerConfirmPasswordErrorMessage);

        String password = registerPassword.getText().toString();
        String confirmPassword = registerConfirmPassword.getText().toString();

        if(password.length() > 0){
            hideErrorMessages(registerPassword, passwordErrorMessage, R.drawable.ic_login_password_icon);

            if(confirmPassword.length() > 0 && !password.equals(confirmPassword)){
                showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
                showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
                return false;
            }

            if(password.equals(confirmPassword)){
                hideErrorMessages(registerConfirmPassword, confirmPasswordErrorMessage, R.drawable.ic_login_password_icon);
            }
        } else{
            showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    public boolean validatePasswordFocusChange(){
        EditText registerPassword = this.registerActivity.findViewById(R.id.registerPassword);
        TextView passwordErrorMessage = this.registerActivity.findViewById(R.id.registerPasswordErrorMessage);
        EditText registerConfirmPassword = this.registerActivity.findViewById(R.id.registerConfirmPassword);
        TextView confirmPasswordErrorMessage = this.registerActivity.findViewById(R.id.registerConfirmPasswordErrorMessage);

        String password = registerPassword.getText().toString();
        String confirmPassword = registerConfirmPassword.getText().toString();

        if(registerPassword.getText().length() == 0){
            showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }else if(!confirmPassword.equals(password) && confirmPassword.length() > 0){
            showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
            showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
            return false;
        }

        return true;
    }

    public boolean validateConfirmPasswordTextChanged(){
        EditText registerPassword = this.registerActivity.findViewById(R.id.registerPassword);
        TextView passwordErrorMessage = this.registerActivity.findViewById(R.id.registerPasswordErrorMessage);
        EditText registerConfirmPassword = this.registerActivity.findViewById(R.id.registerConfirmPassword);
        TextView confirmPasswordErrorMessage = this.registerActivity.findViewById(R.id.registerConfirmPasswordErrorMessage);

        String password = registerPassword.getText().toString();
        String confirmPassword = registerConfirmPassword.getText().toString();

        if(confirmPassword.length() > 0){
            hideErrorMessages(registerConfirmPassword, confirmPasswordErrorMessage, R.drawable.ic_login_password_icon);

            if(password.length() > 0 && !confirmPassword.equals(password)){
                showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
                showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
                return false;
            }

            if(confirmPassword.equals(password)){
                hideErrorMessages(registerPassword, passwordErrorMessage, R.drawable.ic_login_password_icon);
            }
        }else{
            showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    public boolean validateConfirmPasswordFocusChange(){
        EditText registerPassword = this.registerActivity.findViewById(R.id.registerPassword);
        TextView passwordErrorMessage = this.registerActivity.findViewById(R.id.registerPasswordErrorMessage);
        EditText registerConfirmPassword = this.registerActivity.findViewById(R.id.registerConfirmPassword);
        TextView confirmPasswordErrorMessage = this.registerActivity.findViewById(R.id.registerConfirmPasswordErrorMessage);

        String password = registerPassword.getText().toString();
        String confirmPassword = registerConfirmPassword.getText().toString();

        if(registerConfirmPassword.getText().length() == 0){
            showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }else if(!confirmPassword.equals(password) && confirmPassword.length() > 0){
            showErrorMessage(registerConfirmPassword,  confirmPasswordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
            showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Password do not match");
            return false;
        }
        return true;
    }

    void showErrorMessage(EditText field, TextView errorMessage, int iconLeft, int iconRight, String message){
        field.setCompoundDrawablesWithIntrinsicBounds(iconLeft, 0, iconRight, 0);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }

    void hideErrorMessages(EditText field, TextView errorMessage, int iconLeft){
        errorMessage.setVisibility(View.INVISIBLE);
        field.setCompoundDrawablesWithIntrinsicBounds(iconLeft, 0, 0, 0);
    }

}
