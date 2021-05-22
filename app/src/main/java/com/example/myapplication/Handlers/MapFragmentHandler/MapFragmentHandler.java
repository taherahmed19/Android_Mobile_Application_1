package com.example.myapplication.Handlers.MapFragmentHandler;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Adapters.LockableViewPager.LockableViewPager;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Fragments.MapFeedSearchFragment.MapFeedSearchFragment;
import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.Fragments.FormFragment.FormFragment;
import com.example.myapplication.Handlers.MapFeedSearchFragmentHandler.MapFeedSearchFragmentHandler;
import com.example.myapplication.Fragments.MapFeedSearchAutocompleteFragment.MapFeedSearchAutocompleteFragment;
import com.example.myapplication.Handlers.InteractiveMap.InteractiveMap;
import com.example.myapplication.HttpRequest.HttpMap.HttpMap;
import com.example.myapplication.Interfaces.CustomMarkerListener.CustomMarkerListener;
import com.example.myapplication.Interfaces.MapListener.MapListener;
import com.example.myapplication.Models.LoadingSpinner.LoadingSpinner;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.Settings.Settings;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.Utils.StringConstants.StringConstants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class MapFragmentHandler  {

    ImageButton mapSearchButton;
    MapFragment mapFragment;
    MapFeedSearchAutocompleteFragment mapFeedSearchAutocompleteFragment;
    MapFeedSearchFragment mapFeedSearchFragment;
    LockableViewPager viewPager;
    LoadingSpinner loadingSpinner;
    SupportMapFragment supportMapFragment;
    HttpMap httpMap;
    InteractiveMap interactiveMap;
    CustomMarkerListener customMarkerListener;
    MapListener mapListenerInstance;

    public MapFragmentHandler(MapFragment mapFragment, LockableViewPager viewPager, MapListener mapListenerInstance, CustomMarkerListener customMarkerListener) {
        this.mapFragment = mapFragment;
        this.mapFeedSearchAutocompleteFragment = new MapFeedSearchAutocompleteFragment(mapFragment);
        this.mapFeedSearchFragment = new MapFeedSearchFragment(mapFragment);
        this.viewPager = viewPager;
        this.mapListenerInstance = mapListenerInstance;
        this.customMarkerListener = customMarkerListener;
    }

    public void requestMap(Settings settings){
        this.interactiveMap = new InteractiveMap(supportMapFragment, mapFragment.getActivity(), mapFragment.getChildFragmentManager(), mapListenerInstance, mapFragment.getContext(), mapFragment);

        this.httpMap = new HttpMap(mapFragment.getActivity(), mapFragment.getChildFragmentManager(), supportMapFragment, mapFragment, loadingSpinner, settings, customMarkerListener);
        this.httpMap.execute("https://10.0.2.2:443/api/getmarkers");
    }

    public void addMarkerData(ArrayList<Marker> markers) {
        this.interactiveMap.addDataSetMarkers(markers);
    }

    public void updateUserLocation(Location location) {
        this.interactiveMap.setUserLocationGoogleMarker(location);
    }

    public void setMapLocation(LatLng latLng){
        this.interactiveMap.setMapLocation(latLng);
    }

    public void addMarkersListener(GoogleMap mMap, FragmentActivity fragmentActivity){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                if(!marker.getTag().equals(fragmentActivity.getString(R.string.default_constant))){
                    Marker item = (Marker)marker.getTag();

                    MarkerModalFragment markerModalFragment = new MarkerModalFragment(item, viewPager);
                    FragmentTransition.Transition(fragmentActivity.getSupportFragmentManager(), markerModalFragment, R.anim.right_animations, R.anim.left_animation,
                            R.id.mapModalContainer, "");

                    return true;
                }
                return false;
            }
        });
    }

    public void triggerMarkerOnMap(ViewPager viewPager, Marker markerModel){
        interactiveMap.triggerMarkerOnMap(viewPager, markerModel);
    }

    public void handleSavedLocation(LatLng latLng){
        if(latLng != null){
            if(interactiveMap != null){
                interactiveMap.setMapLocation(latLng);
                hideSpinner();
            }
        }
    }

    public void showSpinner(){
        if(this.loadingSpinner != null){
            this.loadingSpinner.show();
        }
    }

    public void hideSpinner(){
        if(this.loadingSpinner != null){
            this.loadingSpinner.hide();
        }
    }

    public void showFeedMapContainer(){
        LinearLayout feedMapContainer = (LinearLayout) mapFragment.getView().findViewById(R.id.feedMapContainer);
        feedMapContainer.setVisibility(View.VISIBLE);
    }

    public LatLng handleResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            if (requestCode == MapFeedSearchFragmentHandler.REQUEST_CODE_SEARCH){
                return (LatLng)data.getParcelableExtra(StringConstants.INTENT_MAP_FEED_SEARCH_LAT_LNG);
            }
        }

        return null;
    }

    public Context getContext(){
        return this.mapFragment.getContext();
    }

    public MapFeedSearchAutocompleteFragment getMapFeedSearchAutocompleteFragment() {
        return mapFeedSearchAutocompleteFragment;
    }

    public MapFeedSearchFragment getMapFeedSearchFragment() {
        return mapFeedSearchFragment;
    }



}
