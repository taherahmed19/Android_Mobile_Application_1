package com.example.myapplication.Models.CurrentLocation;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Looper;

import androidx.core.app.ActivityCompat;

import com.example.myapplication.Interfaces.CurrentLocationListener.CurrentLocationListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class CurrentLocation {

    Activity context;
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 111;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    CurrentLocationListener exampleInterface;

    public CurrentLocation(Activity context) {
        this.context = context;
    }

    public CurrentLocation(Activity context, CurrentLocationListener exampleInterface) {
        this.context = context;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        this.exampleInterface = exampleInterface;
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            exampleInterface.updateUserLocation(locationResult.getLastLocation());
            //exampleInterface.updateUserLocation(locationResult.getLastLocation());
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
