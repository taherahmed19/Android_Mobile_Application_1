package com.example.myapplication.Utils.Distance;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class Distance {

    /**
     * No longer used
     * @param centre
     * @param radius
     * @return
     */
    public static int CalculateRadiusMarkerDistance(LatLng centre, double radius)
    {
        double EARTH_RADIUS = 6378100.0;
        double lat = centre.latitude * Math.PI / 180.0;
        double lon = centre.longitude * Math.PI / 180.0;

        double latPoint = lat + (radius / EARTH_RADIUS) * Math.sin(0);
        double lonPoint = lon + (radius / EARTH_RADIUS) * Math.cos(0) / Math.cos(lat);

        LatLng circlePerimeterPoint = new LatLng(latPoint * 180.0 / Math.PI, lonPoint * 180.0 / Math.PI);

        float[] distance = new float[2];
        Location.distanceBetween(circlePerimeterPoint.latitude, circlePerimeterPoint.longitude,
                centre.latitude, centre.longitude, distance);

        return Math.round(distance[0]);
    }

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
