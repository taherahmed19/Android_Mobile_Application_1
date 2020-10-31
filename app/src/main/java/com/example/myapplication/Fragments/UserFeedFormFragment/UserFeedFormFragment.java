package com.example.myapplication.Fragments.UserFeedFormFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.R;
import com.example.myapplication.Models.FeedForm.FeedForm;
import com.example.myapplication.Utils.StringConstants.StringConstants;
import com.google.android.gms.maps.model.LatLng;

import static android.app.Activity.RESULT_OK;

public class UserFeedFormFragment extends Fragment  {
    FragmentManager fragmentSupport;

    Spinner spinner;
    EditText mapFeedDescription;
    Button geolocationButton;
    String spinnerItem;

    ViewPager viewPager;

    FeedForm feedForm;
    UserFeedFormFragmentElements userFeedFormFragmentElements;
    UserFeedFormFragmentSpinner userFeedFormFragmentSpinner;

    public UserFeedFormFragment(FragmentManager fragmentSupport, ViewPager viewPager){
        this.fragmentSupport = fragmentSupport;
        this.viewPager = viewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_feed_form, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.userFeedFormFragmentSpinner = new UserFeedFormFragmentSpinner(this);
        feedForm = new FeedForm(this, getFragmentManager(), userFeedFormFragmentSpinner);
        this.userFeedFormFragmentElements = new UserFeedFormFragmentElements(mapFeedDescription, spinnerItem, geolocationButton,
                spinner, viewPager, userFeedFormFragmentSpinner,
                feedForm, this);

        feedForm.initialiseValidationConfigurators(userFeedFormFragmentElements);

        CurrentLocation currentLocation = new CurrentLocation(getActivity());
        LatLng location = currentLocation.accessGeolocation();

        userFeedFormFragmentElements.configureMarkerSpinner();
        userFeedFormFragmentElements.configureDescriptionText();
        userFeedFormFragmentElements.configureGeolocationButton();
        userFeedFormFragmentElements.configureLocationButton();
        userFeedFormFragmentElements.configureStaticMap(new LatLng(location.latitude, location.longitude));
        userFeedFormFragmentElements.configureFormCloseButton();
        userFeedFormFragmentElements.configureSubmitButton();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            double lat = data.getDoubleExtra(StringConstants.INTENT_LOCATION_SELECTOR_LAT, 0);
            double lng = data.getDoubleExtra(StringConstants.INTENT_LOCATION_SELECTOR_LNG, 0);

            userFeedFormFragmentElements.setGeolocationButtonClickable(true);
            userFeedFormFragmentElements.updateGeolocationText(true);
            userFeedFormFragmentElements.configureStaticMap(new LatLng(lat,lng));
        }
    }
}