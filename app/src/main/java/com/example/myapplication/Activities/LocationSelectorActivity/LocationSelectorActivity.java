package com.example.myapplication.Activities.LocationSelectorActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Interfaces.CurrentLocationListener.CurrentLocationListener;
import com.example.myapplication.Interfaces.LocationSelectorContract.LocationSelectorContract;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Presenters.LocationSelectorPresenter.LocationSelectorPresenter;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.JWTToken.JWTToken;
import com.example.myapplication.Utils.StringConstants.StringConstants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationSelectorActivity extends AppCompatActivity implements CurrentLocationListener, LocationSelectorContract.View {

    CurrentLocation currentLocation;
    SupportMapFragment supportMapFragment;
    LocationSelectorPresenter locationSelectorPresenter;

    /**
     * initial method to execute for activity
     * @param savedInstanceState instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selector);

        configureSupportMapFragment();
        configureSubmitButton();
        configureReturnButton();

        currentLocation = new CurrentLocation(this,  this);
        locationSelectorPresenter = new LocationSelectorPresenter(this);
        locationSelectorPresenter.makeApiRequestForFormMap(getSupportFragmentManager(), supportMapFragment);
    }

    /**
     * Executed once token is expired after user makes request to web service.
     */
    @Override
    public void handleTokenExpiration(){
        JWTToken.removeTokenSharedPref(this);
    }

    /**
     * Start location updates for users current location once application loads.
     */
    @Override
    public void onStart() {
        super.onStart();
        currentLocation.startLocationUpdates();
    }

    /**
     * Stop location monitoring for performance benefit
     */
    @Override
    public void onStop() {
        super.onStop();
        currentLocation.stopLocationUpdates();
    }

    /**
     * Update user marker on map
     * @param location current location
     */
    @Override
    public void updateUserLocation(Location location) {
        locationSelectorPresenter.setUserLocationGoogleMarker(location);
    }

    /**
     * generate marker on map click
     * @param googleMap map instance
     */
    @Override
    public void handleMapClick(GoogleMap googleMap) {
        this.setGoogleMapClickable(googleMap);
    }

    /**
     * add marker click listener
     * @param googleMap map instance
     */
    @Override
    public void handleMarkersListener(GoogleMap googleMap) {
        this.addMarkersListener(googleMap);
    }

    /**
     * listener code
     * @param mMap map instance
     */
    public void addMarkersListener(GoogleMap mMap){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                return true;
            }
        });
    }

    /**
     * Allow google map to be clicked - custom code for onClick
     * @param mMap map instance
     */
    public void setGoogleMapClickable(GoogleMap mMap){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                locationSelectorPresenter.handleOnGoogleMapClick(mMap, latLng);
                locationSelectorPresenter.updateSelectedLocation(latLng);
            }
        });
    }

    /**
     * Support fragment required by Google API
     */
    void configureSupportMapFragment(){
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }

    /**
     * configure XML
     */
    void configureSubmitButton(){
        final ImageButton locationSubmit = (ImageButton) findViewById(R.id.locationSubmit);

        locationSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationSelectorPresenter.handleSubmitButtonClick();
            }
        });
    }

    /**
     * configure XML
     */
    void configureReturnButton(){
        final ImageButton returnButton = (ImageButton) findViewById(R.id.returnButton);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationSelectorPresenter.handleReturnButtonClick();
            }
        });
    }

    /**
     * Add marker to map rendering
     * @param mMap map instance
     * @param latLng marker coords
     */
    @Override
    public void handleOnGoogleMapClick(GoogleMap mMap, LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    /**
     * activity submit button to finish activity and return to main
     */
    @SuppressLint("ShowToast")
    @Override
    public void handleOnSubmitButtonClick() {
        if(locationSelectorPresenter.getSelectedLocation().getLatLng() != null){
            Intent output = new Intent();
            output.putExtra(StringConstants.INTENT_LOCATION_SELECTOR_LAT, locationSelectorPresenter.getSelectedLocation().getLatLng().latitude);
            output.putExtra(StringConstants.INTENT_LOCATION_SELECTOR_LNG, locationSelectorPresenter.getSelectedLocation().getLatLng().longitude);
            setResult(RESULT_OK, output);
            finish();
        }else{
            Log.d("print", "No location");
            Toast.makeText(getApplicationContext(), "No location selected.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * customer back press
     */
    @Override
    public void handleOnReturnButtonClick() {
        onBackPressed();
    }

    /**
     * application context for activity - different from main context
     * @return context
     */
    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }
}