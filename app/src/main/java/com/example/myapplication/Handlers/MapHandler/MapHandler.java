package com.example.myapplication.Handlers.MapHandler;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Models.Marker.Markers;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Tools.Tools;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.Filter.Filter;
import com.example.myapplication.Models.FilterSettings.FilterSettings;
import com.example.myapplication.Models.FilteredRegion.FilteredRegion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.sourceforge.jtds.jdbc.Support;

import java.util.ArrayList;

public class MapHandler implements OnMapReadyCallback {

    GoogleMap mMap;
    FragmentActivity fragmentActivity;
    FragmentManager fragmentManager;
    ArrayList<Marker> markers;
    ArrayList<com.google.android.gms.maps.model.Marker> googleMarkers;
    SupportMapFragment supportMapFragment;
    MapFragment mapFragment;

    public MapHandler(SupportMapFragment supportMapFragment, FragmentActivity fragmentActivity, FragmentManager fragmentManager, ArrayList<Marker> markers, MapFragment mapFragment){
        this.fragmentActivity =  fragmentActivity;
        this.fragmentManager = fragmentManager;
        this.markers = markers;
        this.mapFragment = mapFragment;
        this.googleMarkers = new ArrayList<>();

        if(mMap == null) {
            supportMapFragment.getMapAsync( this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();

        for(int i = 0; i < this.markers.size(); i++){
            addCustomMarker(i);
        }
        addCurrentLocationMarker();

        googleMap.setTrafficEnabled(true);

        LatLng savedLatLng = this.getMapCameraPosition();

        if(savedLatLng != null){
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(savedLatLng.latitude, savedLatLng.longitude), 15));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }else{
            if(this.markers.size() > 0){
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(this.markers.get(0).getLat()), Double.parseDouble(this.markers.get(0).getLng())), 15));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }
        }

        addMarkersListener();
        mapFragment.showFeedMapContainer();
    }

    public void setMapLocation(LatLng latLng){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    void addCustomMarker(int i){
        int markerColour = this.returnMarkerDrawable(this.markers.get(i).getMarker());
        final Drawable circleDrawable = fragmentActivity.getResources().getDrawable(markerColour);
        final BitmapDescriptor markerIcon = Tools.getMarkerIconFromDrawable(circleDrawable);

        com.google.android.gms.maps.model.Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(this.markers.get(i).getLat()), Double.parseDouble(this.markers.get(i).getLng())))
                .icon(markerIcon));

        marker.setTag(this.markers.get(i));

        googleMarkers.add(marker);
    }

    void addCurrentLocationMarker(){
        CurrentLocation currentLocation = new CurrentLocation(fragmentActivity);
        LatLng latLng = currentLocation.accessGeolocation();

        com.google.android.gms.maps.model.Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        marker.setTag(fragmentActivity.getString(R.string.default_constant));

        googleMarkers.add(marker);
    }

    void addMarkersListener(){
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

    int returnMarkerDrawable(int marker){
        int markerColour = 0;

        switch (marker){
            case 1:
                markerColour = R.drawable.ic_marker_green_maps;
                break;
            case 2:
                markerColour = R.drawable.ic_marker_blue_maps;
                break;
            case 3:
                markerColour = R.drawable.ic_marker_purple_maps;
                break;
            default:
                markerColour = R.drawable.ic_marker;
                break;
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
}
