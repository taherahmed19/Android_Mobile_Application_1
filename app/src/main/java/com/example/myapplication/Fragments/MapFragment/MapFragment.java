package com.example.myapplication.Fragments.MapFragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myapplication.Adapters.LockableViewPager.LockableViewPager;
import com.example.myapplication.Application.App;
import com.example.myapplication.Fragments.MainMapFragment.MainMapFragment;
import com.example.myapplication.Fragments.MapFeedSearchAutocompleteFragment.MapFeedSearchAutocompleteFragment;
import com.example.myapplication.Fragments.MapFeedSearchFragment.MapFeedSearchFragment;
import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.Fragments.RadiusMarkerNotificationFragment.RadiusMarkerNotificationFragment;
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
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.myapplication.Models.CurrentLocation.CurrentLocation.MY_PERMISSIONS_REQUEST_FINE_LOCATION;

public class MapFragment extends Fragment implements MapFeedSearchFragment.FragmentSearchListener, MapFeedSearchAutocompleteFragment.FragmentAutocompleteListener
        , CurrentLocationListener, MapListener, CustomMarkerListener {
    SupportMapFragment supportMapFragment;
    LoadingSpinner loadingSpinner;
    MapFragmentHandler mapFragmentHandler;
    LockableViewPager viewPager;
    CurrentLocation currentLocation;

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mapFragmentHandler.showSpinner();

        LatLng latLng = this.mapFragmentHandler.handleResult(requestCode, resultCode, data);
        this.mapFragmentHandler.handleSavedLocation(latLng);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
            }
        }

        currentLocation.startLocationUpdates();
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
//        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            currentLocation.startLocationUpdates();
//        }
        currentLocation.startLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        currentLocation.stopLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try{
            Objects.requireNonNull(getActivity()).unregisterReceiver(receiver);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try{
            Objects.requireNonNull(getActivity()).registerReceiver(receiver, new IntentFilter(MapFragment.class.toString()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
        boolean voiceEnabled = Objects.requireNonNull(intent.getExtras()).getBoolean("voiceEnabled");
     //   int openNotification = Objects.requireNonNull(intent.getExtras()).getInt("openNotification");
        int userId = Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("userId")));
        int markerId = Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("markerId")));
        String category = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("category"));
        String description = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("description"));
        String lat = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("lat"));
        String lng = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("lng"));
        String firstName = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("firstName"));
        String lastName = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("lastName"));
        int rating = Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("rating")));

        Marker markerModel = new Marker(userId, firstName, lastName,
                category, description, lat, lng, null, markerId, rating);

        mapFragmentHandler.triggerMarkerOnMap(viewPager, markerModel);

        //if voice enabled and app is visible
        if(voiceEnabled && App.APP_VISIBLE){
            //go to marker location
            //create marker modal etc...
            //TTS
        }else if(!voiceEnabled && App.APP_VISIBLE){
            //open radius marker notification fragment
        }else if (voiceEnabled && !App.APP_VISIBLE){
            // go to marker in background
            // TTS
        }else if (!voiceEnabled && !App.APP_VISIBLE){
            // send marker modal fragment
        }


        if(openNotification == 1){
            RadiusMarkerNotificationFragment notificationFragment = new RadiusMarkerNotificationFragment(getParentFragmentManager(), viewPager, markerModel);
            FragmentTransition.Transition(getParentFragmentManager(), notificationFragment,
                    R.anim.radius_marker_notif_open_anim, R.anim.radius_marker_notif_close_anim, R.id.mapFeedSearchPointer, RadiusMarkerNotificationFragment.class.toString());
        }else{
            MarkerModalFragment markerModalFragment = new MarkerModalFragment(markerModel, viewPager);
            FragmentTransition.Transition(getParentFragmentManager(), markerModalFragment, R.anim.right_animations, R.anim.left_animation,
                    R.id.mapModalContainer, "");
            mapFragmentHandler.setMapLocation(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
        }
        }

    };

}