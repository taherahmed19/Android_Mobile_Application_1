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

import com.example.myapplication.Fragments.ErrorFragment.ErrorFragment;
import com.example.myapplication.Handlers.CustomMarkerBottomSheetHandler.CustomMarkerBottomSheetHandler;
import com.example.myapplication.Handlers.RadiusMarkerHandler.RadiusMarkerHandler;
import com.example.myapplication.Interfaces.DeleteRadiusMarkerListener.DeleteRadiusMarkerListener;
import com.example.myapplication.Interfaces.SetRadiusMarkerListener.SetRadiusMarkerListener;
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
    CustomMarkerBottomSheetHandler customMarkerBottomSheetHandler;

    public CustomMarkerBottomSheetFragment(Context context, GoogleMap mMap, LatLng latLng, double radius, RadiusMarkerHandler radiusMarkerHandler,
                                           DeleteRadiusMarkerListener deleteRadiusMarkerListener, SetRadiusMarkerListener setRadiusMarkerListener,
                                           FragmentManager fragmentManager) {
        this.context = context;
        this.mMap = mMap;
        this.latLng = latLng;
        this.customMarkerBottomSheetHandler = new CustomMarkerBottomSheetHandler(this, mMap, latLng, radiusMarkerHandler,
                context, deleteRadiusMarkerListener, setRadiusMarkerListener, fragmentManager);
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

    public void openFragment(CustomMarkerBottomSheetFragment customMarkerBottomSheetDialog, FragmentManager fragmentManager){
        if(!customMarkerBottomSheetDialog.isAdded()){
            FragmentTransition.OpenFragment(fragmentManager, customMarkerBottomSheetDialog, R.id.mapFeedSearchPointer, "");
        }
    }

    public void handleRadiusMarkerRemoval(boolean valid){
        this.customMarkerBottomSheetHandler.handleRadiusMarkerRemoval(valid);
    }

    public void handleRadiusMarker(boolean valid){
        this.customMarkerBottomSheetHandler.handleRadiusMarker(valid);
    }

    public void remove(){
        this.customMarkerBottomSheetHandler.remove();
    }
}