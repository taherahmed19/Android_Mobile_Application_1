package com.example.myapplication.Activities.LocationSelectorActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.example.myapplication.Handlers.LocationSelectorActivityHandler.LocationSelectorActivityHandler;
import com.example.myapplication.Interfaces.CurrentLocationListener;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class LocationSelectorActivity extends AppCompatActivity implements CurrentLocationListener {

    CurrentLocation currentLocation;
    LocationSelectorActivityHandler locationSelectorActivityHandler;

    @Override
    public void updateUserLocation(Location location) {
        locationSelectorActivityHandler.setUserLocationGoogleMarker(location);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selector);

        locationSelectorActivityHandler = new LocationSelectorActivityHandler(this);
        locationSelectorActivityHandler.configureElements();
        locationSelectorActivityHandler.requestMap(null);
        currentLocation = new CurrentLocation(this,  this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CurrentLocation.MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    currentLocation.accessGeolocation();
                }
                return;
            }
        }
    }


    void setGoogleMapClickable(){
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng loc) {
//                mMap.clear();
//                mMap.addMarker(new MarkerOptions()
//                        .position(loc)
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                selectedLocation = loc;
//            }
//        });
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
}