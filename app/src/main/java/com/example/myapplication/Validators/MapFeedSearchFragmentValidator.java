package com.example.myapplication.Validators;

import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;


public class MapFeedSearchFragmentValidator {

    public boolean validateSubmission(EditText mapFeedSearch){
        if(!TextUtils.isEmpty(mapFeedSearch.getText())){
            return true;
        }
        return false;
    }

    public boolean validateLocationField(Editable s){
        if(s.toString().trim().length() > 0) {
            return true;
        }
        return false;
    }

}
