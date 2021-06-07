package com.example.myapplication.Fragments.MapFragment;

import androidx.test.rule.ActivityTestRule;

import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Distance.Distance;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> loginActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    MapFragment mapFragment;

    @Before
    public void setUp() throws Exception {
        mapFragment = new MapFragment(null);
    }

    @Test
    public void testDistanceBetweenTwoPoints() {
        double startingLat = 51.5285582;
        double startingLng = -0.2416809;
        double destinationLat = 51.566988;
        double destinationLng = -0.1449927;

        double distance = Distance.CalculatePointsDistance(startingLat, startingLng, destinationLat, destinationLng);

        assertEquals(7953.97, distance, 0.1);
    }

    @Test
    public void testOpeningSearchFragment(){
        onView(withId(R.id.mapSearchButton)).perform(click());
        onView(withId(R.id.searchFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void testOpeningAutocompleteFragment(){
        onView(withId(R.id.mapSearchButton)).perform(click());
        onView(withId(R.id.searchAutocompleteContainer)).check(matches(isDisplayed()));
    }

    @Test
    public void testOpeningFormFragment(){
        onView(withId(R.id.newPostButton)).perform(click());
        onView(withId(R.id.userFeedForm)).check(matches(isDisplayed()));
    }

    @Test
    public void testMapRefreshClick(){
        onView(withId(R.id.mapRefreshBtn)).perform(click());
        onView(withId(R.id.mapFragmentContainer)).check(matches(isDisplayed()));
    }

    @Test
    public void testMapReturnClick(){
        onView(withId(R.id.mapLocationResetBtn)).perform(click());
    }

    @Test
    public void testBroadcastReceiverRegistered(){
        assertNotNull(mapFragment.getReceiver());
    }

    @Test
    public void testOpeningNavigationDrawer(){
        onView(withId(R.id.userMenu)).perform(click());
        onView(withId(R.id.nav_sign_out)).check(matches(isDisplayed()));
    }

    @Test
    public void testClosingNavigationDrawer(){
        onView(withId(R.id.userMenu)).perform(click());
        onView(withId(R.id.mapFragmentContainer)).perform(click());
        onView(withId(R.id.mapFragmentContainer)).check(matches(isDisplayed()));
    }

}