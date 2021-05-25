package com.example.myapplication.Interfaces.CustomMarkerListener;

import com.example.myapplication.Models.Marker.Marker;

import java.util.ArrayList;

public interface CustomMarkerListener {

    void addMarkerData(ArrayList<Marker> markers);
    void detectRadiusMarker();
}
