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
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.myapplication.Adapters.LockableViewPager.LockableViewPager;
import com.example.myapplication.Application.App;
import com.example.myapplication.Fragments.FormFragment.FormFragment;
import com.example.myapplication.Fragments.MainMapFragment.MainMapFragment;
import com.example.myapplication.Fragments.MapFeedSearchAutocompleteFragment.MapFeedSearchAutocompleteFragment;
import com.example.myapplication.Fragments.MapFeedSearchFragment.MapFeedSearchFragment;
import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.Fragments.RadiusMarkerNotificationFragment.RadiusMarkerNotificationFragment;
import com.example.myapplication.Handlers.BackgroundNotificationHandler.BackgroundNotificationHandler;
import com.example.myapplication.Handlers.InteractiveMap.InteractiveMap;
import com.example.myapplication.Handlers.MapFeedSearchFragmentHandler.MapFeedSearchFragmentHandler;
import com.example.myapplication.Handlers.MapFragmentHandler.MapFragmentHandler;
import com.example.myapplication.HttpRequest.HttpMap.HttpMap;
import com.example.myapplication.Interfaces.CurrentLocationListener.CurrentLocationListener;
import com.example.myapplication.Interfaces.CustomMarkerListener.CustomMarkerListener;
import com.example.myapplication.Interfaces.MapContract.MapContract;
import com.example.myapplication.Interfaces.MapListener.MapListener;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Models.LoadingSpinner.LoadingSpinner;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.Settings.Settings;
import com.example.myapplication.Presenters.MapPresenter.MapPresenter;
import com.example.myapplication.R;
import com.example.myapplication.Refactor.searchAutocomplete.Place;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.Utils.StringConstants.StringConstants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class MapFragment extends Fragment implements MapFeedSearchFragment.FragmentSearchListener,
        MapFeedSearchAutocompleteFragment.FragmentAutocompleteListener,
        CurrentLocationListener, MapListener, CustomMarkerListener,
        MapContract.View {

    SupportMapFragment supportMapFragment;
    LoadingSpinner loadingSpinner;
    MapFragmentHandler mapFragmentHandler;
    LockableViewPager viewPager;
    CurrentLocation currentLocation;

    MapPresenter mapPresenter;
    MapFeedSearchAutocompleteFragment mapFeedSearchAutocompleteFragment;
    MapFeedSearchFragment mapFeedSearchFragment;

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

        currentLocation = new CurrentLocation(getActivity(), this);
        mapPresenter.requestMap(getChildFragmentManager(), supportMapFragment);

        this.mapPresenter = new MapPresenter(this);

        this.mapFeedSearchAutocompleteFragment = new MapFeedSearchAutocompleteFragment(this);
        this.mapFeedSearchFragment = new MapFeedSearchFragment(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.showSpinner();

        LatLng latLng = this.handleResult(requestCode, resultCode, data);
        this.handleSavedLocation(latLng);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        currentLocation.startLocationUpdates();
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
    public void handleMapClick(GoogleMap googleMap) {
    }

    @Override
    public void handleMarkerClick(GoogleMap googleMap, FragmentActivity fragmentActivity) {
        this.mapFragmentHandler.addMarkersListener(googleMap, fragmentActivity);
    }

    @Override
    public void onStart() {
        super.onStart();
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

    @Override
    public void handleSearchButtonClick() {
        if (getFragmentManager() != null) {
            FragmentTransition.TransitionActivityResult(getFragmentManager(), mapFeedSearchFragment,
                    this, R.anim.top_animation, R.anim.down_animation, R.id.mapFeedSearchPointer, MapFeedSearchFragmentHandler.REQUEST_CODE_SEARCH, MapFeedSearchFragment.TAG);

            FragmentTransition.Transition(getFragmentManager(), mapFeedSearchAutocompleteFragment,
                    R.anim.right_animations, R.anim.left_animation, R.id.mapFeedSearchAutoPointer, MapFeedSearchAutocompleteFragment.TAG);
        }
    }

    @Override
    public void handleNewPostButtonClick(){
        FormFragment fragment = new FormFragment(getFragmentManager(), viewPager);
        FragmentTransaction transition = FragmentTransition.Transition(getFragmentManager(), fragment, R.anim.right_animations, R.anim.left_animation, R.id.mapFeedSearchPointer, "");
    }

    @Override
    public boolean handleMarkerClick(com.google.android.gms.maps.model.Marker marker){
        if(!marker.getTag().equals(getString(R.string.default_constant))){
            Marker item = (Marker)marker.getTag();

            MarkerModalFragment markerModalFragment = new MarkerModalFragment(item, viewPager);
            FragmentTransition.Transition(this.getParentFragmentManager(), markerModalFragment, R.anim.right_animations, R.anim.left_animation,
                    R.id.mapModalContainer, "");

            return true;
        }
        return false;
    }

    @Override
    public void hideSpinner(){
        if(this.loadingSpinner != null){
            this.loadingSpinner.hide();
        }
    }

    void configureSupportMapFragment(){
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
    }

    void configureSpinner(){
        this.loadingSpinner = new LoadingSpinner((ProgressBar) getView().findViewById(R.id.feedLoadingSpinner));
    }

    void configureMapSearchButton(){
        ImageButton mapSearchButton = (ImageButton) getView().findViewById(R.id.mapSearchButton);

        mapSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapPresenter.handleSearchButtonClick();
            }
        });
    }

    void configureNewPostButton(){
        final ImageButton newPost = (ImageButton) getView().findViewById(R.id.newPost);
        newPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapPresenter.handleNewPostButtonClick();
            }
        });
    }

    void configureSwitchButton(){
        final ImageButton mapLocationResetBtn = (ImageButton) getView().findViewById(R.id.mapLocationResetBtn);

        mapLocationResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapPresenter.setCameraCurrentLocation();
            }
        });
    }

    void configureMapRefreshButton(){
        final ImageButton mapRefreshBtn = (ImageButton) getView().findViewById(R.id.mapRefreshBtn);

        mapRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    public void addMarkerData(ArrayList<Marker> markers) {
        mapPresenter.addMarkerList(markers);
    }

    public void updateUserLocation(Location location) {
        mapPresenter.setGoogleMakerLocation(location);
        //showFeedMapContainer(); remove?
    }

    public void setMapLocation(LatLng latLng){
        mapPresenter.setMapLocation(latLng);
    }

    public void addMarkersListener(GoogleMap mMap, FragmentActivity fragmentActivity){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                return mapPresenter.handleMarkerClick(marker);
            }
        });
    }

    public void triggerMarkerOnMap(ViewPager viewPager, Marker markerModel){
        mapPresenter.addMarkerToMap(viewPager, markerModel);
    }

    public void handleSavedLocation(LatLng latLng){
        mapPresenter.handleMapSavedLocation(latLng);
    }

    public void showSpinner(){
        if(this.loadingSpinner != null){
            this.loadingSpinner.show();
        }
    }

    //remove?
    public void showFeedMapContainer(){
//        LinearLayout feedMapContainer = (LinearLayout) mapFragment.getView().findViewById(R.id.feedMapContainer);
//        feedMapContainer.setVisibility(View.VISIBLE);
    }

    public LatLng handleResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            if (requestCode == MapFeedSearchFragmentHandler.REQUEST_CODE_SEARCH){
                return (LatLng)data.getParcelableExtra(StringConstants.INTENT_MAP_FEED_SEARCH_LAT_LNG);
            }
        }

        return null;
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean voiceEnabled = Objects.requireNonNull(intent.getExtras()).getBoolean("voiceEnabled");
            int openNotification = Objects.requireNonNull(intent.getExtras()).getInt("openNotification");
            int userId = Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("userId")));
            int markerId = Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("markerId")));
            String category = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("category"));
            String description = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("description"));
            String lat = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("lat"));
            String lng = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("lng"));
            String firstName = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("firstName"));
            String lastName = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("lastName"));
            int rating = Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).getString("rating")));

            Marker markerModel = new Marker(userId, firstName, lastName, category, description, lat, lng, null, markerId, rating);

            triggerMarkerOnMap(viewPager, markerModel);

            if(!voiceEnabled && openNotification == 0){
                MarkerModalFragment markerModalFragment = new MarkerModalFragment(markerModel, viewPager);
                FragmentTransition.Transition(getParentFragmentManager(), markerModalFragment, R.anim.right_animations, R.anim.left_animation,
                        R.id.mapModalContainer, "");
                mapFragmentHandler.setMapLocation(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
            }

            if(!voiceEnabled && openNotification == 1){
                RadiusMarkerNotificationFragment notificationFragment = new RadiusMarkerNotificationFragment(getParentFragmentManager(), viewPager, markerModel);
                FragmentTransition.Transition(getParentFragmentManager(), notificationFragment,
                        R.anim.radius_marker_notif_open_anim, R.anim.radius_marker_notif_close_anim, R.id.mapFeedSearchPointer, RadiusMarkerNotificationFragment.class.toString());
            }

            if(voiceEnabled){
                mapFragmentHandler.setMapLocation(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
                new BackgroundNotificationHandler(getContext(), category, description, lat, lng);
            }
        }
    };


}