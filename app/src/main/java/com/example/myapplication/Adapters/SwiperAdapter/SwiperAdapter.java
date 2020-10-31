package com.example.myapplication.Adapters.SwiperAdapter;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.Adapters.LockableViewPager.LockableViewPager;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Fragments.FeedFragment.FeedFragment;

public class SwiperAdapter extends FragmentStatePagerAdapter{
    FragmentManager fragmentManager = null;
    MapFragment mapFragment;
    FeedFragment feedFragment;
    LockableViewPager viewPager;

    public SwiperAdapter(@NonNull FragmentManager fm, MapFragment mapFragment, FeedFragment feedFragment, LockableViewPager viewPager) {
        super(fm);
        fragmentManager = fm;
        this.mapFragment = mapFragment;
        this.feedFragment = feedFragment;
        this.viewPager = viewPager;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                mapFragment = new MapFragment(viewPager);
                fragment = mapFragment;
                break;
            case 1:
                feedFragment = new FeedFragment();
                fragment = feedFragment;
                break;
        }

//        Bundle bundle = new Bundle();
//        bundle.putInt("position", position + 1);
//        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
