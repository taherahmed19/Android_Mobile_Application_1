package com.example.myapplication.Handlers.RadiusMarkerHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.util.Log;
import android.widget.SeekBar;

import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.CustomMarkerBottomSheetFragment.CustomMarkerBottomSheetFragment;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
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
import java.util.Objects;

public class RadiusMarkerHandler {

    public static double INITIAL_RADIUS = 50;
    private final int FILL_COLOUR = 0x220000FF;
    private final int STROKE_WIDTH = 5;
    private final String STROKE_COLOUR = "#6699ff";

    private LatLng circlePerimeterPoint;
    private Circle radiusMarker;
    private GoogleMap mMap;
    private LatLng latLng;
    private float currentZoom;
    private CircleOptions co;
    private double radius;

    public RadiusMarkerHandler(GoogleMap mMap, LatLng latLng, double radius) {
        this.mMap = mMap;
        this.latLng = latLng;
        this.radius = radius;
        this.createRadiusMarker(radius);
        this.createStartingMarker();
        this.currentZoom = mMap.getCameraPosition().zoom;
    }

    public void createRadiusMarker(double radius){
        if(radius == 0){
            this.radius = INITIAL_RADIUS;
        }
    }

    public void createStartingMarker(){
        if(radiusMarker == null) {
            co = new CircleOptions();
            co.center(latLng);
            co.fillColor(FILL_COLOUR);
            co.strokeColor(Color.parseColor(STROKE_COLOUR));
            co.strokeWidth(STROKE_WIDTH);
            co.radius(radius);
            radiusMarker = mMap.addCircle(co);
        }
    }

    public boolean handleRadiusMarkerClick(CustomMarkerBottomSheetFragment customMarkerBottomSheetDialog, Context context, FragmentManager supportFragmentManager, LatLng latLng){
        SharedPreferences settingsPreference = Objects.requireNonNull(context).getSharedPreferences("Radius_Marker_Settings", 0);
        boolean stateExists = settingsPreference.getBoolean("stateExists", false);
        double radius = (double)settingsPreference.getFloat("radius", 0.0f);
        double centerLat = (double)settingsPreference.getFloat("centerLat", 0.0f);
        double centerLon = (double)settingsPreference.getFloat("centerLon", 0.0f);

        if(stateExists){
            float[] distance = new float[2];
            Location.distanceBetween(latLng.latitude, latLng.longitude, centerLat, centerLon, distance);

            if(distance[0] <= radius){
                return true;
            }
        }

        return false;
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
