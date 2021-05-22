package com.example.myapplication.Fragments.RadiusMarkerNotificationFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Handlers.RadiusMarkerNotificationHandler.RadiusMarkerNotificationHandler;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.R;

public class RadiusMarkerNotificationFragment extends Fragment {

    RadiusMarkerNotificationHandler radiusMarkerNotificationHandler;
    FragmentManager fragmentManager;
    ViewPager viewPager;
    Marker marker;
    int markerId;

    public RadiusMarkerNotificationFragment(FragmentManager fragmentManager,ViewPager viewPager, Marker marker) {
        this.fragmentManager = fragmentManager;
        this.viewPager = viewPager;
        this.marker = marker;
        this.radiusMarkerNotificationHandler = new RadiusMarkerNotificationHandler(this, fragmentManager, marker, viewPager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_radius_marker_notification, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.radiusMarkerNotificationHandler.configure();
    }
}
