package com.example.myapplication.Validators.FormFragmentValidator;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.Handlers.FormFragmentHandler.FormFragmentHandler;
import com.example.myapplication.Models.FormFragmentSpinner.FormFragmentSpinner;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.SpinnerItem.SpinnerItem;
import com.example.myapplication.R;

public class FormFragmentValidator {

    FormFragmentHandler fragmentElements;

    public FormFragmentValidator(FormFragmentHandler fragmentElements) {
        this.fragmentElements = fragmentElements;
    }

    public void removeErrorDrawable(View view){
        if(view instanceof Spinner){
            view.setBackgroundResource(R.drawable.ic_custom_spinner);
        }else{
            view.setBackgroundResource(R.drawable.input_border);
        }
    }

    void addErrorDrawable(View view){
        if(view instanceof Spinner){
            view.setBackgroundResource(R.drawable.ic_custom_spinner_error);
        }else{
            view.setBackgroundResource(R.drawable.input_border_error);
        }
    }

    public boolean spinnerFocusChange(Spinner spinner){
        SpinnerItem item = (SpinnerItem) spinner.getSelectedItem();

        String category = item.getName().toLowerCase();
        int categoryItem = Marker.CategorySwitchCase(category);

        if(categoryItem == 0){
            addErrorDrawable(spinner);
            fragmentElements.showSpinnerErrorMessage();

            return false;
        }else{
            removeErrorDrawable(spinner);
            fragmentElements.removeSpinnerErrorMessage();
        }

        return true;
    }

    public boolean descriptionFocusChange(EditText mapFeedDescription){
        String description = mapFeedDescription.getText().toString();

        if(TextUtils.isEmpty(description)){
            addErrorDrawable(mapFeedDescription);
            fragmentElements.showDescriptionErrorMessage();
            return false;
        }else{
            removeErrorDrawable(mapFeedDescription);
            fragmentElements.removeDescriptionErrorMessage();
        }

        return true;
    }

    public boolean descriptionTextChange(EditText mapFeedDescription){
        String description = mapFeedDescription.getText().toString();

        if(!TextUtils.isEmpty(description) && description.length() > 0){
            removeErrorDrawable(mapFeedDescription);
            fragmentElements.removeDescriptionErrorMessage();
            return true;
        }else{
            addErrorDrawable(mapFeedDescription);
            fragmentElements.showDescriptionErrorMessage();
        }

        return false;
    }


    public void setFragmentElements(FormFragmentHandler fragmentElements) {
        this.fragmentElements = fragmentElements;
    }

    void validateGeolocation(){ }
    void validateImage(){ }
}
