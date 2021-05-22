package com.example.myapplication.Interfaces.MapContract;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public interface MapContract {

    interface View{
        void handleSearchButtonClick();
        void handleNewPostButtonClick();
        void hideSpinner();
        boolean handleMarkerClick(com.google.android.gms.maps.model.Marker marker);
    }

    interface Presenter{

    }

    interface Model{

    }

}
