package com.example.myapplication.Interfaces.MapContract;

import android.content.Context;

import com.example.myapplication.Models.LoadingSpinner.LoadingSpinner;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public interface MapContract {

    interface View{
        void handleRadiusMarkerRemoval(Boolean valid);
        void openBottomSheetWithState(GoogleMap mMap, LatLng latLng);
        void createBottomSheetFragment(GoogleMap mMap, LatLng latLng);
        void handleRadiusMarkerMapClick(GoogleMap mMap);
        void handleSearchButtonClick();
        void handleNewPostButtonClick();
        void hideSpinner();
        void addMarkersListener(GoogleMap mMap);
        boolean handleMarkerClick(com.google.android.gms.maps.model.Marker marker);
        Context getApplicationContext();
        LoadingSpinner getLoadingSpinner();
    }

    interface Presenter{

    }

    interface Model{

    }

}
