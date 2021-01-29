package com.example.myapplication.Handlers.RadiusMarkerHandler;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class RadiusMarkerHandler {

    private Circle radiusMarker;
    private GoogleMap mMap;
    private LatLng latLng;
    private double oneMile = 1609.3;
    private double INITIAL_RADIUS = 20;

    public RadiusMarkerHandler(GoogleMap mMap, LatLng latLng) {
        this.mMap = mMap;
        this.latLng = latLng;
        this.createStartingMarker();
    }

    public void createStartingMarker(){
        if(radiusMarker == null) {
            radiusMarker = mMap.addCircle(new CircleOptions()
                    .center(latLng)
                    .fillColor(0x220000FF)
                    .strokeColor(Color.parseColor("#6699ff"))
                    .strokeWidth(5)
                    .radius(INITIAL_RADIUS));
        }
    }

    public void updateMarkerRadius(double radius){
        radiusMarker.setRadius(radius);
    }

    public void removeMarker(){
        radiusMarker.remove();
    }
}
