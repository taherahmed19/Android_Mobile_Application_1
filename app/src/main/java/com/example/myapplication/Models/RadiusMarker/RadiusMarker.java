package com.example.myapplication.Models.RadiusMarker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.Webservice.HttpDeleteRadiusMarker.HttpDeleteRadiusMarker;
import com.example.myapplication.Webservice.HttpWriteRadiusMarker.HttpWriteRadiusMarker;
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

public class RadiusMarker {

    public static double INITIAL_RADIUS = 50;
    private final int FILL_COLOUR = 0x220000FF;
    private final int STROKE_WIDTH = 5;
    private final String STROKE_COLOUR = "#6699ff";

    private Circle radiusMarker;
    private GoogleMap mMap;
    private LatLng latLng;
    private float currentZoom;
    private CircleOptions co;
    private double radius;

    public RadiusMarker(GoogleMap mMap, LatLng latLng, double radius) {
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

    public void updateMarkerRadius(double radius){
        radiusMarker.setRadius(radius);
    }

    public void removeMarker(){
        radiusMarker.remove();
    }

    public Circle getRadiusMarker() {
        return radiusMarker;
    }
}
