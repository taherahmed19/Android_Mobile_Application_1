package com.example.myapplication.Adapters.LockableViewPager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class LockableViewPager extends ViewPager {

    private boolean swipeable;

    /**
     * Custom view pager constructor
     * @param context
     */
    public LockableViewPager(Context context) {
        super(context);
    }

    /**
     * @param context application context
     * @param attrs xml set
     */
    public LockableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.swipeable = true;
    }

    /**
     * detect touch events on screen
     * @param event type of click
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.swipeable) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    /**
     * @param event
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.swipeable) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    /**
     * @param swipeable ability to swipe screen
     */
    public void setSwipeable(boolean swipeable) {
        this.swipeable = swipeable;
    }
}
