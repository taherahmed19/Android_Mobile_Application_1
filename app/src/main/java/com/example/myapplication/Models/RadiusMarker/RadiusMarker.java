package com.example.myapplication.Models.RadiusMarker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;

import com.example.myapplication.Interfaces.DeleteRadiusMarkerListener.DeleteRadiusMarkerListener;
import com.example.myapplication.Interfaces.SetRadiusMarkerListener.SetRadiusMarkerListener;
import com.example.myapplication.Interfaces.TokenExpirationListener.TokenExpirationListener;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Webservice.HttpDeleteRadiusMarker.HttpDeleteRadiusMarker;
import com.example.myapplication.Webservice.HttpWriteRadiusMarker.HttpWriteRadiusMarker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class RadiusMarker {

    public static double INITIAL_RADIUS = 50;

    private Circle radiusMarker;
    private GoogleMap mMap;
    private LatLng latLng;
    private CircleOptions co;
    private double radius;
    private double originalRadius;

    TokenExpirationListener tokenExpirationListener;

    public RadiusMarker(GoogleMap mMap, LatLng latLng, double radius, TokenExpirationListener tokenExpirationListener) {
        this.mMap = mMap;
        this.latLng = latLng;
        this.radius = radius;
        this.originalRadius = radius;
        this.setInitialRadius(radius);
        this.createStartingMarker();
        this.tokenExpirationListener = tokenExpirationListener;
    }

    public void setInitialRadius(double radius){
        if(radius == 0){
            this.radius = INITIAL_RADIUS;
        }
    }

    public void createStartingMarker(){
        int FILL_COLOUR = 0x220000FF;
        int STROKE_WIDTH = 5;
        String STROKE_COLOUR = "#6699ff";

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

    public void makeApiRequestWriteRadiusMarker(Context context, SetRadiusMarkerListener setRadiusMarkerListener,
                                                boolean inApp, boolean voice){
        int inAppClicked = 0;
        int voiceClicked = 0;

        if(inApp){
            inAppClicked = 1;
        }
        if(voice){
            voiceClicked = 1;
        }

        HttpWriteRadiusMarker httpWriteRadiusMarker = new HttpWriteRadiusMarker(context,
                LoginPreferenceData.getUserId(context), 1,
                String.valueOf(this.radiusMarker.getCenter().latitude),
                String.valueOf(this.radiusMarker.getCenter().longitude),
                String.valueOf(this.radiusMarker.getRadius()),
                inAppClicked,
                voiceClicked,
                setRadiusMarkerListener, tokenExpirationListener);
        httpWriteRadiusMarker.execute();
    }

    public void makeApiRequestDeleteRadiusMarker(Context context, DeleteRadiusMarkerListener deleteRadiusMarkerListener){
        HttpDeleteRadiusMarker httpDeleteRadiusMarker = new HttpDeleteRadiusMarker(context,
                LoginPreferenceData.getUserId(context), deleteRadiusMarkerListener, tokenExpirationListener);
        httpDeleteRadiusMarker.execute();
    }

    public boolean handleRadiusMarkerClick(Context context, LatLng latLng, LatLng centreLatLng, double radius){
        SharedPreferences settingsPreference = Objects.requireNonNull(context).getSharedPreferences("Radius_Marker_Settings", 0);
        boolean stateExists = settingsPreference.getBoolean("stateExists", false);

        if(stateExists){
            float[] distance = new float[2];
            Location.distanceBetween(latLng.latitude, latLng.longitude, centreLatLng.latitude, centreLatLng.longitude, distance);

            return distance[0] <= radius;
        }

        return false;
    }

    public void saveRadiusMarkerSettings(Context context, boolean inApp, boolean voice, boolean state){
        SharedPreferences settingsPreference = Objects.requireNonNull(context).getSharedPreferences("Radius_Marker_Settings", 0);
        SharedPreferences.Editor mapStateEditor = settingsPreference.edit();
        mapStateEditor.putBoolean("stateExists", state);
        mapStateEditor.putBoolean("inAppNotifications", inApp);
        mapStateEditor.putBoolean("voiceNotifications", voice);
        mapStateEditor.apply();
    }

    public void removeMarker(){
        radiusMarker.remove();
    }

    public Circle getRadiusMarker() {
        return radiusMarker;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public double getRadius() {
        return radius;
    }

    public void updateRadius(double radius){
        this.radiusMarker.setRadius(radius);
        this.radius = radius;
    }

    public double getOriginalRadius() {
        return originalRadius;
    }

    public void setOriginalRadius(double originalRadius) {
        this.originalRadius = originalRadius;
    }
}
