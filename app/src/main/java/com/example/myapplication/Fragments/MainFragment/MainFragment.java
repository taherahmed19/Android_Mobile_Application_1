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

    /**
     * lifecyle method - retain instance to save state
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_map, container, false);
    }

    /**
     * render components - element visible
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //viewpage enables refreshing of activities and fragments
        viewPager = (LockableViewPager)getView().findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(0);
        swiperAdapter = new SwiperAdapter(getChildFragmentManager(), viewPager);
        viewPager.setAdapter(swiperAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setSwipeable(false);
    }
}