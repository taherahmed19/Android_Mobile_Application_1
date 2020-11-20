package com.example.myapplication.Models.CurrentLocation;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.Manifest;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.Handlers.MapHandler.MapHandler;
import com.example.myapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.example.myapplication.Fragments.MapFragment.MapFragment;

public class CurrentLocation {

    Activity context;
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 111;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    MapFragment.CurrentLocationListener listener;

    public CurrentLocation(Activity context) {
        this.context = context;
    }

    public CurrentLocation(Activity context, MapFragment.CurrentLocationListener listener) {
        this.context = context;
        this.listener = listener;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            listener.updateUserLocation(locationResult.getLastLocation());
        }
    };

    public LatLng accessGeolocation() {
        return new LatLng(52.500426, -1.969457);
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}
