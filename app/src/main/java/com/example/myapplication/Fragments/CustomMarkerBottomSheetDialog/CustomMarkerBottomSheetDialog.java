package com.example.myapplication.Fragments.CustomMarkerBottomSheetDialog;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.example.myapplication.Handlers.RadiusMarkerHandler.RadiusMarkerHandler;
import com.example.myapplication.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CustomMarkerBottomSheetDialog  {

    Context context;
    BottomSheetDialog dialog;
    GoogleMap mMap;
    LatLng latLng;
    RadiusMarkerHandler radiusMarkerHandler;

    SeekBar seekBar;
    ImageButton radiusMarkerCloseButton;

    public CustomMarkerBottomSheetDialog(Context context, GoogleMap mMap, LatLng latLng) {
        this.context = context;
        this.mMap = mMap;
        this.latLng = latLng;
        this.radiusMarkerHandler = new RadiusMarkerHandler(mMap, latLng);
        this.create();
        this.configure();
        this.show();
    }

    public void create(){
        dialog = new BottomSheetDialog(context);
        dialog.setContentView(R.layout.custom_marker_bottom_sheet_dialog);
        dialog.getWindow().setDimAmount(0);
        dialog.setCanceledOnTouchOutside(false);
    }
    void show(){
        dialog.show();
    }

    void configure(){
        configureSeekBar();
        configureCloseButton();
    }

    void configureSeekBar(){
        seekBar = (SeekBar) dialog.findViewById(R.id.radiusMarkerSeekBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(0);
            seekBar.setMax(100);
            seekBar.setProgress(1);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
                radiusMarkerHandler.updateMarkerRadius(progress);
                Log.d("Print", "Progress " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void configureCloseButton(){
        radiusMarkerCloseButton = (ImageButton) dialog.findViewById(R.id.radiusMarkerCloseButton);
        radiusMarkerCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radiusMarkerHandler.removeMarker();
                dialog.dismiss();
            }
        });
    }

}