package com.example.myapplication.Handlers.MapFeedFragmentHandler;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Adapters.LockableViewPager.LockableViewPager;
import com.example.myapplication.Fragments.MapFilterFragment.MapFilterFragment;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Fragments.MapFeedSearchFragment.MapFeedSearchFragment;
import com.example.myapplication.Fragments.UserFeedFormFragment.UserFeedFormFragment;
import com.example.myapplication.Handlers.MapFeedSearchFragmentHandler.MapFeedSearchFragmentHandler;
import com.example.myapplication.Fragments.MapFeedSearchAutocompleteFragment.MapFeedSearchAutocompleteFragment;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.Utils.StringConstants.StringConstants;
import com.google.android.gms.maps.model.LatLng;

import static android.app.Activity.RESULT_OK;

public class MapFeedFragmentHandler  {

    ImageButton mapSearchButton;
    MapFragment mapFragment;
    MapFeedSearchAutocompleteFragment mapFeedSearchAutocompleteFragment;
    MapFeedSearchFragment mapFeedSearchFragment;
    LockableViewPager viewPager;

    public MapFeedFragmentHandler(MapFragment mapFragment, LockableViewPager viewPager) {
        this.mapFragment = mapFragment;
        this.mapFeedSearchAutocompleteFragment = new MapFeedSearchAutocompleteFragment(mapFragment);
        this.mapFeedSearchFragment = new MapFeedSearchFragment(mapFragment);
        this.viewPager = viewPager;
    }

    public void configureElements(){
        this.configureMapSearchButton();
        this.configureNewPostButton();
        this.configureFilterButton();
        this.configureSwitchButton();
    }

    public void showFeedMapContainer(){
        LinearLayout feedMapContainer = (LinearLayout) mapFragment.getView().findViewById(R.id.feedMapContainer);
        feedMapContainer.setVisibility(View.VISIBLE);
    }

    void configureMapSearchButton(){
        this.mapSearchButton = (ImageButton) mapFragment.getView().findViewById(R.id.mapSearchButton);

        this.mapSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapFragment.getFragmentManager() != null) {
                    FragmentTransition.TransitionActivityResult(mapFragment.getFragmentManager(), mapFeedSearchFragment,
                            mapFragment, R.anim.top_animation, R.anim.down_animation, R.id.mapFeedSearchPointer, MapFeedSearchFragmentHandler.REQUEST_CODE_SEARCH, MapFeedSearchFragment.TAG);

                    FragmentTransition.Transition(mapFragment.getFragmentManager(), mapFeedSearchAutocompleteFragment,
                            R.anim.right_animations, R.anim.left_animation, R.id.mapFeedSearchAutoPointer, MapFeedSearchAutocompleteFragment.TAG);
                }
            }
        });
    }

    void configureNewPostButton(){
        final ImageButton newPost = (ImageButton) mapFragment.getView().findViewById(R.id.newPost);
        newPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserFeedFormFragment fragment = new UserFeedFormFragment(mapFragment.getFragmentManager(), viewPager);
                FragmentTransaction transition = FragmentTransition.Transition(mapFragment.getFragmentManager(), fragment, R.anim.right_animations, R.anim.left_animation, R.id.userFeedFormPointer, "");
            }
        });
    }

    void configureSwitchButton(){
        final ImageButton viewSwitch = (ImageButton) mapFragment.getView().findViewById(R.id.viewSwitch);

        viewSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentFragment = viewPager.getCurrentItem();

                switch (currentFragment){
                    case 0:
                        viewPager.setCurrentItem(1);
                        break;
                    case 1:
                        viewPager.setCurrentItem(0);
                        break;
                }
            }
        });
    }

    void configureFilterButton(){
        MapFilterFragment fragment = new MapFilterFragment(mapFragment.getFragmentManager(), viewPager,  mapFragment);

        ImageButton filterButton = (ImageButton) mapFragment.getView().findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transition = FragmentTransition.Transition(mapFragment.getFragmentManager(), fragment, R.anim.right_animations, R.anim.left_animation, R.id.userFeedFormPointer, "");
            }
        });
    }

    public LatLng handleResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            if (requestCode == MapFeedSearchFragmentHandler.REQUEST_CODE_SEARCH){
                return (LatLng)data.getParcelableExtra(StringConstants.INTENT_MAP_FEED_SEARCH_LAT_LNG);
            }
        }

        return null;
    }

    public Context getContext(){
        return this.mapFragment.getContext();
    }

    public MapFeedSearchAutocompleteFragment getMapFeedSearchAutocompleteFragment() {
        return mapFeedSearchAutocompleteFragment;
    }

    public MapFeedSearchFragment getMapFeedSearchFragment() {
        return mapFeedSearchFragment;
    }
}
