package com.example.myapplication.Fragments.MainFragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Adapters.LockableViewPager.LockableViewPager;
import com.example.myapplication.Adapters.SwiperAdapter.SwiperAdapter;
import com.example.myapplication.R;

public class MainFragment extends Fragment {

    LockableViewPager viewPager;
    SwiperAdapter swiperAdapter;

    public MainFragment(){
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
        swiperAdapter = new SwiperAdapter(getChildFragmentManager(), viewPager);
        viewPager.setAdapter(swiperAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setSwipeable(false);
    }

    public Fragment getCurrentFragment(){
        return swiperAdapter.getItem(0);
    }

}