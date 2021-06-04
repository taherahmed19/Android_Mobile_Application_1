package com.example.myapplication.Adapters.SwiperAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.Adapters.LockableViewPager.LockableViewPager;
import com.example.myapplication.Fragments.MapFragment.MapFragment;

public class SwiperAdapter extends FragmentStatePagerAdapter{
    FragmentManager fragmentManager = null;
    MapFragment mapFragment;
    LockableViewPager viewPager;

    public SwiperAdapter(@NonNull FragmentManager fm, LockableViewPager viewPager) {
        super(fm);
        fragmentManager = fm;
        this.viewPager = viewPager;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        mapFragment = new MapFragment(viewPager);

        return mapFragment;
    }


    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
