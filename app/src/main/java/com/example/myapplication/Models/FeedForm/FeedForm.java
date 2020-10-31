package com.example.myapplication.Models.FeedForm;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.UserFeedFormFragment.UserFeedFormFragment;
import com.example.myapplication.Fragments.UserFeedFormFragment.UserFeedFormFragmentElements;
import com.example.myapplication.Fragments.UserFeedFormFragment.UserFeedFormFragmentSpinner;
import com.example.myapplication.Fragments.UserFeedFormFragment.UserFeedFormFragmentSubmitHandler;
import com.example.myapplication.Fragments.UserFeedFormFragment.UserFeedFormFragmentValidation;
import com.example.myapplication.Models.SpinnerItem.SpinnerItem;
import com.example.myapplication.R;
import com.google.android.gms.maps.model.LatLng;

public class FeedForm {
    FragmentManager fragmentManager;

    UserFeedFormFragment userFeedFormFragment;
    UserFeedFormFragmentValidation userFeedFormFragmentValidation;
    UserFeedFormFragmentSpinner userFeedFormFragmentSpinner;

    Spinner spinner;
    EditText mapFeedDescription;
    Button geolocationButton;

    String description;
    LatLng geolocation;
    int category;

    public FeedForm(UserFeedFormFragment userFeedFormFragment, FragmentManager fragmentManager, UserFeedFormFragmentSpinner userFeedFormFragmentSpinner){
        this.userFeedFormFragment = userFeedFormFragment;
        this.description = "";
        this.geolocation = null;
        this.category = -1;
        this.fragmentManager = fragmentManager;
        this.userFeedFormFragmentValidation = new UserFeedFormFragmentValidation();
        this.initElements();
        this.userFeedFormFragmentSpinner = userFeedFormFragmentSpinner;
    }

    public void initialiseValidationConfigurators(UserFeedFormFragmentElements fragmentElements){
        this.userFeedFormFragmentValidation.setFragmentElements(fragmentElements);
        this.userFeedFormFragmentValidation.setValidationListeners(spinner, userFeedFormFragmentSpinner, mapFeedDescription);
    }

    public boolean requestSubmit(){
        setDescription();
        setGeolocation();
        setCategory();

        boolean ableToSubmit = userFeedFormFragmentValidation.validate(spinner, mapFeedDescription, geolocationButton);
        if(ableToSubmit){
            UserFeedFormFragmentSubmitHandler submitHandler =
                    new UserFeedFormFragmentSubmitHandler(fragmentManager, userFeedFormFragment, description, geolocation, category);
           //submitHandler.submit();
           //submitHandler.setSubmitted(true);

           return submitHandler.isSubmitted();
        }

        return false;
    }

    void initElements(){
        spinner = (Spinner) userFeedFormFragment.getView().findViewById(R.id.formSpinner);
        mapFeedDescription = (EditText)userFeedFormFragment.getView().findViewById(R.id.mapFeedDescription);
        geolocationButton = (Button) userFeedFormFragment.getView().findViewById(R.id.geolocationButton);
    }

    public void setDescription(){
        this.description = mapFeedDescription.getText().toString();
    }

    public void setGeolocation(){
        this.geolocation = (LatLng)geolocationButton.getTag();
    }

    public void setCategory(){
        SpinnerItem item = (SpinnerItem) spinner.getSelectedItem();
        String category = item.getName().toLowerCase();

        this.category = FeedForm.categorySwitchCase(category);
    }

    public static int categorySwitchCase(String category){
        int categoryItem = -1;

        switch (category.toLowerCase()){
            case "environment":
                categoryItem = 1;
                break;
            case "weather":
                categoryItem = 2;
                break;
            case "people":
                categoryItem = 3;
                break;
            default:
                categoryItem = 0;
                break;
        }

        return categoryItem;
    }
}