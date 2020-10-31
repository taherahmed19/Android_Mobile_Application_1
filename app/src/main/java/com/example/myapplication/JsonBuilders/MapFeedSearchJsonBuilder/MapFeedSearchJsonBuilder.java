package com.example.myapplication.JsonBuilders.MapFeedSearchJsonBuilder;

import android.util.Log;


import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapFeedSearchJsonBuilder {

    public LatLng jsonHandler(String sourceString){
        LatLng latLng = getGeocoordinates(sourceString);

        return latLng;
    }

    LatLng getGeocoordinates(String jsonString){
        LatLng latLng = null;

        try {
            JSONObject routeGeolocation = new JSONObject(jsonString);
            JSONArray results = routeGeolocation.getJSONArray("results");

            if(results.length() != 0){
                JSONObject geometry = results.getJSONObject(0).getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");

                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");

                latLng = new LatLng(lat, lng);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return latLng;
    }
}
