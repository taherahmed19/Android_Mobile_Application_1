package com.example.myapplication.Refactor.searchAutocomplete.searchGeolocation;

import android.util.Log;

import com.tomtom.online.sdk.common.location.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchJsonBuilder {

    Route route;

    public SearchJsonBuilder() {
        this.route = null;
    }

    public Route buildJSON(String jsonStringSource, String jsonStringDestination){
        LatLng source = addRouteGeocoordinates(jsonStringSource);
        LatLng destination = addRouteGeocoordinates(jsonStringDestination);

        RouteGeolocation sourceGeolocation = new RouteGeolocation(source);
        RouteGeolocation destinationGeolocation = new RouteGeolocation(destination);

        this.route = new Route(sourceGeolocation, destinationGeolocation);

        Log.d("Print", "Source " + this.route.getSource().getLatLng());
        Log.d("Print", "Destination " + this.route.getDestination().getLatLng());

        return this.route;
    }

    LatLng addRouteGeocoordinates(String jsonString){
        LatLng latLng = null;

        try {
            JSONObject routeGeolocation = new JSONObject(jsonString);

            JSONArray results = routeGeolocation.getJSONArray("results");

            JSONObject geometry = results.getJSONObject(0).getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            double lat = location.getDouble("lat");
            double lng = location.getDouble("lng");

            latLng = new LatLng(lat, lng);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return latLng;
    }
}
