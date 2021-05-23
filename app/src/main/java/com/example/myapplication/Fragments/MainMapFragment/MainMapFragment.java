package com.example.myapplication.Fragments.MainMapFragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Adapters.LockableViewPager.LockableViewPager;
import com.example.myapplication.Adapters.SwiperAdapter.SwiperAdapter;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Fragments.FeedFragment.FeedFragment;
import com.example.myapplication.R;

import java.util.Objects;

public class MainMapFragment extends Fragment {
    LockableViewPager viewPager;
    MapFragment mapFragment;
    SwiperAdapter swiperAdapter;

    public MainMapFragment(){
        mapFragment = new MapFragment(viewPager);
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
        swiperAdapter = new SwiperAdapter(getChildFragmentManager(), mapFragment, viewPager);
        viewPager.setAdapter(swiperAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setSwipeable(false);
    }

}