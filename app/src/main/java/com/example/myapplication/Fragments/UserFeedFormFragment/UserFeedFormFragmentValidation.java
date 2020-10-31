package com.example.myapplication.Fragments.UserFeedFormFragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.Models.FeedForm.FeedForm;
import com.example.myapplication.Models.SpinnerItem.SpinnerItem;
import com.example.myapplication.R;

public class UserFeedFormFragmentValidation {

    UserFeedFormFragmentElements fragmentElements;

    public UserFeedFormFragmentValidation() {
    }

    public void setValidationListeners(Spinner spinner, UserFeedFormFragmentSpinner userFeedFormFragmentSpinner, EditText mapFeedDescription){
        spinnerValidationListener(spinner, userFeedFormFragmentSpinner);
        descriptionValidationListener(mapFeedDescription);
    }

    public boolean validate(Spinner spinner, EditText mapFeedDescription, TextView geolocationTextView){
        validateCategorySpinner(spinner);
        validateDescription(mapFeedDescription);

        return true;
    }

    public void removeErrorDrawable(View view){
        if(view instanceof Spinner){
            view.setBackgroundResource(R.drawable.ic_custom_spinner);
        }else{
            view.setBackgroundResource(R.drawable.input_border);
        }
    }

    boolean validateCategorySpinner(Spinner spinner){
        SpinnerItem item = (SpinnerItem) spinner.getSelectedItem();

        String category = item.getName().toLowerCase();
        int categoryItem = FeedForm.categorySwitchCase(category);

        if(categoryItem == 0){
            spinner.setTag("revalidate");
            addErrorDrawable(spinner);
            fragmentElements.showSpinnerErrorMessage();
            return false;
        }

        return true;
    }

    boolean validateDescription(EditText mapFeedDescription){
        String description = mapFeedDescription.getText().toString();

        if(TextUtils.isEmpty(description)){
            mapFeedDescription.setTag("revalidate");
            addErrorDrawable(mapFeedDescription);
            fragmentElements.showDescriptionErrorMessage();
            return false;
        }

        return true;
    }

    void descriptionValidationListener(EditText mapFeedDescription){
        mapFeedDescription.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mapFeedDescription.getTag() != null && validateDescription(mapFeedDescription)){
                    removeErrorDrawable(mapFeedDescription);
                    fragmentElements.removeDescriptionErrorMessage();
                }
            }
        });
    }

    void spinnerValidationListener(Spinner spinner, UserFeedFormFragmentSpinner userFeedFormFragmentSpinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner.getTag() != null && validateCategorySpinner(spinner)){
                    removeErrorDrawable(spinner);
                    fragmentElements.removeSpinnerErrorMessage();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void addErrorDrawable(View view){
        if(view instanceof Spinner){
            view.setBackgroundResource(R.drawable.ic_custom_spinner_error);
        }else{
            view.setBackgroundResource(R.drawable.input_border_error);
        }
    }

    public void setFragmentElements(UserFeedFormFragmentElements fragmentElements) {
        this.fragmentElements = fragmentElements;
    }

    void validateGeolocation(){ }
    void validateImage(){ }
}
