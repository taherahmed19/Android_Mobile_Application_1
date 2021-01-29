package com.example.myapplication.Fragments.CustomMarkerBottomSheetFragment;

import android.content.Context;
import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Handlers.CustomMarkerBottomSheetHandler.CustomMarkerBottomSheetHandler;
import com.example.myapplication.Handlers.RadiusMarkerHandler.RadiusMarkerHandler;
import com.example.myapplication.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CustomMarkerBottomSheetFragment extends Fragment {

    Context context;
    GoogleMap mMap;
    LatLng latLng;
    RadiusMarkerHandler radiusMarkerHandler;

    CustomMarkerBottomSheetHandler customMarkerBottomSheetHandler;

    public CustomMarkerBottomSheetFragment(Context context, GoogleMap mMap, LatLng latLng) {
        this.context = context;
        this.mMap = mMap;
        this.latLng = latLng;
        this.radiusMarkerHandler = new RadiusMarkerHandler(mMap, latLng);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_marker_bottom, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        customMarkerBottomSheetHandler = new CustomMarkerBottomSheetHandler(this, mMap, latLng, radiusMarkerHandler);
        customMarkerBottomSheetHandler.configure();
    }



}