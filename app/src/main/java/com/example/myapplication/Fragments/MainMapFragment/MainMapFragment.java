package com.example.myapplication.Fragments.MainMapFragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Adapters.LockableViewPager.LockableViewPager;
import com.example.myapplication.Adapters.SwiperAdapter.SwiperAdapter;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Fragments.FeedFragment.FeedFragment;
import com.example.myapplication.Fragments.UserFeedFormFragment.UserFeedFormFragment;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.Fragments.MapFilterFragment.MapFilterFragment;

public class MainMapFragment extends Fragment {
    LockableViewPager viewPager;
    MapFragment mapFragment;
    FeedFragment feedFragment;
    SwiperAdapter swiperAdapter;
    MapFilterFragment fragment;

    public MainMapFragment(){
        mapFragment = new MapFragment(viewPager);
        feedFragment = new FeedFragment();
        viewPager = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewPager = (LockableViewPager)getView().findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(0);
        swiperAdapter = new SwiperAdapter(getChildFragmentManager(), mapFragment, feedFragment, viewPager);
        viewPager.setAdapter(swiperAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setSwipeable(false);

    }

}