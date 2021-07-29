package com.example.myapplication.Fragments.MapFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Adapters.LockableViewPager.LockableViewPager;
import com.example.myapplication.Fragments.BottomSheetFragment.BottomSheetFragment;
import com.example.myapplication.Fragments.FormFragment.FormFragment;
import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.Fragments.RadiusMarkerNotificationFragment.RadiusMarkerNotificationFragment;
import com.example.myapplication.Interfaces.CurrentLocationListener.CurrentLocationListener;
import com.example.myapplication.Interfaces.MapContract.MapContract;
import com.example.myapplication.Models.BackgroundNotification.BackgroundNotification;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Models.LoadingSpinner.LoadingSpinner;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Presenters.MapPresenter.MapPresenter;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.JWTToken.JWTToken;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.Utils.Tools.Tools;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class MapFragment extends Fragment implements
        CurrentLocationListener, MapContract.View {

    SupportMapFragment supportMapFragment;
    LoadingSpinner loadingSpinner;
    LockableViewPager viewPager;
    CurrentLocation currentLocation;

    MapPresenter mapPresenter;
    BottomSheetFragment bottomSheetFragment;

    public MapFragment(LockableViewPager viewPager) {
        this.viewPager = viewPager;
    }

    /**
     * lifecycle for application
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    /**
     * components rendered to screen - requires presenter to be instantiated here.
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        this.loadingSpinner = new LoadingSpinner((ProgressBar) requireView().findViewById(R.id.feedLoadingSpinner));
        this.currentLocation = new CurrentLocation(getActivity(), this);
        this.mapPresenter = new MapPresenter(this);
        this.mapPresenter.makeApiRequestForMainMap(getChildFragmentManager(), supportMapFragment);

        this.mapPresenter.makeApiRequestGetRadiusMarker();

        configureMapRefreshButton();
        configureNewPostButton();
        configureSpinner();
        configureSupportMapFragment();
        configureSwitchButton();
    }

    /**
     * activity result when returning from fragment
     * @param requestCode custom request code
     * @param resultCode custom response code defined in fragment
     * @param data custom data to be parsed
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.showSpinner();
    }

    /**
     * lifecycle - location listener
     */
    @Override
    public void onStart() {
        super.onStart();
        currentLocation.startLocationUpdates();
    }

    /**
     * lifecyle - stop location listener
     */
    @Override
    public void onStop() {
        super.onStop();
        currentLocation.stopLocationUpdates();
    }

    /**
     * lifecycle - remove broadcast receiver
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        try{
            Objects.requireNonNull(getActivity()).unregisterReceiver(receiver);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * lifecycle - set broadcast receiver to listen from request from service.
     */
    @Override
    public void onResume() {
        super.onResume();

        //Hide keyboard when returning from Form fragment
        Tools.HideKeyboard(Objects.requireNonNull(getActivity()));

        try{
            getActivity().registerReceiver(receiver, new IntentFilter(MapFragment.class.toString()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Render form fragment into the UI - main activity will remain in background process to enable
     * easy refreshing of the map
     */
    @Override
    public void handleNewPostButtonClick(){
        FormFragment fragment = new FormFragment(getFragmentManager(), viewPager);
        FragmentTransition.Transition(getParentFragmentManager(), fragment, R.anim.right_animations, R.anim.left_animation, R.id.mapFeedSearchPointer, "");
    }

    /**
     * Detect user clicking on a marker on the map -
     * Create new modal fragment to display marker details
     * @param marker marker object - not conflict with default google marker
     * @return
     */
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

    /**
     * marker listener method
     * @param mMap map instance
     */
    @Override
    public void addMarkersListener(GoogleMap mMap){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                return mapPresenter.handleMarkerClick(marker);
            }
        });
    }

    /**
     * remove spinner from UI - called after successful response from web service
     */
    @Override
    public void hideSpinner(){
        if(this.loadingSpinner != null){
            this.loadingSpinner.hide();
        }
    }

    /**
     * notification area click listener
     * map long click to ensure user has to hold map for 1 second
     * map click equals default click
     * @param mMap
     */
    @Override
    public void handleRadiusMarkerMapClick(GoogleMap mMap){
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mapPresenter.createRadiusMarker(latLng);
                mapPresenter.createBottomSheetFragment(latLng);
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mapPresenter.openBottomSheetWithState(mMap, latLng);
            }
        });
    }

    /**
     * Create new notification area
     * Removes existing notification area if exists
     * @param mMap map instance
     * @param latLng center coordinate of notification area
     */
    @Override
    public void createBottomSheetFragment(GoogleMap mMap, LatLng latLng){
        if(bottomSheetFragment != null && bottomSheetFragment.isAdded()){
            bottomSheetFragment.getParentFragmentManager().popBackStack();
        }
        bottomSheetFragment = new BottomSheetFragment(mMap, latLng, this.mapPresenter.getRadiusMarker());
        FragmentTransition.OpenFragment(getParentFragmentManager(), bottomSheetFragment, R.id.mapFeedSearchPointer, "");
    }

    /**
     * If notification area is existing - open fragment with existing state
     * Without this method the fragment data will reset.
     * @param mMap map instance
     * @param latLng coords for notification area
     */
    @Override
    public void openBottomSheetWithState(GoogleMap mMap, LatLng latLng){
        boolean clickedInsideArea = mapPresenter.handleRadiusMarkerClick(latLng);

        if (clickedInsideArea) {
            bottomSheetFragment = new BottomSheetFragment(mMap, latLng, this.mapPresenter.getRadiusMarker());
            FragmentTransition.OpenFragment(getParentFragmentManager(), bottomSheetFragment, R.id.mapFeedSearchPointer, "");
        }else{
            Log.d("Print", "Clicked outside area ");
        }
    }

    /**
     * method to be called after response from web service
     * Toast to create UI dialog to user to display information
     * @param valid
     */
    @Override
    public void handleRadiusMarkerRemoval(Boolean valid){
        if(!valid){
            Toast.makeText(getContext(), getContext().getString(R.string.radius_marker_delete_body), Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), getContext().getString(R.string.radius_marker_delete_existing_marker_body), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * application context for fragment
     * @return
     */
    @Override
    public Context getApplicationContext(){
        return this.getContext();
    }

    /**
     * @return loading spinner instance
     */
    @Override
    public LoadingSpinner getLoadingSpinner(){
        return this.loadingSpinner;
    }

    /**
     * update user current location marker
     * @param location
     */
    @Override
    public void updateUserLocation(Location location) {
        mapPresenter.setGoogleMarkerLocation(location);
    }

    /**
     * To be called once token expired - remove token from local storage
     */
    @Override
    public void handleTokenExpiration(){
        JWTToken.removeTokenSharedPref(this.getActivity());
    }

    /**
     * set specific tiles to be rendered for user - updated once user moves map
     * @param latLng
     */
    public void setMapLocation(LatLng latLng){
        mapPresenter.setMapLocation(latLng);
    }

    /**
     * add marker
     * @param viewPager
     * @param markerModel
     */
    public void triggerMarkerOnMap(ViewPager viewPager, Marker markerModel){
        mapPresenter.addMarkerToMap(viewPager, markerModel);
    }

    /**
     * add spinner to UI
     */
    public void showSpinner(){
        if(this.loadingSpinner != null){
            this.loadingSpinner.show();
        }
    }

    /**
     * support map fragment required by map in XML file
     */
    void configureSupportMapFragment(){
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
    }

    /**
     * configure XML
     */
    void configureSpinner(){
        this.loadingSpinner = new LoadingSpinner((ProgressBar) getView().findViewById(R.id.feedLoadingSpinner));
    }

    /**
     * configure XML
     */
    void configureNewPostButton(){
        final ImageButton newPost = (ImageButton) getView().findViewById(R.id.newPostButton);
        newPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapPresenter.handleNewPostButtonClick();
            }
        });
    }

    /**
     * configure XML
     */
    void configureSwitchButton(){
        final ImageButton mapLocationResetBtn = (ImageButton) getView().findViewById(R.id.mapLocationResetBtn);

        mapLocationResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapPresenter.setCameraCurrentLocation();
            }
        });
    }

    /**
     * configure XML
     */
    void configureMapRefreshButton(){
        final ImageButton mapRefreshBtn = (ImageButton) getView().findViewById(R.id.mapRefreshBtn);

        mapRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    /**
     * @return broadcast receiver instance
     */
    public BroadcastReceiver getReceiver() {
        return receiver;
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Get data from FirebaseService.

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

            Marker markerModel = new Marker(userId, firstName, lastName, category, description, lat, lng, null, markerId);

            triggerMarkerOnMap(viewPager, markerModel);

            //conditions depending on user notification settings.

            if (!voiceEnabled && openNotification == 0) {
                MarkerModalFragment markerModalFragment = new MarkerModalFragment(markerModel, viewPager);
                FragmentTransition.Transition(getParentFragmentManager(), markerModalFragment, R.anim.right_animations, R.anim.left_animation,
                        R.id.mapModalContainer, "");
                setMapLocation(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
            }

            if (!voiceEnabled && openNotification == 1) {
                RadiusMarkerNotificationFragment notificationFragment = new RadiusMarkerNotificationFragment(getParentFragmentManager(), viewPager, markerModel);
                FragmentTransition.Transition(getParentFragmentManager(), notificationFragment,
                        R.anim.radius_marker_notif_open_anim, R.anim.radius_marker_notif_close_anim, R.id.mapFeedSearchPointer, RadiusMarkerNotificationFragment.class.toString());
            }

            if (voiceEnabled) {
                setMapLocation(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
                new BackgroundNotification(getContext(), category, description, lat, lng);
            }
        }
    };
}