package com.example.myapplication.Fragments.SearchFragment;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class SearchFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        onView(withId(R.id.mapSearchButton)).perform(click());
    }

    @Test
    public void testClosingFragment() {
        Espresso.onView(withId(R.id.mapFeedSearchClose)).perform(click());
        onView(withId(R.id.mapFragmentContainer)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddingTextToSearchBar(){
        Espresso.onView(withId(R.id.mapFeedSearch)).perform(typeText("London"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.mapFeedSearch)).check(matches(withText("London")));
    }

}