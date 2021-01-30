package com.example.myapplication.Handlers.RadiusMarkerHandler;

import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.util.Log;
import android.widget.SeekBar;

import com.example.myapplication.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class RadiusMarkerHandler {

    private Circle radiusMarker;
    private GoogleMap mMap;
    private LatLng latLng;
    private float currentZoom;
    private CircleOptions co;
    private double radius;

    public RadiusMarkerHandler(GoogleMap mMap, LatLng latLng, boolean createRadiusMarker, double radius) {
        this.mMap = mMap;
        this.latLng = latLng;
        this.radius = radius;
        this.createStartingMarker();
        //this.addCircleWithConstantSize();
        this.currentZoom = mMap.getCameraPosition().zoom;
    }

    public void createStartingMarker(){
        if(radiusMarker == null) {
            co = new CircleOptions();
            co.center(latLng);
            co.fillColor(0x220000FF);
            co.strokeColor(Color.parseColor("#6699ff"));
            co.strokeWidth(5);
            co.radius(radius);
            radiusMarker = mMap.addCircle(co);
        }
    }

    public void updateMarkerRadius(double radius){
        radiusMarker.setRadius(radius);
    }

    public void removeMarker(){
        radiusMarker.remove();
    }

    public double calculateCircleRadiusMeterForMapCircle(final int _targetRadiusDip, final double _circleCenterLatitude, final float _currentMapZoom) {
        final double arbitraryValueForDip = radiusMarker.getRadius();
        final double oneDipDistance = Math.abs(Math.cos(Math.toRadians(_circleCenterLatitude))) * arbitraryValueForDip / Math.pow(2, _currentMapZoom);
        return oneDipDistance * (double) _targetRadiusDip;
    }

    public void addCircleWithConstantSize(){
        final GoogleMap googleMap = mMap;

        //Setting a listener on the map camera to monitor when the camera changes
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                if (position.zoom != currentZoom){
                    currentZoom = position.zoom;  // here you get zoom level

                    //Use the function to calculate the radius
                    final double newRadius = calculateCircleRadiusMeterForMapCircle((int)radius, co.getCenter().latitude, googleMap.getCameraPosition().zoom);
                    //Apply the radius to the circle
                    radiusMarker.setRadius(newRadius);
                }

            }
        });
    }

    public Circle getRadiusMarker() {
        return radiusMarker;
    }

    public LatLng getCirclePerimeterPoint() {
        return circlePerimeterPoint;
    }

    LatLng circlePerimeterPoint;
    public double calculateRadiusMarkerDistance(LatLng centre, double radius)
    {
        double EARTH_RADIUS = 6378100.0;
        double lat = centre.latitude * Math.PI / 180.0;
        double lon = centre.longitude * Math.PI / 180.0;

        double latPoint = lat + (radius / EARTH_RADIUS) * Math.sin(0);
        double lonPoint = lon + (radius / EARTH_RADIUS) * Math.cos(0) / Math.cos(lat);

        circlePerimeterPoint = new LatLng(latPoint * 180.0 / Math.PI, lonPoint * 180.0 / Math.PI);

        //mMap.addMarker(new MarkerOptions().position(circlePerimeterPoint));

        float[] distance = new float[2];
        Location.distanceBetween(circlePerimeterPoint.latitude, circlePerimeterPoint.longitude,
                centre.latitude, centre.longitude, distance);

        double distanceInMiles =  distance[0] * 0.000621371192;

        //return distance in miles with two dp
        return Math.floor(distanceInMiles * 100) / 100;
    }
}
