package com.example.myapplication.Fragments.UserFeedFormFragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Activities.LocationSelectorActivity.LocationSelectorActivity;
import com.example.myapplication.Adapters.CustomSpinnerAdapter.CustomSpinnerAdapter;
import com.example.myapplication.HttpRequest.FormStaticMap.FormStaticMap;
import com.example.myapplication.Models.FeedForm.FeedForm;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Models.SpinnerItem.SpinnerItem;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Tools.Tools;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;

public class UserFeedFormFragmentElements {

    ImageView formLocationImage;
    EditText mapFeedDescription;
    String spinnerItem;
    Button geolocationButton;
    Spinner spinner;
    ViewPager viewPager;
    UserFeedFormFragmentSpinner userFeedFormFragmentSpinner;
    FeedForm feedForm;
    UserFeedFormFragment userFeedFormFragment;

    public UserFeedFormFragmentElements(EditText mapFeedDescription, String spinnerItem, Button geolocationButton,
                                        Spinner spinner, ViewPager viewPager, UserFeedFormFragmentSpinner userFeedFormFragmentSpinner,
                                        FeedForm feedForm, UserFeedFormFragment userFeedFormFragment) {
        this.mapFeedDescription = mapFeedDescription;
        this.spinnerItem = spinnerItem;
        this.geolocationButton = geolocationButton;
        this.spinner = spinner;
        this.viewPager = viewPager;
        this.userFeedFormFragmentSpinner = userFeedFormFragmentSpinner;
        this.feedForm = feedForm;
        this.userFeedFormFragment = userFeedFormFragment;
    }

    public void showDescriptionErrorMessage(){
        TextView descriptionError = (TextView) userFeedFormFragment.getView().findViewById(R.id.descriptionError);
        descriptionError.setVisibility(View.VISIBLE);
    }

    public void showSpinnerErrorMessage(){
        TextView spinnerError = (TextView) userFeedFormFragment.getView().findViewById(R.id.spinnerError);
        spinnerError.setVisibility(View.VISIBLE);
    }

    public void removeDescriptionErrorMessage(){
        TextView descriptionError = (TextView) userFeedFormFragment.getView().findViewById(R.id.descriptionError);
        descriptionError.setVisibility(View.INVISIBLE);
    }

    public void removeSpinnerErrorMessage(){
        TextView spinnerError = (TextView) userFeedFormFragment.getView().findViewById(R.id.spinnerError);
        spinnerError.setVisibility(View.INVISIBLE);
    }

    void configureDescriptionText(){
        mapFeedDescription = (EditText) userFeedFormFragment.getView().findViewById(R.id.mapFeedDescription);
    }

    void configureLocationButton(){
        final Button locationButton = (Button) userFeedFormFragment.getView().findViewById(R.id.locationButton);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner = userFeedFormFragment.getView().findViewById(R.id.formSpinner);
                int marker = -1;

                if(spinner.getSelectedItemPosition() == 0){
                    marker = (int)(BitmapDescriptorFactory.HUE_RED);
                }else{
                    SpinnerItem spinnerItem = (SpinnerItem)spinner.getSelectedItem();
                    marker = Tools.MarkerPicker(spinnerItem.getName().toLowerCase());
                }

                Intent intent = new Intent(userFeedFormFragment.getActivity(), LocationSelectorActivity.class);
                intent.putExtra("chosenMarker", marker);

                userFeedFormFragment.startActivityForResult(intent, 1);
            }
        });
    }

    void configureGeolocationButton(){
        geolocationButton = (Button) userFeedFormFragment.getView().findViewById(R.id.geolocationButton);

        geolocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGeolocationButtonClickable(false);
                updateGeolocationText(false);

                CurrentLocation currentLocation = new CurrentLocation(userFeedFormFragment.getActivity());
                LatLng location = currentLocation.accessGeolocation();

                configureStaticMap(new LatLng(location.latitude, location.longitude));
            }
        });

        setGeolocationButtonClickable(false);
        updateGeolocationText(false);
    }

    void setGeolocationButtonClickable(boolean clickable){
        geolocationButton.setClickable(clickable);
    }

    void setFormGeolocationTag(double lat, double lng){
        geolocationButton.setTag(new LatLng(lat, lng));
    }

    void updateGeolocationText(boolean locationButtonSelected){
        if(locationButtonSelected){
            geolocationButton.setText(userFeedFormFragment.getString(R.string.form_use_current_location));
        }else{
            geolocationButton.setText(userFeedFormFragment.getString(R.string.form_current_location));
        }
    }

    void configureStaticMap(LatLng latLng){
        String imageUrl = MessageFormat.format(userFeedFormFragment.getString(R.string.form_static_map),
                latLng.latitude, latLng.longitude, 15, latLng.latitude, latLng.longitude, userFeedFormFragment.getString(R.string.GOOGLE_API_KEY));
        formLocationImage = (ImageView) userFeedFormFragment.getView().findViewById(R.id.formLocationImage);

        setFormGeolocationTag(latLng.latitude, latLng.longitude);

        FormStaticMap formStaticMap = new FormStaticMap(formLocationImage);
        formStaticMap.execute(imageUrl);
    }

    void configureFormCloseButton(){
        final TextView formClose = (TextView) userFeedFormFragment.getView().findViewById(R.id.formClose);
        formClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userFeedFormFragment.getParentFragmentManager().popBackStack();
            }
        });
    }

    void configureSubmitButton(){
        Button mapFeedSubmit = (Button) userFeedFormFragment.getView().findViewById(R.id.mapFeedSubmit);
        mapFeedSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean submitted = feedForm.requestSubmit();
                if(submitted){
                    Log.d("Print", "Button clicked");
                    viewPager.getAdapter().notifyDataSetChanged(); //add to new post submission
                    userFeedFormFragment.getActivity().onBackPressed();
                }
            }
        });
    }

    ArrayList<SpinnerItem> configureMarkerSpinner(){
        spinner = userFeedFormFragment.getView().findViewById(R.id.formSpinner);
        ArrayList<SpinnerItem> customList = CustomSpinnerAdapter.CreateMarkerSpinnerItems(userFeedFormFragment.getContext());

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(Objects.requireNonNull(userFeedFormFragment.getActivity()), customList);

        if(spinner != null){
            spinner.setAdapter(adapter);
        }

        return customList;
    }
}
