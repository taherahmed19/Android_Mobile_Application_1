package com.example.myapplication.Presenters.MapPresenter;

import android.location.Location;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Handlers.InteractiveMap.InteractiveMap;
import com.example.myapplication.HttpRequest.HttpMap.HttpMap;
import com.example.myapplication.Interfaces.MapContract.MapContract;
import com.example.myapplication.Interfaces.MapListener.MapListener;
import com.example.myapplication.Models.Marker.Marker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MapPresenter implements MapContract.Presenter, MapListener {

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

    public void addMarkerList(ArrayList<Marker> markers){
        this.interactiveMap.addDataSetMarkers(markers);
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
        this.interactiveMap = new InteractiveMap(supportMapFragment, mapFragment.getActivity(), mapFragment.getChildFragmentManager(), this, mapFragment.getContext(), mapFragment);

        HttpMap httpMap = new HttpMap(mapFragment.getActivity(), mapFragment.getChildFragmentManager(), supportMapFragment, mapFragment, loadingSpinner, settings, customMarkerListener);
        httpMap.execute("https://10.0.2.2:443/api/getmarkers");

    }

    @Override
    public void handleMapClick(GoogleMap googleMap) {

    }

    @Override
    public void handleMarkerClick(GoogleMap googleMap, FragmentActivity fragmentActivity) {

    }
}
