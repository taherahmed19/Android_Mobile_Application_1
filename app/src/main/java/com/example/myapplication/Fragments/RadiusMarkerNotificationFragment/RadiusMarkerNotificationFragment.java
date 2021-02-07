package com.example.myapplication.Fragments.RadiusMarkerNotificationFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Fragments.MapFilterFragment.MapFilterFragment;
import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.Handlers.MapFragmentHandler.MapFragmentHandler;
import com.example.myapplication.Handlers.MapHandler.MapHandler;
import com.example.myapplication.Handlers.RadiusMarkerNotificationHandler.RadiusMarkerNotificationHandler;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class RadiusMarkerNotificationFragment extends Fragment {

    RadiusMarkerNotificationHandler radiusMarkerNotificationHandler;
    FragmentManager fragmentManager;
    MapHandler mapHandler;
    Marker marker;
    ViewPager viewPager;
    LatLng latLng;

    public RadiusMarkerNotificationFragment(FragmentManager fragmentManager, MapHandler mapHandler, Marker marker, ViewPager viewPager, LatLng latLng) {
        this.fragmentManager = fragmentManager;
        this.mapHandler = mapHandler;
        this.marker = marker;
        this.viewPager = viewPager;
        this.latLng = latLng;
        this.radiusMarkerNotificationHandler = new RadiusMarkerNotificationHandler(this, fragmentManager, mapHandler, marker, viewPager, latLng);
    }

    MapFragmentHandler mapFragmentHandler;
    ArrayList<Marker> markers;
    int markerId;
    public RadiusMarkerNotificationFragment(ViewPager viewPager, MapFragmentHandler mapFragmentHandler, ArrayList<Marker> markers, int markerId) {
        this.viewPager = viewPager;
        this.mapFragmentHandler = mapFragmentHandler;
        this.markers = markers;
        this.markerId = markerId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_radius_marker_notification, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     //   this.radiusMarkerNotificationHandler.configure();

        LinearLayout radiusMarkerNotifButton = (LinearLayout) this.getView().findViewById(R.id.radiusMarkerNotifButton);
        radiusMarkerNotifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.getAdapter().notifyDataSetChanged();

                for(Marker marker : markers) {
                    if(markerId == marker.getId()){
                        getParentFragmentManager().popBackStack();

                        MarkerModalFragment markerModalFragment = new MarkerModalFragment(marker, viewPager);
                        FragmentTransition.Transition(fragmentManager, markerModalFragment, R.anim.right_animations, R.anim.left_animation,
                                R.id.mapModalContainer, "");
                        mapHandler.setMapLocation(latLng);
                    }
                }
            }
        });

    }
}
