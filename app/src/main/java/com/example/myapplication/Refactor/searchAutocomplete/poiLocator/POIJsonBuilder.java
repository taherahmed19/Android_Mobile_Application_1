package com.example.myapplication.Refactor.searchAutocomplete.poiLocator;

import com.example.myapplication.Refactor.searchAutocomplete.searchGeolocation.Route;
import com.example.myapplication.Refactor.searchAutocomplete.searchGeolocation.RouteGeolocation;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class POIJsonBuilder {

    public Route buildJSON(String jsonString, LatLng current){
        Route route = null;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray results = jsonObject.getJSONArray("results");

            JSONObject firstResult = (JSONObject)results.get(0);
            JSONObject geometry = firstResult.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");

            Double lat = location.getDouble("lat");
            Double lon = location.getDouble("lng");

            RouteGeolocation source = new RouteGeolocation(new com.tomtom.online.sdk.common.location.LatLng(current.latitude, current.longitude));
            RouteGeolocation destination = new RouteGeolocation(new com.tomtom.online.sdk.common.location.LatLng(lat, lon));

            route = new Route(source, destination);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return route;
    }
}
