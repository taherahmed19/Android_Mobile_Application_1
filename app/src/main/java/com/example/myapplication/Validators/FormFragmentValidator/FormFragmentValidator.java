package com.example.myapplication.Validators.FormFragmentValidator;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.Handlers.FormFragmentHandler.FormFragmentHandler;
import com.example.myapplication.Models.FormFragmentSpinner.FormFragmentSpinner;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.SpinnerItem.SpinnerItem;
import com.example.myapplication.R;
import com.google.android.gms.maps.model.LatLng;

public class FormFragmentValidator {

    FormFragmentHandler formFragmentHandler;

    public FormFragmentValidator(FormFragmentHandler formFragmentHandler) {
        this.formFragmentHandler = formFragmentHandler;
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

        if(spinner.getSelectedItemPosition() == 0){
            addErrorDrawable(spinner);
            formFragmentHandler.showSpinnerErrorMessage();

            return false;
        }else{
            removeErrorDrawable(spinner);
            formFragmentHandler.removeSpinnerErrorMessage();
        }


        return true;
    }

    public boolean descriptionFocusChange(EditText mapFeedDescription){
        String description = mapFeedDescription.getText().toString();

        if(TextUtils.isEmpty(description)){
            addErrorDrawable(mapFeedDescription);
            formFragmentHandler.showDescriptionErrorMessage();
            return false;
        }else{
            removeErrorDrawable(mapFeedDescription);
            formFragmentHandler.removeDescriptionErrorMessage();
        }

        return true;
    }

    public boolean descriptionTextChange(EditText mapFeedDescription){
        String description = mapFeedDescription.getText().toString();

        if(!TextUtils.isEmpty(description) && description.length() > 0){
            removeErrorDrawable(mapFeedDescription);
            formFragmentHandler.removeDescriptionErrorMessage();
            return true;
        }else{
            addErrorDrawable(mapFeedDescription);
            formFragmentHandler.showDescriptionErrorMessage();
        }

        return false;
    }

    public boolean imageSelected(RelativeLayout imageButton, String encodedImage){
        if(encodedImage.length() > 0){
            removeErrorDrawable(imageButton);
            formFragmentHandler.removeImageErrorMessage();
        }else{
            addErrorDrawable(imageButton);
            formFragmentHandler.showImageErrorMessage();
        }

        return encodedImage.length() > 0;
    }

    public boolean locationSelected(Button button, LatLng chosenLocation){
        if(chosenLocation != null){
            formFragmentHandler.removeLocationErrorMessage();
        }else{
            formFragmentHandler.showLocationErrorMessage();
        }

        return chosenLocation == null;
    }
}
