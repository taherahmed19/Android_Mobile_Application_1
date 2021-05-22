package com.example.myapplication.Models.SelectedLocation;

import com.example.myapplication.Interfaces.LocationSelectorContract.LocationSelectorContract;
import com.google.android.gms.maps.model.LatLng;

public class SelectedLocation implements LocationSelectorContract.Model {

    LatLng latLng;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
