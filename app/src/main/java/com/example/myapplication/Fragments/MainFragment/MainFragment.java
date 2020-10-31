package com.example.myapplication.Fragments.MainFragment;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Models.Marker.Markers;
import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainFragment extends Fragment implements OnMapReadyCallback
{
    CurrentLocation currentLocation;
    LatLng location;
    GoogleMap mMap;
    Markers markers;

    public MainFragment(){
        mMap = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMarkers();

        currentLocation = new CurrentLocation(getActivity());
        location = currentLocation.accessGeolocation();

        if(mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CurrentLocation.MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    currentLocation.accessGeolocation();
                } else {
                    Log.d("print", "Status: Declined");
                }
                return;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Marker marker =  mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.latitude, location.longitude))
                .title("")
                .snippet("Random snippet")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        marker.setTag(new LatLng(location.latitude, location.longitude));

        googleMap.setTrafficEnabled(true);
        setMarkerClickable();

        LatLng savedLatLng = this.getMapCameraPosition();

        if(savedLatLng != null){
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(savedLatLng.latitude, savedLatLng.longitude), 15));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }else{
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), 15));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        saveMapCameraPosition();
    }

    void setMarkerClickable(){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng position = (LatLng)(marker.getTag());
                Log.d("arg0", "Clicked marker at " + position.latitude + " " + position.longitude);

                return false;
            }
        });
    }

    void initMarkers(){
        this.markers = new Markers();
        this.markers.setupTestData();
    }

    void addMarkers(){
        for(int i = 0; i < this.markers.markers.size(); i++){
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(this.markers.getMarker(i).getLatitude(), this.markers.getMarker(i).getLongitude()))
                    .title("")
                    .snippet("Random snippet")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }

    void saveMapCameraPosition(){
        CameraPosition mMyCam = mMap.getCameraPosition();
        String longitude = String.valueOf(mMyCam.target.longitude);
        String latitude = String.valueOf(mMyCam.target.latitude);

        SharedPreferences mapState = getContext().getSharedPreferences("Main_Fragment_Map_State", 0);
        SharedPreferences.Editor mapStateEditor = mapState.edit();
        mapStateEditor.putString("longitude", longitude);
        mapStateEditor.putString("latitude", latitude);
        mapStateEditor.apply();

        Log.d("arg0", "Map camera position destroyed " + latitude + " " + longitude);
    }

    LatLng getMapCameraPosition(){
        SharedPreferences mapState = getContext().getSharedPreferences("Main_Fragment_Map_State", 0);
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