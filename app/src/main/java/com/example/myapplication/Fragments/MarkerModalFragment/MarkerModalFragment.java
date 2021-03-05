package com.example.myapplication.Fragments.MarkerModalFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Fragments.ErrorFragment.ErrorFragment;
import com.example.myapplication.Handlers.MarkerModalFragmentHandler.MarkerModalFragmentHandler;
import com.example.myapplication.Handlers.RatingHandler.RatingHandler;
import com.example.myapplication.Interfaces.MarkerImageListener.MarkerImageListener;
import com.example.myapplication.Interfaces.MarkerListener.MarkerListener;
import com.example.myapplication.Interfaces.RatingsListener.RatingsListener;
import com.example.myapplication.R;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;
import java.util.Random;

public class MarkerModalFragment extends Fragment implements RatingsListener, MarkerListener, MarkerImageListener {

    MarkerModalFragmentHandler markerModalFragmentHandler;
    RatingHandler ratingHandler;
    Marker marker;
    ViewPager viewPager;

    public MarkerModalFragment(Marker marker, ViewPager viewPager) {
        this.marker = marker;
        this.markerModalFragmentHandler = new MarkerModalFragmentHandler(this);
        this.ratingHandler = new RatingHandler(this, marker);
        this.viewPager = viewPager;
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

    @Override
    public void updateModalRating(boolean response) {
        if(response){
            this.ratingHandler.setRating();
            this.ratingHandler.saveSettingsSharedPreference();
            this.markerModalFragmentHandler.updateRatingValue(this.ratingHandler.getRating());
        }else{
            Toast.makeText(getContext(), getContext().getString(R.string.post_rating_body), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void saveModalRatingState(int rating) {
        this.markerModalFragmentHandler.updateRatingValue(rating);
    }

    @Override
    public void deleteUserPost(boolean response) {
        if(response){
            Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
            getParentFragmentManager().popBackStack();
        }else{
            Toast.makeText(getContext(), getContext().getString(R.string.post_delete_body), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void handleMarkerImage(String encodedString) {
        this.markerModalFragmentHandler.handleMarkerImage(encodedString);
    }
}