package com.example.myapplication.Handlers.BackgroundNotificationHandler;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.myapplication.Interfaces.CurrentLocationListener.CurrentLocationListener;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Services.FirebaseService.TTSService.TTSService;
import com.example.myapplication.Utils.Distance.Distance;
import com.google.android.gms.maps.model.LatLng;

public class BackgroundNotificationHandler implements CurrentLocationListener {

    Context context;
    private String category;
    private String description;
    private double lat;
    private double lng;
    private LatLng currentLatLng;

    public BackgroundNotificationHandler(Context context, String category, String description, String lat, String lng) {
        this.context = context;
        this.category = category;
        this.description = description;
        this.lat = Double.parseDouble(lat);
        this.lng = Double.parseDouble(lng);
        CurrentLocation currentLocation = new CurrentLocation(this.context, this);
        currentLocation.startLocationUpdates();
    }

    @Override
    public void updateUserLocation(Location location) {
        this.currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        runTTS();
    }

    public void runTTS(){
        String message = createMessage();
        TTSService ttsService = new TTSService(context, message);
    }

    String createMessage(){
        String markerMessageTitle = "A new " + category + " marker has been added to your radius area.";
        String markerMessageBody = "With the details " + description;
        String distanceBetweenMarkerAndUser = "The marker is " + calculateDistance() + " metres from your current location";

        return markerMessageTitle + markerMessageBody + distanceBetweenMarkerAndUser;
    }

    String calculateDistance(){
        return String.valueOf(Distance.CalculatePointsDistance(currentLatLng.latitude, currentLatLng.longitude, lat, lng));
    }
}
