package com.example.myapplication.Interfaces.LocationSelectorContract;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public interface LocationSelectorContract {

    interface View{
        void handleTokenExpiration();
        void handleMapClick(GoogleMap googleMap);
        void handleMarkersListener(GoogleMap googleMap);
        void handleOnGoogleMapClick(GoogleMap mMap, LatLng latLng);
        void handleOnSubmitButtonClick();
        void handleOnReturnButtonClick();
        Context getContext();
    }

    interface Presenter{

    }

    interface Model{

    }

}
