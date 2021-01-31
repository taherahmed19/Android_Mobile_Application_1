package com.example.myapplication.Fragments.CustomMarkerBottomSheetFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Handlers.CustomMarkerBottomSheetHandler.CustomMarkerBottomSheetHandler;
import com.example.myapplication.Handlers.RadiusMarkerHandler.RadiusMarkerHandler;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

public class CustomMarkerBottomSheetFragment extends Fragment {

    Context context;
    GoogleMap mMap;
    LatLng latLng;
    RadiusMarkerHandler radiusMarkerHandler;
    CustomMarkerBottomSheetHandler customMarkerBottomSheetHandler;
    public static double INITIAL_RADIUS = 50;

    public CustomMarkerBottomSheetFragment(Context context, GoogleMap mMap, LatLng latLng, double radius) {
        this.context = context;
        this.mMap = mMap;
        this.latLng = latLng;
        this.createRadiusMarker(radius);
        this.customMarkerBottomSheetHandler = new CustomMarkerBottomSheetHandler(this, mMap, latLng, radiusMarkerHandler);
    }

    public void createRadiusMarker(double radius){
        if(radius == 0){
            radius = INITIAL_RADIUS;
        }
        this.radiusMarkerHandler = new RadiusMarkerHandler(mMap, latLng, radius);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_marker_bottom, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customMarkerBottomSheetHandler.configure();
    }

    public void onRadiusMarkerClick(CustomMarkerBottomSheetFragment customMarkerBottomSheetDialog, FragmentManager supportFragmentManager, LatLng latLng){
        this.customMarkerBottomSheetHandler.handleRadiusMarkerClick(customMarkerBottomSheetDialog, context, supportFragmentManager, latLng);
    }

    public void remove(){
        //Objects.requireNonNull(context).getSharedPreferences("Radius_Marker_Settings", 0).edit().clear().apply();
        this.radiusMarkerHandler.getRadiusMarker().remove();
    }


}