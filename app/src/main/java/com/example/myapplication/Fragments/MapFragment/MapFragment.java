package com.example.myapplication.Fragments.MapFragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myapplication.Adapters.LockableViewPager.LockableViewPager;
import com.example.myapplication.Fragments.MainMapFragment.MainMapFragment;
import com.example.myapplication.Fragments.MapFeedSearchAutocompleteFragment.MapFeedSearchAutocompleteFragment;
import com.example.myapplication.Fragments.MapFeedSearchFragment.MapFeedSearchFragment;
import com.example.myapplication.Fragments.MapFilterFragment.MapFilterFragment;
import com.example.myapplication.Handlers.MapFragmentHandler.MapFragmentHandler;
import com.example.myapplication.Interfaces.CurrentLocationListener.CurrentLocationListener;
import com.example.myapplication.Interfaces.CustomMarkerListener.CustomMarkerListener;
import com.example.myapplication.Interfaces.MapListener.MapListener;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Models.LoadingSpinner.LoadingSpinner;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.Settings.Settings;
import com.example.myapplication.R;
import com.example.myapplication.Refactor.searchAutocomplete.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class MapFragment extends Fragment implements MapFeedSearchFragment.FragmentSearchListener, MapFeedSearchAutocompleteFragment.FragmentAutocompleteListener
                                                     , MapFilterFragment.FragmentMapFilterListener, CurrentLocationListener, MapListener, CustomMarkerListener {
    SupportMapFragment supportMapFragment;
    LoadingSpinner loadingSpinner;
    MapFragmentHandler mapFragmentHandler;
    LockableViewPager viewPager;
    CurrentLocation currentLocation;

    int receiverMarkerId;
    double receiverLat;
    double receiverLng;
    boolean received;

    public MapFragment(LockableViewPager viewPager) {
        this.viewPager = viewPager;
        this.received = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mapFragmentHandler = new MapFragmentHandler(this, viewPager, this, this);
        this.supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        this.loadingSpinner = new LoadingSpinner((ProgressBar) Objects.requireNonNull(getView()).findViewById(R.id.feedLoadingSpinner));
        this.mapFragmentHandler.configureElements();

        currentLocation = new CurrentLocation(getActivity(), this);
        this.mapFragmentHandler.requestMap(null);
    }

    @Override
    public void addMarkerData(ArrayList<Marker> markers) {
        this.mapFragmentHandler.addMarkerData(markers);
        if(mapFragmentHandler != null){
            if(markers != null && received){
                mapFragmentHandler.triggerMarkerWithinRadiusMarker(markers, receiverMarkerId, viewPager, new LatLng(receiverLat, receiverLng));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mapFragmentHandler.showSpinner();

        LatLng latLng = this.mapFragmentHandler.handleResult(requestCode, resultCode, data);
        this.mapFragmentHandler.handleSavedLocation(latLng);
    }

    public void showFeedMapContainer(){
        this.mapFragmentHandler.showFeedMapContainer();
    }

    @Override
    public void onInputAutocompleteSent(CharSequence input) {
        this.mapFragmentHandler.getMapFeedSearchFragment().updateEditText(input);
    }

    @Override
    public void onInputSearchSent(CharSequence input) {
        this.mapFragmentHandler.getMapFeedSearchAutocompleteFragment().updateEditText(input);
    }

    @Override
    public void onSearchTextChanged(Place place, String mainText, String secondText) {
        this.mapFragmentHandler.getMapFeedSearchAutocompleteFragment().buildAutocompleteSearchItem(place, mainText, secondText);
    }

    @Override
    public void onTriggerResultsClear() {
        this.mapFragmentHandler.getMapFeedSearchAutocompleteFragment().clearAutocomplete();
    }

    @Override
    public int checkSearchFieldLength() {
        return this.mapFragmentHandler.getMapFeedSearchFragment().searchFieldLength();
    }

    @Override
    public void onSettingsUpdated(Settings settings) {
        try{
            this.mapFragmentHandler.requestMap(settings);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateUserLocation(Location location) {
        this.mapFragmentHandler.updateUserLocation(location);
        this.showFeedMapContainer();
    }

    /*
    * Map Listener
    * */
    @Override
    public void handleMapClick(GoogleMap googleMap) {
    }

    /*
     * Map Listener
     * */
    @Override
    public void handleMarkerClick(GoogleMap googleMap, FragmentActivity fragmentActivity) {
        this.mapFragmentHandler.addMarkersListener(googleMap, fragmentActivity);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            currentLocation.startLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        currentLocation.stopLocationUpdates();

        try{
            Objects.requireNonNull(getActivity()).unregisterReceiver(receiver);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, new IntentFilter(MapFragment.class.toString()));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            received = true;
            receiverMarkerId = Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("markerId")));
            receiverLat = Double.parseDouble(Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("lat")));
            receiverLng = Double.parseDouble(Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("lng")));

            Log.d("Print", "onreceive = " + receiverMarkerId);
        }

    };

}