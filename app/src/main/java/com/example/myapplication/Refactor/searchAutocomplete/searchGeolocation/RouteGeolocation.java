package com.example.myapplication.Refactor.searchAutocomplete.searchGeolocation;

import com.tomtom.online.sdk.common.location.LatLng;

public class RouteGeolocation {

    LatLng latLng;

    public RouteGeolocation(LatLng latLng) {
        this.latLng = latLng;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
