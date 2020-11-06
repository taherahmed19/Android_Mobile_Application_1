package com.example.myapplication.Activities.LocationSelectorActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.R;
import com.example.myapplication.Utils.StringConstants.StringConstants;
import com.example.myapplication.Utils.Tools.Tools;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationSelectorActivity extends AppCompatActivity implements OnMapReadyCallback {

    CurrentLocation currentLocation;
    LatLng location;
    GoogleMap mMap;
    LatLng selectedLocation;

    int customMarkerIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selector);

        currentLocation = new CurrentLocation(this);
        location = currentLocation.accessGeolocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        configureReturnButton();
        configureSubmitButton();
        configureCustomMarkerIcon();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.latitude, location.longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        googleMap.setTrafficEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), 40));

        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        setGoogleMapClickable();
    }

    void setGoogleMapClickable(){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            Drawable circleDrawable;
            BitmapDescriptor markerIcon;

            @Override
            public void onMapClick(LatLng loc) {
                setupMarker();
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .icon(markerIcon));
                selectedLocation = loc;
            }

            public void setupMarker(){
                if(customMarkerIcon == (int)(BitmapDescriptorFactory.HUE_RED)){
                    markerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                }else{
                    circleDrawable = getResources().getDrawable(customMarkerIcon);
                    markerIcon = Tools.getMarkerIconFromDrawable(circleDrawable);
                }
            }
        });
    }

    void configureCustomMarkerIcon(){
        Intent intent = getIntent();
        this.customMarkerIcon = intent.getIntExtra("chosenMarker", (int)(BitmapDescriptorFactory.HUE_RED));
    }

    void configureSubmitButton(){
        final Button locationSubmit = (Button) findViewById(R.id.locationSubmit);

        locationSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent output = new Intent();
                output.putExtra(StringConstants.INTENT_LOCATION_SELECTOR_LAT, selectedLocation.latitude);
                output.putExtra(StringConstants.INTENT_LOCATION_SELECTOR_LNG, selectedLocation.longitude);
                setResult(RESULT_OK, output);
                finish();
            }
        });
    }

    void configureReturnButton(){
        final TextView returnButton = (TextView) findViewById(R.id.returnButton);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}