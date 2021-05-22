package com.example.myapplication.Fragments.FormFragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Fragments.ConfirmFragment.ConfirmFragment;
import com.example.myapplication.Fragments.ErrorFragment.ErrorFragment;
import com.example.myapplication.Handlers.FormFragmentHandler.FormFragmentHandler;
import com.example.myapplication.Interfaces.CurrentLocationListener.CurrentLocationListener;
import com.example.myapplication.Interfaces.FeedSubmitListener.FeedSubmitListener;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Models.FormFragmentSpinner.FormFragmentSpinner;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.Utils.StringConstants.StringConstants;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class FormFragment extends Fragment implements FeedSubmitListener, CurrentLocationListener {
    FragmentManager fragmentSupport;
    FormFragmentHandler formFragmentHandler;
    CurrentLocation currentLocation;
    LatLng currentLatLng;
    ViewPager viewPager;

    public FormFragment(FragmentManager fragmentSupport, ViewPager viewPager){
        this.fragmentSupport = fragmentSupport;
        this.viewPager = viewPager;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        currentLocation = new CurrentLocation(this.getActivity(),  this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_feed_form, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.formFragmentHandler = new FormFragmentHandler(this, this);
        this.formFragmentHandler.configure();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            double lat = data.getDoubleExtra(StringConstants.INTENT_LOCATION_SELECTOR_LAT, 0);
            double lng = data.getDoubleExtra(StringConstants.INTENT_LOCATION_SELECTOR_LNG, 0);

            this.formFragmentHandler.onActivityResultConfigure(lat, lng);
        }

        if(requestCode == FormFragmentHandler.CAMERA_REQUEST){
            this.formFragmentHandler.onActivityResultCamera(requestCode, resultCode, data);
        }

        if(requestCode == FormFragmentHandler.GALLERY_REQUEST){
            this.formFragmentHandler.onActivityResultGallery(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.formFragmentHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void handleSubmitStatusMessage(boolean validPost) {
        if(validPost){
            Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
            getParentFragmentManager().popBackStack();
        }else{
            Toast.makeText(getContext(), getContext().getString(R.string.form_error_body), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        currentLocation.startLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        currentLocation.stopLocationUpdates();
    }

    @Override
    public void updateUserLocation(Location location) {
        if(!this.formFragmentHandler.isConfiguredStaticMap()){
            this.currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
    }
}