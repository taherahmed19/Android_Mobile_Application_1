package com.example.myapplication.Handlers.CustomMarkerBottomSheetHandler;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.CustomMarkerBottomSheetFragment.CustomMarkerBottomSheetFragment;
import com.example.myapplication.Handlers.RadiusMarkerHandler.RadiusMarkerHandler;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.Utils.Tools.Tools;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

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

    boolean inAppButtonClicked;
    boolean voiceButtonClicked;

    public CustomMarkerBottomSheetHandler(CustomMarkerBottomSheetFragment customMarkerBottomSheetFragment, GoogleMap mMap, LatLng latLng, RadiusMarkerHandler radiusMarkerHandler) {
        this.customMarkerBottomSheetFragment = customMarkerBottomSheetFragment;
        this.mMap = mMap;
        this.latLng = latLng;
        this.radiusMarkerHandler = radiusMarkerHandler;
        inAppButtonClicked = true;
        voiceButtonClicked = false;
    }

    public void configure(){
        configureSeekBar();
        configureCloseButton();
        configureInAppButton();
        configureVoiceButton();
        configureSaveButton();
        configureRemoveButton();
        resetNotificationsState();
    }

    public void handleRadiusMarkerClick(CustomMarkerBottomSheetFragment customMarkerBottomSheetDialog, Context context, FragmentManager supportFragmentManager){
        SharedPreferences settingsPreference = Objects.requireNonNull(context).getSharedPreferences("Radius_Marker_Settings", 0);
        boolean stateExists = settingsPreference.getBoolean("stateExists", false);
        double radius = (double)settingsPreference.getFloat("radius", 0.0f);
        double centerLat = (double)settingsPreference.getFloat("centerLat", 0.0f);
        double centerLon = (double)settingsPreference.getFloat("centerLon", 0.0f);

        if(stateExists){
            float[] distance = new float[2];
            Location.distanceBetween(latLng.latitude, latLng.longitude, centerLat, centerLon, distance);

            if(distance[0] > radius){
            } else {
                if(customMarkerBottomSheetDialog == null){
                    customMarkerBottomSheetDialog = new CustomMarkerBottomSheetFragment(context, mMap, latLng, 0);
                }
                FragmentTransition.OpenFragment(supportFragmentManager, customMarkerBottomSheetDialog, R.id.mapFeedSearchPointer, "");
            }
        }
    }

    void configureSeekBar(){
        radiusMarkerSeekBar = (SeekBar) Objects.requireNonNull(this.customMarkerBottomSheetFragment.getView()).findViewById(R.id.radiusMarkerSeekBar);
        radiusMarkerSeekBarProgress = (TextView) this.customMarkerBottomSheetFragment.getView().findViewById(R.id.radiusMarkerSeekBarProgress);

        DisplayMetrics displayMetrics = Tools.ScreenDimensions(this.customMarkerBottomSheetFragment);
        int width = displayMetrics.widthPixels;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            radiusMarkerSeekBar.setMin(0);
            radiusMarkerSeekBar.setMax(width / 4);
            radiusMarkerSeekBar.setProgress((int)this.radiusMarkerHandler.getRadiusMarker().getRadius());

            double initialMiles = radiusMarkerHandler.calculateRadiusMarkerDistance(latLng, this.radiusMarkerHandler.getRadiusMarker().getRadius());
            String progressText = initialMiles + " mi";
            radiusMarkerSeekBarProgress.setText(progressText);
        }

        radiusMarkerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);

                double updatedMiles = radiusMarkerHandler.calculateRadiusMarkerDistance(latLng, progress);
                String progressText = updatedMiles + " mi";
                radiusMarkerSeekBarProgress.setText(progressText);
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
        radiusMarkerInAppButton = (Button) Objects.requireNonNull(this.customMarkerBottomSheetFragment.getView()).findViewById(R.id.radiusMarkerInAppButton);
        radiusMarkerInAppButton.setBackground(customMarkerBottomSheetFragment.getResources().getDrawable(R.drawable.custom_marker_bottom_background_clicked));

        radiusMarkerInAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleInAppButtonClicked();
            }
        });
    }

    void configureVoiceButton(){
        radiusMarkerVoiceButton = (Button) Objects.requireNonNull(this.customMarkerBottomSheetFragment.getView()).findViewById(R.id.radiusMarkerVoiceButton);

        radiusMarkerVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleVoiceButtonClicked();
            }
        });
    }

    void configureCloseButton(){
        radiusMarkerCloseButton = (ImageButton) Objects.requireNonNull(this.customMarkerBottomSheetFragment.getView()).findViewById(R.id.radiusMarkerCloseButton);

        radiusMarkerCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settingsPreference = Objects.requireNonNull(customMarkerBottomSheetFragment.getContext()).getSharedPreferences("Radius_Marker_Settings", 0);
                boolean stateExists = settingsPreference.getBoolean("stateExists", false);
                double radius = (double)settingsPreference.getFloat("radius", 0.0f);
                double centerLat = (double)settingsPreference.getFloat("centerLat", 0.0f);
                double centerLon = (double)settingsPreference.getFloat("centerLon", 0.0f);

                if (!stateExists){
                    radiusMarkerHandler.removeMarker();
                }else{
                    radiusMarkerHandler.getRadiusMarker().setRadius(radius);
                    radiusMarkerSeekBar.setProgress((int)radius);

                    double initialMiles = radiusMarkerHandler.calculateRadiusMarkerDistance(new LatLng(centerLat, centerLon), radius);
                    String progressText = initialMiles + " mi";
                    radiusMarkerSeekBarProgress.setText(progressText);
                }
                Objects.requireNonNull(customMarkerBottomSheetFragment.getActivity()).getSupportFragmentManager().popBackStack();
            }
        });
    }

    void configureRemoveButton(){
        Button radiusMarkerRemoveButton = (Button) this.customMarkerBottomSheetFragment.getView().findViewById(R.id.radiusMarkerRemoveButton);
        SharedPreferences settingsPreference = Objects.requireNonNull(customMarkerBottomSheetFragment.getContext()).getSharedPreferences("Radius_Marker_Settings", 0);
        boolean stateExists = settingsPreference.getBoolean("stateExists", false);

        if(stateExists){
            radiusMarkerRemoveButton.setVisibility(View.VISIBLE);
        }

        radiusMarkerRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureRemoveDialog();
            }
        });
    }

    void configureRemoveDialog(){
        Dialog dialog = new Dialog(Objects.requireNonNull(this.customMarkerBottomSheetFragment.getContext()));
        dialog.setContentView(R.layout.radius_marker_dialog);
        dialog.show();

        Window window = dialog.getWindow();
        Objects.requireNonNull(window).setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        Button radiusMarkerDialogCloseButton = (Button) dialog.findViewById(R.id.radiusMarkerDialogCloseButton);
        radiusMarkerDialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button radiusMarkerDialogRemoveButton = (Button) dialog.findViewById(R.id.radiusMarkerDialogRemoveButton);
        radiusMarkerDialogRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Objects.requireNonNull(customMarkerBottomSheetFragment.getContext()).getSharedPreferences("Radius_Marker_Settings", 0).edit().clear().apply();
                radiusMarkerHandler.getRadiusMarker().remove();
                Objects.requireNonNull(customMarkerBottomSheetFragment.getActivity()).getSupportFragmentManager().popBackStack();
            }
        });
    }

    void configureSaveButton(){
        radiusMarkerSaveButton = (Button) Objects.requireNonNull(this.customMarkerBottomSheetFragment.getView()).findViewById(R.id.radiusMarkerSaveButton);

        radiusMarkerSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSharedPreference();
                Objects.requireNonNull(customMarkerBottomSheetFragment.getActivity()).getSupportFragmentManager().popBackStack();
            }
        });
    }

    void saveSharedPreference(){
        SharedPreferences settingsPreference = Objects.requireNonNull(this.customMarkerBottomSheetFragment.getContext()).getSharedPreferences("Radius_Marker_Settings", 0);
        SharedPreferences.Editor preferenceEditor = settingsPreference.edit();
        preferenceEditor.putBoolean("stateExists", true);
        preferenceEditor.putBoolean("inAppNotifications", inAppButtonClicked);
        preferenceEditor.putBoolean("voiceNotifications", voiceButtonClicked);
        preferenceEditor.putFloat("radius", (float) radiusMarkerHandler.getRadiusMarker().getRadius());
        preferenceEditor.putFloat("centerLat", (float) latLng.latitude);
        preferenceEditor.putFloat("centerLon", (float) latLng.longitude);
        preferenceEditor.apply();
    }

    void resetNotificationsState(){
        SharedPreferences settingsPreference = Objects.requireNonNull(this.customMarkerBottomSheetFragment.getContext()).getSharedPreferences("Radius_Marker_Settings", 0);
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
