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

    /**
     * Swiper adapter constructor
     * Work with custom view page
     * @param fm activity fragment manager
     * @param viewPager custom viewpage
     */
    public SwiperAdapter(@NonNull FragmentManager fm, LockableViewPager viewPager) {
        super(fm);
        fragmentManager = fm;
        this.viewPager = viewPager;
    }

    /**
     * Get screen item
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        mapFragment = new MapFragment(viewPager);
        return mapFragment;
    }

    /**
     * Count always to return 1 as main activity is the only screen
     * excludes login / register activities
     * @return
     */
    @Override
    public int getCount() {
        return 1;
    }

    /**
     * Enable view pager refreshing
     * @param object item object - not required
     * @return
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
