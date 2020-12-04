package com.example.myapplication.Interfaces.CurrentLocationListener;

import android.location.Location;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;

public interface CurrentLocationListener {

    void updateUserLocation(Location location);
}
