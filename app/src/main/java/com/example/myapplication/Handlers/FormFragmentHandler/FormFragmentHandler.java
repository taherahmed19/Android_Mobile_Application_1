package com.example.myapplication.Handlers.FormFragmentHandler;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.Activities.LocationSelectorActivity.LocationSelectorActivity;
import com.example.myapplication.Adapters.CustomSpinnerAdapter.CustomSpinnerAdapter;
import com.example.myapplication.Fragments.FormFragment.FormFragment;
import com.example.myapplication.HttpRequest.HttpFeed.HttpFeed;
import com.example.myapplication.Interfaces.FeedSubmitListener.FeedSubmitListener;
import com.example.myapplication.Models.FormFragmentSpinner.FormFragmentSpinner;
import com.example.myapplication.HttpRequest.FormStaticMap.FormStaticMap;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.SpinnerItem.SpinnerItem;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Utils.Tools.Tools;
import com.example.myapplication.Validators.FormFragmentValidator.FormFragmentValidator;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;

public class FormFragmentHandler {

    ImageView formLocationImage;

    FormFragmentSpinner formFragmentSpinner;
    FormFragment formFragment;
    FeedSubmitListener feedSubmitListener;
    FormFragmentValidator formFragmentValidator;

    boolean isConfiguredStaticMap;

    public FormFragmentHandler(FormFragment formFragment, FeedSubmitListener feedSubmitListener) {
        this.formFragmentSpinner = new FormFragmentSpinner(formFragment);
        this.formFragment = formFragment;
        this.feedSubmitListener = feedSubmitListener;
        this.isConfiguredStaticMap = false;
        this.formFragmentValidator = new FormFragmentValidator(this);
    }

    public void configure(){
        configureMarkerSpinner();
        configureDescriptionText();
        configureGeolocationButton();
        configureLocationButton();
        configureFormCloseButton();
        configureSubmitButton();
        configureSpinner();
    }

    public void submit(){
        Spinner spinner = (Spinner) formFragment.getView().findViewById(R.id.formSpinner);
        SpinnerItem item = (SpinnerItem) spinner.getSelectedItem();
        int category = Marker.CategorySwitchCase(item.getName().toLowerCase());
        EditText mapFeedDescription = (EditText) formFragment.getView().findViewById(R.id.mapFeedDescription);
        String description = Tools.encodeString(mapFeedDescription.getText().toString());
        Button geolocationButton = (Button) formFragment.getView().findViewById(R.id.geolocationButton);
        int userID = LoginPreferenceData.getUserId(formFragment.getActivity().getApplicationContext());

        LatLng chosenLocation = (LatLng)geolocationButton.getTag();

        HttpFeed httpFeed = new HttpFeed(this.formFragment.getContext(), userID, category, description, chosenLocation ,feedSubmitListener);
        httpFeed.execute("");
    }

    public void onActivityResultConfigure(double lat, double lng){
        setGeolocationButtonClickable(true);
        updateGeolocationText(true);
        configureStaticMap(new LatLng(lat,lng));
    }

    public void showDescriptionErrorMessage(){
        TextView descriptionError = (TextView) formFragment.getView().findViewById(R.id.descriptionError);
        descriptionError.setVisibility(View.VISIBLE);
    }

    public void showSpinnerErrorMessage(){
        TextView spinnerError = (TextView) formFragment.getView().findViewById(R.id.spinnerError);
        spinnerError.setVisibility(View.VISIBLE);
    }

    public void removeDescriptionErrorMessage(){
        TextView descriptionError = (TextView) formFragment.getView().findViewById(R.id.descriptionError);
        descriptionError.setVisibility(View.INVISIBLE);
    }

    public void removeSpinnerErrorMessage(){
        TextView spinnerError = (TextView) formFragment.getView().findViewById(R.id.spinnerError);
        spinnerError.setVisibility(View.INVISIBLE);
    }

    public boolean isConfiguredStaticMap() {
        return isConfiguredStaticMap;
    }

    void configureSpinner(){
        Spinner spinner = formFragment.getView().findViewById(R.id.formSpinner);
        int initialPosition = spinner.getSelectedItemPosition();
        spinner.setSelection(initialPosition, false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                formFragmentValidator.spinnerFocusChange(spinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void configureDescriptionText(){
        EditText mapFeedDescription = (EditText) formFragment.getView().findViewById(R.id.mapFeedDescription);

        mapFeedDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                formFragmentValidator.descriptionTextChange(mapFeedDescription);
            }
        });
    }

