package com.example.myapplication.Utils.Distance;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class Distance {
    /**
     *
     * @param startingLat
     * @param startingLng
     * @param destinationLat
     * @param destinationLng
     * @return
     */
    public static double CalculatePointsDistance(double startingLat, double startingLng, double destinationLat, double destinationLng){
        float[] distance = new float[1];
        Location.distanceBetween(startingLat, startingLng, destinationLat, destinationLng, distance);

        return Math.floor(distance[0] * 100) / 100;
   }
}
