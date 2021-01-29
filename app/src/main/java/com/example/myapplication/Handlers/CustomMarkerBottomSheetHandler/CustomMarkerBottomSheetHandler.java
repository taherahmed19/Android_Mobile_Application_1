package com.example.myapplication.Handlers.CustomMarkerBottomSheetHandler;

import android.content.Context;
import android.os.Build;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class CustomMarkerBottomSheetHandler {

    CustomMarkerBottomSheetFragment customMarkerBottomSheetFragment;
    Context context;
    GoogleMap mMap;
    LatLng latLng;
    RadiusMarkerHandler radiusMarkerHandler;

    SeekBar radiusMarkerSeekBar;
    ImageButton radiusMarkerCloseButton;
    TextView radiusMarkerSeekBarProgress;
    LinearLayout radiusMarkerEmailButton;
    LinearLayout radiusMarkerInAppButton;
    LinearLayout radiusMarkerVoiceButton;


    public CustomMarkerBottomSheetHandler(CustomMarkerBottomSheetFragment customMarkerBottomSheetFragment, GoogleMap mMap, LatLng latLng, RadiusMarkerHandler radiusMarkerHandler) {
        this.customMarkerBottomSheetFragment = customMarkerBottomSheetFragment;
        this.mMap = mMap;
        this.latLng = latLng;
        this.radiusMarkerHandler = radiusMarkerHandler;
    }

    public void configure(){
        configureSeekBar();
        configureCloseButton();
        configureEmailButton();
        configureInAppButton();
        configureVoiceButton();
    }

    void configureSeekBar(){
        radiusMarkerSeekBar = (SeekBar) this.customMarkerBottomSheetFragment.getView().findViewById(R.id.radiusMarkerSeekBar);
        radiusMarkerSeekBarProgress = (TextView) this.customMarkerBottomSheetFragment.getView().findViewById(R.id.radiusMarkerSeekBarProgress);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            radiusMarkerSeekBar.setMin(0);
            radiusMarkerSeekBar.setMax(100);
            radiusMarkerSeekBar.setProgress(1);

            String initialProgress = "1";
            radiusMarkerSeekBarProgress.setText(initialProgress);
        }

        radiusMarkerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String updatedProgress = String.valueOf(progress);
                seekBar.setProgress(progress);

                radiusMarkerSeekBarProgress.setText(updatedProgress);
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

    boolean emailButtonClicked = false;
    void configureEmailButton(){
        radiusMarkerEmailButton = (LinearLayout) this.customMarkerBottomSheetFragment.getView().findViewById(R.id.radiusMarkerVoiceButton);

        radiusMarkerEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!emailButtonClicked){
                    radiusMarkerEmailButton.setBackground(customMarkerBottomSheetFragment.getResources().getDrawable(R.drawable.custom_marker_bottom_background_clicked));
                    emailButtonClicked = true;
                }else{
                    radiusMarkerEmailButton.setBackgroundResource(0);
                    emailButtonClicked = false;
                }
            }
        });
    }

    boolean inAppButtonClicked = false;
    void configureInAppButton(){
        radiusMarkerInAppButton = (LinearLayout) this.customMarkerBottomSheetFragment.getView().findViewById(R.id.radiusMarkerInAppButton);

        radiusMarkerInAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inAppButtonClicked){
                    radiusMarkerInAppButton.setBackground(customMarkerBottomSheetFragment.getResources().getDrawable(R.drawable.custom_marker_bottom_background_clicked));
                    inAppButtonClicked = true;
                }else{
                    radiusMarkerInAppButton.setBackgroundResource(0);
                    inAppButtonClicked = false;
                }
            }
        });
    }

    boolean voiceButtonClicked = false;
    void configureVoiceButton(){
        radiusMarkerVoiceButton = (LinearLayout) this.customMarkerBottomSheetFragment.getView().findViewById(R.id.radiusMarkerVoiceButton);

        radiusMarkerVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!voiceButtonClicked){
                    radiusMarkerVoiceButton.setBackground(customMarkerBottomSheetFragment.getResources().getDrawable(R.drawable.custom_marker_bottom_background_clicked));
                    voiceButtonClicked = true;
                    Log.d("Print", "Clicked");
                }else{
                    Log.d("Print", "Unclicked");
                    radiusMarkerVoiceButton.setBackgroundResource(0);
                    voiceButtonClicked = false;
                }
            }
        });
    }

    void configureCloseButton(){
        radiusMarkerCloseButton = (ImageButton) this.customMarkerBottomSheetFragment.getView().findViewById(R.id.radiusMarkerCloseButton);
        radiusMarkerCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radiusMarkerHandler.removeMarker();
                customMarkerBottomSheetFragment.getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

}
