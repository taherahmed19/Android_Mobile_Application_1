package com.example.myapplication.Handlers.MapOnClickHandler;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentManager;

//import com.example.myapplication.Fragments.CustomMarkerBottomSheetFragment.CustomMarkerBottomSheetFragment;
import com.example.myapplication.Models.RadiusMarker.RadiusMarker;
import com.example.myapplication.Interfaces.DeleteRadiusMarkerListener.DeleteRadiusMarkerListener;
import com.example.myapplication.Interfaces.SetRadiusMarkerListener.SetRadiusMarkerListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class MapOnClickHandler implements DeleteRadiusMarkerListener, SetRadiusMarkerListener {

    FragmentManager fragmentManager;
    private Context context;
    private GoogleMap mMap;
    //CustomMarkerBottomSheetFragment customMarkerBottomSheetDialog;
    RadiusMarker radiusMarker;

    public MapOnClickHandler(Context context, GoogleMap mMap, FragmentManager fragmentManager) {
        this.context = context;
        this.mMap = mMap;
        this.fragmentManager = fragmentManager;
    }

    void resetRadiusMarkerState(){
        if(context != null){
            SharedPreferences settingsPreference = Objects.requireNonNull(context).getSharedPreferences("Radius_Marker_Settings", 0);
            boolean stateExists = settingsPreference.getBoolean("stateExists", false);
            double radius = (double)settingsPreference.getFloat("radius", 0.0f);
            double centerLat = (double)settingsPreference.getFloat("centerLat", 0.0f);
            double centerLon = (double)settingsPreference.getFloat("centerLon", 0.0f);

            if(stateExists){
                radiusMarker = new RadiusMarker(mMap, new LatLng(centerLat, centerLon), radius);
//                customMarkerBottomSheetDialog = new CustomMarkerBottomSheetFragment(context, mMap, new LatLng(centerLat, centerLon), radius, radiusMarkerHandler,
//                        this, this, fragmentManager);
            }
        }
    }

//    void configureRadiusMarker(){
//        MapOnClickHandler instance = this;
//
//        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(LatLng latLng) {
//                radiusMarkerHandler = new RadiusMarkerHandler(mMap, latLng, 0);
//
////                if(customMarkerBottomSheetDialog != null){
////                    customMarkerBottomSheetDialog.remove();
////                    customMarkerBottomSheetDialog = null;
////                }
////
////                customMarkerBottomSheetDialog = new CustomMarkerBottomSheetFragment(context, mMap, latLng, 0, radiusMarkerHandler,
////                        instance, instance, fragmentManager);
////                FragmentTransition.OpenFragment(fragmentManager, customMarkerBottomSheetDialog, R.id.mapFeedSearchPointer, "");
//            }
//        });
//
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
////                if(customMarkerBottomSheetDialog != null){
////                    boolean clickedInsideArea = radiusMarkerHandler.handleRadiusMarkerClick(customMarkerBottomSheetDialog, context, fragmentManager, latLng);
////
////                    if(clickedInsideArea){
////                        customMarkerBottomSheetDialog.openFragment(customMarkerBottomSheetDialog, fragmentManager);
////                    }
////                }
//            }
//        });
//    }

    @Override
    public void handleRadiusMarkerRemoval(boolean valid) {
    //    this.customMarkerBottomSheetDialog.handleRadiusMarkerRemoval(valid);
    }

    @Override
    public void handleRadiusMarker(boolean valid) {
      //  this.customMarkerBottomSheetDialog.handleRadiusMarker(valid);
    }
}
