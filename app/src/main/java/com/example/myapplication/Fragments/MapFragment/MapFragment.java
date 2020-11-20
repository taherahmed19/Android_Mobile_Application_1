package com.example.myapplication.Fragments.MapFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myapplication.Adapters.LockableViewPager.LockableViewPager;
import com.example.myapplication.Fragments.MapFeedSearchAutocompleteFragment.MapFeedSearchAutocompleteFragment;
import com.example.myapplication.Fragments.MapFeedSearchFragment.MapFeedSearchFragment;
import com.example.myapplication.Fragments.MapFilterFragment.MapFilterFragment;
import com.example.myapplication.Handlers.MapFeedFragmentHandler.MapFeedFragmentHandler;
import com.example.myapplication.Handlers.MapHandler.MapHandler;
import com.example.myapplication.HttpRequest.HttpMap.HttpMap;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Models.LoadingSpinner.LoadingSpinner;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.Settings.Settings;
import com.example.myapplication.R;
import com.example.myapplication.Refactor.searchAutocomplete.Place;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MapFragment extends Fragment implements MapFeedSearchFragment.FragmentSearchListener, MapFeedSearchAutocompleteFragment.FragmentAutocompleteListener
                                                     , MapFilterFragment.FragmentMapFilterListener {
    SupportMapFragment supportMapFragment;

    LoadingSpinner loadingSpinner;
    MapFeedFragmentHandler mapFeedFragmentHandler;

    LockableViewPager viewPager;

    CurrentLocation currentLocation;

    public interface MarkerListener {
        void addMarkerData(ArrayList<Marker> markers);
    }

    public interface CurrentLocationListener {
        void updateUserLocation(Location location);
    }

    public MapFragment(LockableViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mapFeedFragmentHandler = new MapFeedFragmentHandler(this, viewPager);
        this.supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        this.loadingSpinner = new LoadingSpinner((ProgressBar) getView().findViewById(R.id.feedLoadingSpinner));
        this.mapFeedFragmentHandler.configureElements();

        this.mapFeedFragmentHandler.setCurrentLocationListenerListener((CurrentLocationListener) mapFeedFragmentHandler);
        this.mapFeedFragmentHandler.setListener((MarkerListener) mapFeedFragmentHandler);

        currentLocation = new CurrentLocation(getActivity(), this.mapFeedFragmentHandler.getCurrentLocationListener());
        this.mapFeedFragmentHandler.requestMap(null);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mapFeedFragmentHandler.showSpinner();

        LatLng latLng = this.mapFeedFragmentHandler.handleResult(requestCode, resultCode, data);
        this.mapFeedFragmentHandler.handleSavedLocation(latLng);
    }

    public void showFeedMapContainer(){
        this.mapFeedFragmentHandler.showFeedMapContainer();
    }

    @Override
    public void onInputAutocompleteSent(CharSequence input) {
        this.mapFeedFragmentHandler.getMapFeedSearchFragment().updateEditText(input);
    }

    @Override
    public void onInputSearchSent(CharSequence input) {
        this.mapFeedFragmentHandler.getMapFeedSearchAutocompleteFragment().updateEditText(input);
    }

    @Override
    public void onSearchTextChanged(Place place, String mainText, String secondText) {
        this.mapFeedFragmentHandler.getMapFeedSearchAutocompleteFragment().buildAutocompleteSearchItem(place, mainText, secondText);
    }

    @Override
    public void onTriggerResultsClear() {
        this.mapFeedFragmentHandler.getMapFeedSearchAutocompleteFragment().clearAutocomplete();
    }

    @Override
    public int checkSearchFieldLength() {
        return this.mapFeedFragmentHandler.getMapFeedSearchFragment().searchFieldLength();
    }

    @Override
    public void onSettingsUpdated(Settings settings) {
        try{
            this.mapFeedFragmentHandler.requestMap(settings);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            currentLocation.startLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        currentLocation.stopLocationUpdates();
        this.mapFeedFragmentHandler.setListener(null);
        this.mapFeedFragmentHandler.setCurrentLocationListenerListener(null);
    }

}