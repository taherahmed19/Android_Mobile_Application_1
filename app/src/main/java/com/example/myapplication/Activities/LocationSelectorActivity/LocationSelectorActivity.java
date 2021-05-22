package com.example.myapplication.Activities.LocationSelectorActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.Interfaces.CurrentLocationListener.CurrentLocationListener;
import com.example.myapplication.Interfaces.LocationSelectorContract.LocationSelectorContract;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Presenters.LocationSelectorPresenter.LocationSelectorPresenter;
import com.example.myapplication.R;
import com.example.myapplication.Utils.StringConstants.StringConstants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

public class LocationSelectorActivity extends AppCompatActivity implements CurrentLocationListener, LocationSelectorContract.View {

    CurrentLocation currentLocation;
    SupportMapFragment supportMapFragment;
    LocationSelectorPresenter locationSelectorPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selector);

        configureSupportMapFragment();
        configureSubmitButton();
        configureReturnButton();

        currentLocation = new CurrentLocation(this,  this);
        locationSelectorPresenter = new LocationSelectorPresenter(this);
        locationSelectorPresenter.requestMap(getSupportFragmentManager(), supportMapFragment);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == CurrentLocation.MY_PERMISSIONS_REQUEST_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                currentLocation.accessGeolocation();
            }
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
        locationSelectorPresenter.setUserLocationGoogleMarker(location);
    }

    @Override
    public void handleMapClick(GoogleMap googleMap) {
        this.setGoogleMapClickable(googleMap);
    }

    @Override
    public void handleMarkersListener(GoogleMap googleMap) {
        this.addMarkersListener(googleMap);
    }

    public void addMarkersListener(GoogleMap mMap){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                return true;
            }
        });
    }

    public void setGoogleMapClickable(GoogleMap mMap){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                locationSelectorPresenter.handleOnGoogleMapClick(mMap, latLng);
                locationSelectorPresenter.updateSelectedLocation(latLng);
            }
        });
    }

    void configureSupportMapFragment(){
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }

    void configureSubmitButton(){
        final ImageButton locationSubmit = (ImageButton) findViewById(R.id.locationSubmit);

        locationSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationSelectorPresenter.handleSubmitButtonClick();
            }
        });
    }

    void configureReturnButton(){
        final ImageButton returnButton = (ImageButton) findViewById(R.id.returnButton);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationSelectorPresenter.handleReturnButtonClick();
            }
        });
    }

    @Override
    public void handleOnGoogleMapClick(GoogleMap mMap, LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    @Override
    public void handleOnSubmitButtonClick() {
        Intent output = new Intent();
        output.putExtra(StringConstants.INTENT_LOCATION_SELECTOR_LAT, locationSelectorPresenter.getSelectedLocation().latitude);
        output.putExtra(StringConstants.INTENT_LOCATION_SELECTOR_LNG, locationSelectorPresenter.getSelectedLocation().longitude);
        setResult(RESULT_OK, output);
        finish();
    }

    @Override
    public void handleOnReturnButtonClick() {
        onBackPressed();
    }

    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }
}