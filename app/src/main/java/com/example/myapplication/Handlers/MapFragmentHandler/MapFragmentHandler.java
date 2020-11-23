package com.example.myapplication.Handlers.MapFragmentHandler;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Adapters.LockableViewPager.LockableViewPager;
import com.example.myapplication.Fragments.MapFilterFragment.MapFilterFragment;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Fragments.MapFeedSearchFragment.MapFeedSearchFragment;
import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.Fragments.UserFeedFormFragment.UserFeedFormFragment;
import com.example.myapplication.Handlers.MapFeedSearchFragmentHandler.MapFeedSearchFragmentHandler;
import com.example.myapplication.Fragments.MapFeedSearchAutocompleteFragment.MapFeedSearchAutocompleteFragment;
import com.example.myapplication.Handlers.MapHandler.MapHandler;
import com.example.myapplication.HttpRequest.HttpMap.HttpMap;
import com.example.myapplication.Interfaces.CustomMarkerListener;
import com.example.myapplication.Interfaces.MapListener;
import com.example.myapplication.Models.LoadingSpinner.LoadingSpinner;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.Settings.Settings;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.Utils.StringConstants.StringConstants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

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
    MapHandler mapHandler;

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

    public void configureElements(){
        this.configureSupportMapFragment();
        this.configureSpinner();
        this.configureMapSearchButton();
        this.configureNewPostButton();
        this.configureFilterButton();
        this.configureSwitchButton();
    }

    public void requestMap(Settings settings){
        this.mapHandler = new MapHandler(supportMapFragment, mapFragment.getActivity(), mapFragment.getChildFragmentManager(), mapListenerInstance);

        this.httpMap = new HttpMap(mapFragment.getActivity(), mapFragment.getChildFragmentManager(), supportMapFragment, mapFragment, loadingSpinner, settings, customMarkerListener);
        this.httpMap.execute("https://10.0.2.2:443/api/getmarkers");
    }

    public void addMarkerData(ArrayList<Marker> markers) {
        this.mapHandler.addDataSetMarkers(markers);
    }

    public void updateUserLocation(Location location) {
        this.mapHandler.setUserLocationGoogleMarker(location);
    }

    public void addMarkersListener(GoogleMap mMap, FragmentActivity fragmentActivity){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                if(!marker.getTag().equals(fragmentActivity.getString(R.string.default_constant))){
                    Marker item = (Marker)marker.getTag();
                    MarkerModalFragment markerModalFragment = new MarkerModalFragment(item);
                    FragmentTransition.Transition(fragmentActivity.getSupportFragmentManager(), markerModalFragment, R.anim.right_animations, R.anim.left_animation,
                            R.id.mapModalContainer, "");

                    return true;
                }
                return false;
            }
        });
    }

    public void setGoogleMapClickable(GoogleMap mMap){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng loc) {
                return;
            }
        });
    }

    public void handleSavedLocation(LatLng latLng){
        if(latLng != null){
            if(mapHandler != null){
                mapHandler.setMapLocation(latLng);
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

    void configureSupportMapFragment(){
        supportMapFragment = (SupportMapFragment) mapFragment.getChildFragmentManager().findFragmentById(R.id.mapFragment);
    }

    void configureSpinner(){
        this.loadingSpinner = new LoadingSpinner((ProgressBar) mapFragment.getView().findViewById(R.id.feedLoadingSpinner));
    }

    void configureMapSearchButton(){
        this.mapSearchButton = (ImageButton) mapFragment.getView().findViewById(R.id.mapSearchButton);

        this.mapSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapFragment.getFragmentManager() != null) {
                    FragmentTransition.TransitionActivityResult(mapFragment.getFragmentManager(), mapFeedSearchFragment,
                            mapFragment, R.anim.top_animation, R.anim.down_animation, R.id.mapFeedSearchPointer, MapFeedSearchFragmentHandler.REQUEST_CODE_SEARCH, MapFeedSearchFragment.TAG);

                    FragmentTransition.Transition(mapFragment.getFragmentManager(), mapFeedSearchAutocompleteFragment,
                            R.anim.right_animations, R.anim.left_animation, R.id.mapFeedSearchAutoPointer, MapFeedSearchAutocompleteFragment.TAG);
                }
            }
        });
    }

    void configureNewPostButton(){
        final ImageButton newPost = (ImageButton) mapFragment.getView().findViewById(R.id.newPost);
        newPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserFeedFormFragment fragment = new UserFeedFormFragment(mapFragment.getFragmentManager(), viewPager);
                FragmentTransaction transition = FragmentTransition.Transition(mapFragment.getFragmentManager(), fragment, R.anim.right_animations, R.anim.left_animation, R.id.userFeedFormPointer, "");
            }
        });
    }

    void configureSwitchButton(){
        final ImageButton viewSwitch = (ImageButton) mapFragment.getView().findViewById(R.id.viewSwitch);

        viewSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentFragment = viewPager.getCurrentItem();

                switch (currentFragment){
                    case 0:
                        viewPager.setCurrentItem(1);
                        break;
                    case 1:
                        viewPager.setCurrentItem(0);
                        break;
                }
            }
        });
    }

    void configureFilterButton(){
        ImageButton filterButton = (ImageButton) mapFragment.getView().findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpMap.saveMapCameraPosition();

                MapFilterFragment fragment = (MapFilterFragment) mapFragment.getFragmentManager().findFragmentByTag(MapFilterFragment.TAG);
                if (fragment == null) {
                    fragment = new MapFilterFragment(mapFragment.getFragmentManager(), viewPager,  mapFragment);
                }

                FragmentTransaction transition = FragmentTransition.Transition(mapFragment.getFragmentManager(), fragment,
                        R.anim.right_animations, R.anim.left_animation, R.id.userFeedFormPointer, MapFilterFragment.TAG);
            }
        });
    }
}
