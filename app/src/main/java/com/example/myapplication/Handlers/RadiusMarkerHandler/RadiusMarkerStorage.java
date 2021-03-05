package com.example.myapplication.Handlers.RadiusMarkerHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.myapplication.Fragments.CustomMarkerBottomSheetFragment.CustomMarkerBottomSheetFragment;
import com.example.myapplication.Fragments.ErrorFragment.ErrorFragment;
import com.example.myapplication.HttpRequest.HttpDeleteRadiusMarker.HttpDeleteRadiusMarker;
import com.example.myapplication.HttpRequest.HttpWriteRadiusMarker.HttpWriteRadiusMarker;
import com.example.myapplication.Interfaces.DeleteRadiusMarkerListener.DeleteRadiusMarkerListener;
import com.example.myapplication.Interfaces.SetRadiusMarkerListener.SetRadiusMarkerListener;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class RadiusMarkerStorage {

    RadiusMarkerHandler radiusMarkerHandler;
    DeleteRadiusMarkerListener deleteRadiusMarkerListener;
    SetRadiusMarkerListener setRadiusMarkerListener;
    Context context;

    public RadiusMarkerStorage(RadiusMarkerHandler radiusMarkerHandler, DeleteRadiusMarkerListener deleteRadiusMarkerListener,
                               SetRadiusMarkerListener setRadiusMarkerListener, Context context) {
        this.radiusMarkerHandler = radiusMarkerHandler;
        this.deleteRadiusMarkerListener = deleteRadiusMarkerListener;
        this.setRadiusMarkerListener = setRadiusMarkerListener;
        this.context = context;
        this.removeExistingData();
    }

    public void writeRadiusMarkerDb(){
        HttpWriteRadiusMarker httpWriteRadiusMarker = new HttpWriteRadiusMarker(context,
                LoginPreferenceData.getUserId(context), 1,
                String.valueOf(this.radiusMarkerHandler.getRadiusMarker().getCenter().latitude),
                String.valueOf(this.radiusMarkerHandler.getRadiusMarker().getCenter().longitude),
                String.valueOf(this.radiusMarkerHandler.getRadiusMarker().getRadius()),
                this.setRadiusMarkerListener);
        httpWriteRadiusMarker.execute();
    }

    public void deleteRadiusMarkerDb(){
        HttpDeleteRadiusMarker httpDeleteRadiusMarker = new HttpDeleteRadiusMarker(context,
                LoginPreferenceData.getUserId(context), this.deleteRadiusMarkerListener, radiusMarkerHandler);
        httpDeleteRadiusMarker.execute();
    }

    public void saveSharedPreference(boolean inAppButtonClicked, boolean voiceButtonClicked, LatLng latLng){
        SharedPreferences settingsPreference = Objects.requireNonNull(context).getSharedPreferences("Radius_Marker_Settings", 0);
        SharedPreferences.Editor preferenceEditor = settingsPreference.edit();
        preferenceEditor.putBoolean("stateExists", true);
        preferenceEditor.putBoolean("inAppNotifications", inAppButtonClicked);
        preferenceEditor.putBoolean("voiceNotifications", voiceButtonClicked);
        preferenceEditor.putFloat("radius", (float) radiusMarkerHandler.getRadiusMarker().getRadius());
        preferenceEditor.putFloat("centerLat", (float) latLng.latitude);
        preferenceEditor.putFloat("centerLon", (float) latLng.longitude);
        preferenceEditor.apply();
    }

    public void clearSharedPreference(){
        context.getSharedPreferences("Radius_Marker_Settings", 0).edit().clear().apply();
    }

    void removeExistingData(){
        SharedPreferences settingsPreference = Objects.requireNonNull(context).getSharedPreferences("Radius_Marker_Settings", 0);
        boolean stateExists = settingsPreference.getBoolean("stateExists", false);

        if(stateExists){
            deleteRadiusMarkerDb();
            clearSharedPreference();
        }
    }
}
