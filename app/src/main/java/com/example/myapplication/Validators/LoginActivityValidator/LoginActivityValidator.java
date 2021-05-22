package com.example.myapplication.Validators.LoginActivityValidator;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.Activities.LoginActivity.LoginActivity;
import com.example.myapplication.R;

public class LoginActivityValidator {
/*
    LoginActivity loginActivity;

    public LoginActivityValidator(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public boolean validateEmailTextChanged(){
        EditText loginEmail = (EditText) this.loginActivity.findViewById(R.id.loginEmail);
        TextView emailErrorMessage = this.loginActivity.findViewById(R.id.loginEmailErrorMessage);
        String email = loginEmail.getText().toString();

        if(email.length() > 0){
            //request to webservice
            hideErrorMessages(loginEmail, emailErrorMessage, R.drawable.ic_login_email_icon);

            if(email.matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+")){
                hideErrorMessages(loginEmail, emailErrorMessage, R.drawable.ic_login_email_icon);
            }
        }else{
            showErrorMessage(loginEmail,  emailErrorMessage, R.drawable.ic_login_email_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    public boolean validatePasswordTextChanged(){
        EditText loginPassword = (EditText) this.loginActivity.findViewById(R.id.loginPassword);
        TextView emailErrorMessage = this.loginActivity.findViewById(R.id.loginPasswordErrorMessage);
        String password = loginPassword.getText().toString();

        if(password.length() > 0){
            //request to webservice
            hideErrorMessages(loginPassword, emailErrorMessage, R.drawable.ic_login_password_icon);
        }else{
            showErrorMessage(loginPassword,  emailErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }

    public boolean validateEmailFocusChanged(){
        EditText loginEmail = (EditText) this.loginActivity.findViewById(R.id.loginEmail);
        TextView emailErrorMessage = this.loginActivity.findViewById(R.id.loginEmailErrorMessage);
        String email = loginEmail.getText().toString();

        if(email.length() == 0){
            showErrorMessage(loginEmail,  emailErrorMessage, R.drawable.ic_login_email_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }else if(!email.matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+")){
            showErrorMessage(loginEmail,  emailErrorMessage, R.drawable.ic_login_email_icon, R.drawable.ic_login_register_error_icon, "Enter a valid email");
            return false;
        }

        return true;
    }

    public boolean validatePasswordFocusChange(){
        EditText registerPassword = this.loginActivity.findViewById(R.id.loginPassword);
        TextView passwordErrorMessage = this.loginActivity.findViewById(R.id.loginPasswordErrorMessage);

        String password = registerPassword.getText().toString();

        if(registerPassword.getText().length() == 0){
            showErrorMessage(registerPassword,  passwordErrorMessage, R.drawable.ic_login_password_icon, R.drawable.ic_login_register_error_icon, "Field must be inputted");
            return false;
        }

        return true;
    }
*/

}
