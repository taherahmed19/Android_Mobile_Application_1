package com.example.myapplication.Presenters.LocationSelectorPresenter;

import android.location.Location;

import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Interfaces.LocationSelectorContract.LocationSelectorContract;
import com.example.myapplication.Interfaces.MapListener.MapListener;
import com.example.myapplication.Interfaces.TokenExpirationListener.TokenExpirationListener;
import com.example.myapplication.Models.InteractiveMap.InteractiveMap;
import com.example.myapplication.Models.SelectedLocation.SelectedLocation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class LocationSelectorPresenter implements LocationSelectorContract.Presenter, MapListener, TokenExpirationListener {

    LocationSelectorContract.View view;
    SelectedLocation selectedLocation;
    InteractiveMap interactiveMap;

    public LocationSelectorPresenter(LocationSelectorContract.View view) {
        this.view = view;
        this.selectedLocation = new SelectedLocation();
    }

    @Override
    public void handleMapClick(GoogleMap googleMap) {
        this.view.handleMapClick(googleMap);
    }

    @Override
    public void handleMarkerClick(GoogleMap googleMap) {
        this.view.handleMarkersListener(googleMap);
    }

    @Override
    public void handleTokenExpiration() {
        this.view.handleTokenExpiration();
    }

    public void updateSelectedLocation(LatLng latLng){
        this.selectedLocation.setLatLng(latLng);
    }

    public LatLng getSelectedLocation(){
        return this.selectedLocation.getLatLng();
    }

    public void handleOnGoogleMapClick(GoogleMap mMap, LatLng latLng){
        this.view.handleOnGoogleMapClick(mMap, latLng);
    }

    public void handleSubmitButtonClick(){
        this.view.handleOnSubmitButtonClick();
    }

    public void handleReturnButtonClick(){
        this.view.handleOnReturnButtonClick();
    }

    public void requestMap(FragmentManager fragmentManager, SupportMapFragment supportMapFragment){
        this.interactiveMap = new InteractiveMap(supportMapFragment, this.view.getContext(), fragmentManager, this, this);
    }

    public void setUserLocationGoogleMarker(Location location) {
        interactiveMap.setUserLocationGoogleMarker(location);
    }
}
