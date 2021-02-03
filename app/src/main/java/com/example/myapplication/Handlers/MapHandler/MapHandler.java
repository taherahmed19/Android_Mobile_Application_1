package com.example.myapplication.Handlers.MapHandler;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.CustomMarkerBottomSheetFragment.CustomMarkerBottomSheetFragment;
import com.example.myapplication.Fragments.MapFilterFragment.MapFilterFragment;
import com.example.myapplication.Fragments.RadiusMarkerNotificationFragment.RadiusMarkerNotificationFragment;
import com.example.myapplication.Handlers.MapOnClickHandler;
import com.example.myapplication.Interfaces.MapListener.MapListener;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

public class MapHandler implements OnMapReadyCallback {

    GoogleMap mMap;
    FragmentActivity fragmentActivity;
    FragmentManager fragmentManager;
    ArrayList<com.google.android.gms.maps.model.Marker> googleMarkers;
    CurrentLocation currentLocation;
    com.google.android.gms.maps.model.Marker userLocationMarker;
    boolean cameraInitPos = false;
    LatLng location;
    MapListener mapListener;
    Context context;
    com.example.myapplication.Fragments.MapFragment.MapFragment mapFragment;

    public MapHandler(SupportMapFragment supportMapFragment, FragmentActivity fragmentActivity, FragmentManager fragmentManager, MapListener mapListener) {
        this.fragmentActivity = fragmentActivity;
        this.fragmentManager = fragmentManager;
        this.googleMarkers = new ArrayList<>();
        this.mapListener = mapListener;

        if (mMap == null) {
            supportMapFragment.getMapAsync(this);
        }
    }

    public MapHandler(SupportMapFragment supportMapFragment, FragmentActivity fragmentActivity, FragmentManager fragmentManager, MapListener mapListener, Context context, com.example.myapplication.Fragments.MapFragment.MapFragment mapFragment) {
        this.fragmentActivity = fragmentActivity;
        this.fragmentManager = fragmentManager;
        this.googleMarkers = new ArrayList<>();
        this.mapListener = mapListener;
        this.context = context;
        this.mapFragment = mapFragment;

        if (mMap == null) {
            supportMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setTrafficEnabled(false);

        LatLng savedLatLng = this.getMapCameraPosition();

        if (savedLatLng != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(savedLatLng.latitude, savedLatLng.longitude), 17));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
        }

        mapListener.handleMarkerClick(mMap, fragmentActivity);

        MapOnClickHandler onClickHandler = new MapOnClickHandler(context, mMap, fragmentManager);
        onClickHandler.configure();

        showDialog();
    }

    public void addDataSetMarkers(ArrayList<Marker> markers) {
        for (int i = 0; i < markers.size(); i++) {
            addCustomMarker(i, markers);
        }
    }

    public void startLocationUpdates() {
        currentLocation.startLocationUpdates();
    }

    public void stopLocationUpdates() {
        currentLocation.stopLocationUpdates();
    }

    public void setMapLocation(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 17));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
    }

    void addCustomMarker(int i, ArrayList<Marker> markers) {
        final BitmapDescriptor markerIcon = this.returnMarkerIcon(markers.get(i).getUserId());

        com.google.android.gms.maps.model.Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(markers.get(i).getLat()), Double.parseDouble(markers.get(i).getLng())))
                .icon(markerIcon));

        marker.setTag(markers.get(i));

        googleMarkers.add(marker);
    }

    /*
    * This method prints the current location using a custom google marker
    * */
    public void setUserLocationMarker(Location location) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        if (this.userLocationMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLocation);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);

            this.userLocationMarker = mMap.addMarker(markerOptions);
            userLocationMarker.setTag(fragmentActivity.getString(R.string.default_constant));
            googleMarkers.add(userLocationMarker);

            if (mMap != null) {
                if (ActivityCompat.checkSelfPermission(fragmentActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(fragmentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.latitude, currentLocation.longitude), 17));
            }
        }else{
            this.userLocationMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            userLocationMarker.setRotation(location.getBearing());
        }
    }

    /*
    * This method prints the current location using the Uber like blue dot marker
    * */
    public void setUserLocationGoogleMarker(Location location) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        this.location = currentLocation;

        if (!cameraInitPos) {
            if (mMap != null) {
                if (ActivityCompat.checkSelfPermission(fragmentActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(fragmentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.latitude, currentLocation.longitude), 17));
                cameraInitPos = true;
            }
        }

    }

    public void moveCameraToCurrentLocation(){
        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(fragmentActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(fragmentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.location, 17));
        }
    }

    BitmapDescriptor returnMarkerIcon(int userId){
        BitmapDescriptor markerColour = null;

        if(userId == LoginPreferenceData.getUserId(this.fragmentActivity)){
            markerColour = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
       }else{
            markerColour = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        }

        return markerColour;
    }

    public void saveMapCameraPosition(){
        if(mMap != null){
            CameraPosition mMyCam = mMap.getCameraPosition();
            String longitude = String.valueOf(mMyCam.target.longitude);
            String latitude = String.valueOf(mMyCam.target.latitude);

            SharedPreferences mapState = fragmentActivity.getSharedPreferences("Main_MapFeed_Map_State", 0);
            SharedPreferences.Editor mapStateEditor = mapState.edit();
            mapStateEditor.putString("longitude", longitude);
            mapStateEditor.putString("latitude", latitude);
            mapStateEditor.apply();
        }
    }

    LatLng getMapCameraPosition(){
        SharedPreferences mapState = fragmentActivity.getSharedPreferences("Main_MapFeed_Map_State", 0);
        String latitudeStr = mapState.getString("latitude", "");
        String longitudeStr = mapState.getString("longitude", "");

        if(latitudeStr != "" && longitudeStr != ""){
            double latitude = Double.parseDouble(latitudeStr);
            double longitude = Double.parseDouble(longitudeStr);

            return new LatLng(latitude,longitude);
        }

        return null;
    }

    public LatLng getLocation() {
        return location;
    }

    void showDialog(){
//        FragmentTransition.Transition(mapFragment.getParentFragmentManager(), new RadiusMarkerNotificationFragment(),
//                R.anim.radius_marker_notif_open_anim, R.anim.radius_marker_notif_close_anim, R.id.mapFeedSearchPointer, MapFilterFragment.TAG);
    }
}
