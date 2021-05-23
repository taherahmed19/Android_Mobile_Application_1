package com.example.myapplication.Presenters.MapPresenter;

import android.location.Location;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Models.InteractiveMap.InteractiveMap;
import com.example.myapplication.HttpRequest.HttpMap.HttpMap;
import com.example.myapplication.Interfaces.CustomMarkerListener.CustomMarkerListener;
import com.example.myapplication.Interfaces.MapContract.MapContract;
import com.example.myapplication.Interfaces.MapListener.MapListener;
import com.example.myapplication.Models.Marker.Marker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MapPresenter implements MapContract.Presenter, MapListener, CustomMarkerListener {

    MapContract.View view;
    InteractiveMap interactiveMap;

    public MapPresenter(MapContract.View view) {
        this.view = view;
    }

    public void handleSearchButtonClick(){
        view.handleSearchButtonClick();
    }

    public void handleNewPostButtonClick(){
        view.handleNewPostButtonClick();
    }

    public void setCameraCurrentLocation(){
        interactiveMap.moveCameraToCurrentLocation();
    }

    public void setGoogleMakerLocation(Location location){
        this.interactiveMap.setUserLocationGoogleMarker(location);
    }

    public void setMapLocation(LatLng latLng){
        this.interactiveMap.setMapLocation(latLng);
    }

    public boolean handleMarkerClick(com.google.android.gms.maps.model.Marker marker){
        return this.view.handleMarkerClick(marker);
    }

    public void addMarkerToMap(ViewPager viewPager, Marker markerModel){
        interactiveMap.triggerMarkerOnMap(viewPager, markerModel);
    }

    public void handleMapSavedLocation(LatLng latLng){
        if(latLng != null){
            if(interactiveMap != null){
                interactiveMap.setMapLocation(latLng);
                view.hideSpinner();
            }
        }
    }

    public void requestMap(FragmentManager fragmentManager, SupportMapFragment supportMapFragment){
        this.interactiveMap = new InteractiveMap(supportMapFragment, view.getApplicationContext(), fragmentManager, this);

        HttpMap httpMap = new HttpMap(view.getLoadingSpinner(), view.getApplicationContext(), this);
        httpMap.execute("https://10.0.2.2:443/api/getmarkers");

    }

    @Override
    public void handleMapClick(GoogleMap googleMap) {
    }

    @Override
    public void handleMarkerClick(GoogleMap googleMap) {
        view.addMarkersListener(googleMap);
    }

    @Override
    public void addMarkerData(ArrayList<Marker> markers) {
        this.interactiveMap.addDataSetMarkers(markers);
    }
}