package com.example.myapplication.Fragments.MarkerModalFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.Handlers.MarkerModalFragmentHandler.MarkerModalFragmentHandler;
import com.example.myapplication.Handlers.RatingHandler.RatingHandler;
import com.example.myapplication.R;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.google.android.material.navigation.NavigationView;

import java.util.Random;

public class MarkerModalFragment extends Fragment {

    MarkerModalFragmentHandler markerModalFragmentHandler;
    RatingHandler ratingHandler;
    Marker marker;

    public MarkerModalFragment(Marker marker) {
        this.marker = marker;
        this.markerModalFragmentHandler = new MarkerModalFragmentHandler(this);
        this.ratingHandler = new RatingHandler(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_marker_modal, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.markerModalFragmentHandler.configure();
        this.ratingHandler.configure();
    }

    public Marker getMarker() {
        return marker;
    }
}