package com.example.myapplication.Presenters.MapPresenter;

import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Fragments.BottomSheetFragment.BottomSheetFragment;
import com.example.myapplication.Interfaces.DeleteRadiusMarkerListener.DeleteRadiusMarkerListener;
import com.example.myapplication.Models.InteractiveMap.InteractiveMap;
import com.example.myapplication.Models.RadiusMarker.RadiusMarker;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Webservice.HttpGetRadiusMarker.HttpGetRadiusMarker;
import com.example.myapplication.Webservice.HttpMap.HttpMap;
import com.example.myapplication.Interfaces.CustomMarkerListener.CustomMarkerListener;
import com.example.myapplication.Interfaces.MapContract.MapContract;
import com.example.myapplication.Interfaces.MapListener.MapListener;
import com.example.myapplication.Models.Marker.Marker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Objects;

public class MapPresenter implements MapContract.Presenter, MapListener, CustomMarkerListener, DeleteRadiusMarkerListener {

    MapContract.View view;
    InteractiveMap interactiveMap;
    RadiusMarker radiusMarker;

    public MapPresenter(MapContract.View view) {
        this.view = view;
    }

    @Override
    public void handleMapClick(GoogleMap googleMap) {
    }

    @Override
    public void handleMarkerClick(GoogleMap googleMap) {
        view.addMarkersListener(googleMap);
    }

    @Override
    public void detectRadiusMarker(){
        this.view.handleRadiusMarkerMapClick(this.interactiveMap.getMap());
    }

    @Override
    public void addMarkerData(ArrayList<Marker> markers) {
        this.interactiveMap.addDataSetMarkers(markers);
    }

    @Override
    public void renderRadiusMarker(double lat, double lon, double radius, boolean inApp, boolean voice){
        this.radiusMarker = new RadiusMarker(this.interactiveMap.getMap(), new LatLng(lat, lon), radius);
        this.radiusMarker.saveRadiusMarkerSettings(this.view.getApplicationContext(), lat, lon, radius, inApp, voice);
    }

    @Override
    public void handleRadiusMarkerRemoval(boolean valid) {
        this.view.handleRadiusMarkerRemoval(valid);
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
        boolean mapLocationSet = this.interactiveMap.handleMapSavedLocation(latLng);
        if(mapLocationSet){
            view.hideSpinner();
        }
    }

    public void makeApiRequestForGoogleMap(FragmentManager fragmentManager, SupportMapFragment supportMapFragment){
        this.interactiveMap = new InteractiveMap(supportMapFragment, view.getApplicationContext(), fragmentManager, this);
        this.interactiveMap.makeApiRequestForMap(view.getLoadingSpinner(), view.getApplicationContext(), this);
    }

    public void createRadiusMarker(LatLng latLng){
        SharedPreferences settingsPreference = Objects.requireNonNull(this.view.getApplicationContext()).getSharedPreferences("Radius_Marker_Settings", 0);
        boolean stateExists = settingsPreference.getBoolean("stateExists", false);

        if(stateExists){
            radiusMarker.makeApiRequestDeleteRadiusMarker(this.view.getApplicationContext(), this);
            SharedPreferences.Editor mapStateEditor = settingsPreference.edit();
            mapStateEditor.putBoolean("stateExists", false);
            mapStateEditor.putBoolean("inAppNotifications", true);
            mapStateEditor.putBoolean("voiceNotifications", false);
            mapStateEditor.apply();
        }

        if(radiusMarker != null){
            radiusMarker.removeMarker();
        }
        radiusMarker = new RadiusMarker(this.interactiveMap.getMap(), latLng, 0);
    }

    public void createBottomSheetFragment(LatLng latLng){
        this.view.createBottomSheetFragment(this.interactiveMap.getMap(), latLng);
    }

    public void openBottomSheetWithState(GoogleMap mMap, LatLng latLng){
        this.view.openBottomSheetWithState(mMap, latLng);
    }

    public boolean handleRadiusMarkerClick(LatLng latLng){
        return radiusMarker.handleRadiusMarkerClick(this.view.getApplicationContext(), latLng, this.radiusMarker.getLatLng(), this.radiusMarker.getRadius());
    }

    public RadiusMarker getRadiusMarker(){
        return this.radiusMarker;
    }

    public void makeApiRequestGetRadiusMarker(){
        this.interactiveMap.makeApiRequestGetRadiusMarker(this.view.getApplicationContext(), this);
    }
}
