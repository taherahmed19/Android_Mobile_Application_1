package com.example.myapplication.Handlers.LocationSelectorActivityHandler;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.example.myapplication.Activities.LocationSelectorActivity.LocationSelectorActivity;
import com.example.myapplication.Handlers.MapHandler.MapHandler;
import com.example.myapplication.HttpRequest.HttpMap.HttpMap;
import com.example.myapplication.Models.Settings.Settings;
import com.example.myapplication.R;
import com.example.myapplication.Utils.StringConstants.StringConstants;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import static android.app.Activity.RESULT_OK;

public class LocationSelectorActivityHandler {

    MapHandler mapHandler;
    SupportMapFragment supportMapFragment;
    LocationSelectorActivity locationSelectorActivity;

    public LocationSelectorActivityHandler(LocationSelectorActivity locationSelectorActivity) {
        this.locationSelectorActivity = locationSelectorActivity;
    }

    public void configureElements(){
        this.configureSupportMapFragment();
        this.configureSubmitButton();
        this.configureReturnButton();
    }

    public void requestMap(Settings settings){
        this.mapHandler = new MapHandler(supportMapFragment, locationSelectorActivity, locationSelectorActivity.getSupportFragmentManager());
    }

    public void setUserLocationGoogleMarker(Location location) {
        mapHandler.setUserLocationGoogleMarker(location);
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
                output.putExtra(StringConstants.INTENT_LOCATION_SELECTOR_LAT, mapHandler.getSelectedLocation().latitude);
                output.putExtra(StringConstants.INTENT_LOCATION_SELECTOR_LNG, mapHandler.getSelectedLocation().longitude);
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
