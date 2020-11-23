package com.example.myapplication.Handlers.LocationSelectorActivityHandler;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.Activities.LocationSelectorActivity.LocationSelectorActivity;
import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.Handlers.MapHandler.MapHandler;
import com.example.myapplication.HttpRequest.HttpMap.HttpMap;
import com.example.myapplication.Interfaces.MapListener;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.Settings.Settings;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.Utils.StringConstants.StringConstants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.app.Activity.RESULT_OK;

public class LocationSelectorActivityHandler {

    MapHandler mapHandler;
    SupportMapFragment supportMapFragment;
    LocationSelectorActivity locationSelectorActivity;
    LatLng selectedLocation;

    MapListener mapListenerInstance;

    public LocationSelectorActivityHandler(LocationSelectorActivity locationSelectorActivity, MapListener mapListenerInstance) {
        this.locationSelectorActivity = locationSelectorActivity;
        this.mapListenerInstance = mapListenerInstance;
    }

    public void configureElements(){
        this.configureSupportMapFragment();
        this.configureSubmitButton();
        this.configureReturnButton();
    }

    public void requestMap(Settings settings){
        this.mapHandler = new MapHandler(supportMapFragment, locationSelectorActivity, locationSelectorActivity.getSupportFragmentManager(), mapListenerInstance);
    }

    public void setUserLocationGoogleMarker(Location location) {
        mapHandler.setUserLocationGoogleMarker(location);
    }

    public void addMarkersListener(GoogleMap mMap, FragmentActivity fragmentActivity){
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
            public void onMapClick(LatLng loc) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                selectedLocation = loc;
            }
        });
    }

    void configureSupportMapFragment(){
        supportMapFragment = (SupportMapFragment) locationSelectorActivity.getSupportFragmentManager().findFragmentById(R.id.map);
    }

    void configureSubmitButton(){
        final Button locationSubmit = (Button) locationSelectorActivity.findViewById(R.id.locationSubmit);

        locationSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent output = new Intent();
                output.putExtra(StringConstants.INTENT_LOCATION_SELECTOR_LAT, selectedLocation.latitude);
                output.putExtra(StringConstants.INTENT_LOCATION_SELECTOR_LNG, selectedLocation.longitude);
                locationSelectorActivity.setResult(RESULT_OK, output);
                locationSelectorActivity.finish();
            }
        });
    }

    void configureReturnButton(){
        final TextView returnButton = (TextView) locationSelectorActivity.findViewById(R.id.returnButton);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationSelectorActivity.onBackPressed();
            }
        });
    }
}
