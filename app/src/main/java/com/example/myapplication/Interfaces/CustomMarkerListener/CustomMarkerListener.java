package com.example.myapplication.Interfaces.CustomMarkerListener;

import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.RadiusMarker.RadiusMarker;

import java.util.ArrayList;

public interface CustomMarkerListener {

    void renderRadiusMarker(double lat, double lon, double radius, boolean inApp, boolean voice);
    void addMarkerData(ArrayList<Marker> markers);
    void detectRadiusMarker();

}
