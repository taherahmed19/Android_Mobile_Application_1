package com.example.myapplication.Handlers.CustomMarkerBottomSheetHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.myapplication.Fragments.CustomMarkerBottomSheetFragment.CustomMarkerBottomSheetFragment;
import com.example.myapplication.Handlers.RadiusMarkerHandler.RadiusMarkerHandler;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.text.DecimalFormat;

public class CustomMarkerBottomSheetHandler {

    CustomMarkerBottomSheetFragment customMarkerBottomSheetFragment;
    GoogleMap mMap;
    LatLng latLng;
    RadiusMarkerHandler radiusMarkerHandler;

    SeekBar radiusMarkerSeekBar;
    ImageButton radiusMarkerCloseButton;
    TextView radiusMarkerSeekBarProgress;
    Button radiusMarkerInAppButton;
    Button radiusMarkerVoiceButton;
    Button radiusMarkerSaveButton;

    boolean inAppButtonClicked = false;
    boolean voiceButtonClicked = false;

    public CustomMarkerBottomSheetHandler(CustomMarkerBottomSheetFragment customMarkerBottomSheetFragment, GoogleMap mMap, LatLng latLng, RadiusMarkerHandler radiusMarkerHandler) {
        this.customMarkerBottomSheetFragment = customMarkerBottomSheetFragment;
        this.mMap = mMap;
        this.latLng = latLng;
        this.radiusMarkerHandler = radiusMarkerHandler;
    }

    public void configure(){
        configureSeekBar();
        configureCloseButton();
        configureInAppButton();
        configureVoiceButton();
        configureSaveButton();
        resetNotificationsState();
    }

    public void resetState(){

    }

    void configureSeekBar(){
        radiusMarkerSeekBar = (SeekBar) this.customMarkerBottomSheetFragment.getView().findViewById(R.id.radiusMarkerSeekBar);
        radiusMarkerSeekBarProgress = (TextView) this.customMarkerBottomSheetFragment.getView().findViewById(R.id.radiusMarkerSeekBarProgress);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.customMarkerBottomSheetFragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            radiusMarkerSeekBar.setMin(0);
            radiusMarkerSeekBar.setMax(width / 4);
            radiusMarkerSeekBar.setProgress((int)this.radiusMarkerHandler.getRadiusMarker().getRadius());

            double initialMiles = radiusMarkerHandler.calculateRadiusMarkerDistance(latLng, this.radiusMarkerHandler.getRadiusMarker().getRadius());
            radiusMarkerSeekBarProgress.setText(initialMiles + " mi");
        }

        radiusMarkerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);

                double updatedMiles = radiusMarkerHandler.calculateRadiusMarkerDistance(latLng, progress);
                radiusMarkerSeekBarProgress.setText(updatedMiles + " mi");
                radiusMarkerHandler.updateMarkerRadius(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    void configureInAppButton(){
        radiusMarkerInAppButton = (Button) this.customMarkerBottomSheetFragment.getView().findViewById(R.id.radiusMarkerInAppButton);

        radiusMarkerInAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleInAppButtonClicked();
            }
        });
    }

    void configureVoiceButton(){
        radiusMarkerVoiceButton = (Button) this.customMarkerBottomSheetFragment.getView().findViewById(R.id.radiusMarkerVoiceButton);

        radiusMarkerVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleVoiceButtonClicked();
            }
        });
    }

    void configureCloseButton(){
        radiusMarkerCloseButton = (ImageButton) this.customMarkerBottomSheetFragment.getView().findViewById(R.id.radiusMarkerCloseButton);
        radiusMarkerCloseButton.setOnClickListener(new View.OnClickListener() {
            SharedPreferences settingsPreference = customMarkerBottomSheetFragment.getContext().getSharedPreferences("Radius_Marker_Settings", 0);
            boolean stateExists = settingsPreference.getBoolean("stateExists", false);

            @Override
            public void onClick(View view) {
                if (!stateExists){
                    radiusMarkerHandler.removeMarker();
                }
                customMarkerBottomSheetFragment.getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    void configureSaveButton(){
        radiusMarkerSaveButton = (Button) this.customMarkerBottomSheetFragment.getView().findViewById(R.id.radiusMarkerSaveButton);

        radiusMarkerSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSharedPreference();
                customMarkerBottomSheetFragment.getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    void saveSharedPreference(){
        SharedPreferences settingsPreference = this.customMarkerBottomSheetFragment.getContext().getSharedPreferences("Radius_Marker_Settings", 0);
        SharedPreferences.Editor preferenceEditor = settingsPreference.edit();
        preferenceEditor.putBoolean("stateExists", true);
        preferenceEditor.putBoolean("inAppNotifications", inAppButtonClicked);
        preferenceEditor.putBoolean("voiceNotifications", voiceButtonClicked);
        preferenceEditor.putFloat("radius", (float)radiusMarkerHandler.getRadiusMarker().getRadius());
        preferenceEditor.putFloat("centerLat", (float) latLng.latitude);
        preferenceEditor.putFloat("centerLon", (float) latLng.longitude);
        preferenceEditor.apply();
    }

    void resetNotificationsState(){
        SharedPreferences settingsPreference = this.customMarkerBottomSheetFragment.getContext().getSharedPreferences("Radius_Marker_Settings", 0);
        boolean inAppButtonState = settingsPreference.getBoolean("inAppNotifications", false);
        boolean voiceButtonState = settingsPreference.getBoolean("voiceNotifications", false);

        inAppButtonClicked = inAppButtonState;
        voiceButtonClicked = voiceButtonState;

        if(inAppButtonState){
            radiusMarkerInAppButton.setBackground(customMarkerBottomSheetFragment.getResources().getDrawable(R.drawable.custom_marker_bottom_background_clicked));
        }else if(voiceButtonClicked){
            radiusMarkerVoiceButton.setBackground(customMarkerBottomSheetFragment.getResources().getDrawable(R.drawable.custom_marker_bottom_background_clicked));
        }
    }

    void handleInAppButtonClicked(){
        if(!inAppButtonClicked){
            inAppButtonClicked = true;
            voiceButtonClicked = false;
            radiusMarkerInAppButton.setBackground(customMarkerBottomSheetFragment.getResources().getDrawable(R.drawable.custom_marker_bottom_background_clicked));
            radiusMarkerVoiceButton.setBackgroundResource(0);
        }else{
            inAppButtonClicked = false;
            radiusMarkerInAppButton.setBackgroundResource(0);
        }
    }

    void handleVoiceButtonClicked(){
        if(!voiceButtonClicked){
            voiceButtonClicked = true;
            inAppButtonClicked = false;
            radiusMarkerVoiceButton.setBackground(customMarkerBottomSheetFragment.getResources().getDrawable(R.drawable.custom_marker_bottom_background_clicked));
            radiusMarkerInAppButton.setBackgroundResource(0);
        }else{
            voiceButtonClicked = false;
            radiusMarkerVoiceButton.setBackgroundResource(0);
        }
    }

}
