package com.example.myapplication.Interfaces.SearchListener;

import com.google.android.gms.maps.model.LatLng;

public interface SearchListener {

    void popFragments();
    void returnFragmentData(LatLng latLng);
}
