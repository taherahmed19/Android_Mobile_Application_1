package com.example.myapplication.Interfaces.LocationSelectorMapListener;

import com.google.android.gms.maps.GoogleMap;

public interface LocationSelectorMapListener {

    void handleMapClick(GoogleMap googleMap);
    void handleMarkerClick(GoogleMap googleMap);
}
