package com.example.myapplication.Fragments.RadiusMarkerNotificationFragment;

import android.annotation.SuppressLint;
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

import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;

public class RadiusMarkerNotificationFragment extends Fragment {

    FragmentManager fragmentManager;
    ViewPager viewPager;
    Marker marker;

    public RadiusMarkerNotificationFragment(FragmentManager fragmentManager,ViewPager viewPager, Marker marker) {
        this.fragmentManager = fragmentManager;
        this.viewPager = viewPager;
        this.marker = marker;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_radius_marker_notification, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        configureCloseButton();
        configureNotificationText();
        configureNotificationTriggerButton();
    }

    @SuppressLint("SetTextI18n")
    private void configureNotificationText(){
        TextView radiusMarkerCategoryText = this.getView().findViewById(R.id.radiusMarkerCategoryText);
        String category = marker.getCategory();
        radiusMarkerCategoryText.setText(Character.toUpperCase(category.charAt(0)) + category.substring(1));

        TextView radiusMarkerDescriptionText = (TextView) this.getView().findViewById(R.id.radiusMarkerDescriptionText);
        radiusMarkerDescriptionText.setText(marker.getDescription());
    }

    private void configureNotificationTriggerButton(){
        LinearLayout radiusMarkerNotifButton = (LinearLayout) this.getView().findViewById(R.id.radiusMarkerNotifButton);
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

    private void configureCloseButton(){
        LinearLayout radiusMarkerNotifCloseButton = (LinearLayout) this.getView().findViewById(R.id.radiusMarkerNotifCloseButton);
        radiusMarkerNotifCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
    }
}
