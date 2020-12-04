package com.example.myapplication.Interfaces.MapListener;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;

public interface MapListener {

    //break out into new interface between mapfragment, location selector and the map handler.
    void handleMapClick(GoogleMap googleMap);
    void handleMarkerClick(GoogleMap googleMap, FragmentActivity fragmentActivity);

}
