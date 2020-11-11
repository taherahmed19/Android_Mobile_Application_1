package com.example.myapplication.Fragments.MapFilterFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Fragments.MapFeedSearchFragment.MapFeedSearchFragment;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Handlers.MapFeedFragmentHandler.MapFeedFragmentHandler;
import com.example.myapplication.Handlers.MapFeedSearchFragmentHandler.MapFeedSearchFragmentHandler;
import com.example.myapplication.Handlers.MapFilterFragmentHandler.MapFilterFragmentHandler;
import com.example.myapplication.Models.Settings.Settings;
import com.example.myapplication.Models.SpinnerItem.SpinnerItem;
import com.example.myapplication.R;
import com.example.myapplication.Models.FilteredRegion.FilteredRegion;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class MapFilterFragment extends Fragment{
    public static final String TAG = MapFilterFragment.class.getName();

    FragmentManager fragmentSupport;
    ViewPager viewPager;
    MapFragment mapFragment;

    MapFilterFragmentHandler mapFilterFragmentHandler;

    FragmentMapFilterListener listener;

    public interface FragmentMapFilterListener {
        void onSettingsUpdated(Settings settings);
    }

    public MapFilterFragment(FragmentManager fragmentSupport, ViewPager viewPager, MapFragment mapFragment){
        this.fragmentSupport = fragmentSupport;
        this.viewPager = viewPager;
        this.mapFilterFragmentHandler = new MapFilterFragmentHandler(this, viewPager);
        this.mapFragment = mapFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                double latitude = data.getDoubleExtra("latitude", 0 );
                double longitude = data.getDoubleExtra("longitude", 0);
                String name = data.getStringExtra("name");

                FilteredRegion filteredRegion = new FilteredRegion(new LatLng(latitude,longitude), name);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_filter, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mapFilterFragmentHandler.configure();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(mapFragment != null && mapFragment instanceof MapFeedSearchFragment.FragmentSearchListener){
            this.mapFilterFragmentHandler.setListener((FragmentMapFilterListener) mapFragment);
        }else{
            throw new RuntimeException(mapFragment.toString() + " must implemented FragmentSearchListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mapFilterFragmentHandler.setListener(null);
    }
}