    void configureLocationButton(){
        final Button locationButton = (Button) formFragment.getView().findViewById(R.id.locationButton);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner spinner = formFragment.getView().findViewById(R.id.formSpinner);
                int marker = -1;

                if(spinner.getSelectedItemPosition() == 0){
                    marker = (int)(BitmapDescriptorFactory.HUE_RED);
                }else{
                    SpinnerItem spinnerItem = (SpinnerItem)spinner.getSelectedItem();
                    marker = Tools.MarkerPicker(spinnerItem.getName().toLowerCase());
                }

                Intent intent = new Intent(formFragment.getActivity(), LocationSelectorActivity.class);
                intent.putExtra("chosenMarker", marker);

                formFragment.startActivityForResult(intent, 1);
            }
        });
    }

    void configureGeolocationButton(){
        Button geolocationButton = (Button) formFragment.getView().findViewById(R.id.geolocationButton);

        geolocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGeolocationButtonClickable(false);
                updateGeolocationText(false);

                CurrentLocation currentLocation = new CurrentLocation(formFragment.getActivity());
                LatLng location = currentLocation.accessGeolocation();

                configureStaticMap(new LatLng(location.latitude, location.longitude));
            }
        });

        setGeolocationButtonClickable(false);
        updateGeolocationText(false);
    }

    void setGeolocationButtonClickable(boolean clickable){
        Button geolocationButton = (Button) formFragment.getView().findViewById(R.id.geolocationButton);
        geolocationButton.setClickable(clickable);
    }

    void setFormGeolocationTag(double lat, double lng){
        Button geolocationButton = (Button) formFragment.getView().findViewById(R.id.geolocationButton);
        geolocationButton.setTag(new LatLng(lat, lng));
    }

    void updateGeolocationText(boolean locationButtonSelected){
        Button geolocationButton = (Button) formFragment.getView().findViewById(R.id.geolocationButton);

        if(locationButtonSelected){
            geolocationButton.setText(formFragment.getString(R.string.form_use_current_location));
        }else{
            geolocationButton.setText(formFragment.getString(R.string.form_current_location));
        }
    }

    public void configureStaticMap(LatLng latLng){
        String imageUrl = MessageFormat.format(formFragment.getString(R.string.form_static_map),
                latLng.latitude, latLng.longitude, 15, latLng.latitude, latLng.longitude, formFragment.getString(R.string.GOOGLE_API_KEY));
        formLocationImage = (ImageView) formFragment.getView().findViewById(R.id.formLocationImage);

        setFormGeolocationTag(latLng.latitude, latLng.longitude);

        FormStaticMap formStaticMap = new FormStaticMap(formLocationImage);
        formStaticMap.execute(imageUrl);

        isConfiguredStaticMap = true;
    }

    void configureFormCloseButton(){
        final TextView formClose = (TextView) formFragment.getView().findViewById(R.id.formClose);
        formClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(formFragment.getActivity() != null){
                    Tools.HideKeyboard(formFragment.getActivity());
                }
                formFragment.getParentFragmentManager().popBackStack();
            }
        });
    }

    void configureSubmitButton(){
        Button mapFeedSubmit = (Button) formFragment.getView().findViewById(R.id.mapFeedSubmit);
        mapFeedSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validPost = validate();
                if(validPost){
                    if(formFragment.getActivity() != null){
                        Tools.HideKeyboard(formFragment.getActivity());
                    }
                    submit();
                }
            }
        });
    }

    ArrayList<SpinnerItem> configureMarkerSpinner(){
        Spinner spinner = formFragment.getView().findViewById(R.id.formSpinner);
        ArrayList<SpinnerItem> customList = CustomSpinnerAdapter.CreateMarkerSpinnerItems(formFragment.getContext());

        if(formFragment.getActivity() != null){
            CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(formFragment.getActivity(), customList);

            if(spinner != null){
                spinner.setAdapter(adapter);
            }
        }

        return customList;
    }

    boolean validate(){
        EditText mapFeedDescription = (EditText) formFragment.getView().findViewById(R.id.mapFeedDescription);
        Spinner spinner = formFragment.getView().findViewById(R.id.formSpinner);
        boolean validSpinnerItem = formFragmentValidator.spinnerFocusChange(spinner);
        boolean validDescription = formFragmentValidator.descriptionFocusChange(mapFeedDescription);

        return validSpinnerItem && validDescription;
    }

}
