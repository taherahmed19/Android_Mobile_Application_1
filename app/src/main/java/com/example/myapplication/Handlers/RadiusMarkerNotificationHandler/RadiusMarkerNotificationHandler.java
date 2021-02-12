package com.example.myapplication.Handlers.RadiusMarkerNotificationHandler;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.Fragments.RadiusMarkerNotificationFragment.RadiusMarkerNotificationFragment;
import com.example.myapplication.Handlers.MapHandler.MapHandler;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.google.android.gms.maps.model.LatLng;

public class RadiusMarkerNotificationHandler {

    RadiusMarkerNotificationFragment radiusMarkerNotificationFragment;
    FragmentManager fragmentManager;
    MapHandler mapHandler;
    Marker marker;
    ViewPager viewPager;

//    public RadiusMarkerNotificationHandler(RadiusMarkerNotificationFragment radiusMarkerNotificationFragment,
//                                           FragmentManager fragmentManager, MapHandler mapHandler,
//                                           Marker marker, ViewPager viewPager, LatLng latLng) {
//        this.radiusMarkerNotificationFragment = radiusMarkerNotificationFragment;
//        this.fragmentManager = fragmentManager;
//        this.mapHandler = mapHandler;
//        this.marker = marker;
//        this.viewPager = viewPager;
//        this.latLng = latLng;
//    }


    public RadiusMarkerNotificationHandler(RadiusMarkerNotificationFragment radiusMarkerNotificationFragment, FragmentManager fragmentManager,
                                           Marker marker, ViewPager viewPager) {
        this.radiusMarkerNotificationFragment = radiusMarkerNotificationFragment;
        this.fragmentManager = fragmentManager;
        this.marker = marker;
        this.viewPager = viewPager;
    }

    public void configure(){
        configureNotificationText();
        configureNotificationTriggerButton();
        configureCloseButton();
    }

    void configureNotificationText(){
        TextView radiusMarkerCategoryText = (TextView) this.radiusMarkerNotificationFragment.getView().findViewById(R.id.radiusMarkerCategoryText);
        radiusMarkerCategoryText.setText(marker.getCategory());

        TextView radiusMarkerDescriptionText = (TextView) this.radiusMarkerNotificationFragment.getView().findViewById(R.id.radiusMarkerDescriptionText);
        radiusMarkerDescriptionText.setText(marker.getDescription());
    }

    void configureNotificationTriggerButton(){
        LinearLayout radiusMarkerNotifButton = (LinearLayout) this.radiusMarkerNotificationFragment.getView().findViewById(R.id.radiusMarkerNotifButton);
        radiusMarkerNotifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();

                MarkerModalFragment markerModalFragment = new MarkerModalFragment(marker, viewPager);
                FragmentTransition.Transition(fragmentManager, markerModalFragment, R.anim.right_animations, R.anim.left_animation,
                        R.id.mapModalContainer, "");

            }
        });
    }

    void configureCloseButton(){
        LinearLayout radiusMarkerNotifCloseButton = (LinearLayout) this.radiusMarkerNotificationFragment.getView().findViewById(R.id.radiusMarkerNotifCloseButton);
        radiusMarkerNotifCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radiusMarkerNotificationFragment.getParentFragmentManager().popBackStack();
            }
        });
    }
}
