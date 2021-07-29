package com.example.myapplication.Fragments.FormFragment;

import androidx.test.rule.ActivityTestRule;

import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class FormFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        onView(withId(R.id.newPostButton)).perform(click());
    }

    @Test
    public void testCloseButtonClick(){
        onView(withId(R.id.formClose)).perform(click());
        onView(withId(R.id.mapFragmentContainer)).check(matches(isDisplayed()));
    }

    @Test
    public void testLocationButtonClick() {
        onView(withId(R.id.locationButton)).perform(click());
        onView(withId(R.id.userFeedForm)).check(matches(isDisplayed()));
    }

    @Test
    public void testChangingCategoryValue(){
        onView(withId(R.id.formSpinner)).perform(click());
        onView(withText("Environment")).perform(click());
    }

    @Test
    public void testAddingDescriptionText(){
        onView(withId(R.id.mapFeedDescription)).perform(typeText("An environmental issue"));
        closeSoftKeyboard();
        onView(withId(R.id.mapFeedDescription)).check(matches(withText("An environmental issue")));
    }

    @Test
    public void testInvalidCategoryError() {
        onView(withId(R.id.formSpinner)).perform(click());
        onView(withText("Select a category")).perform(click());
        onView(withId(R.id.mapFeedDescription)).perform(click());
        onView(withId(R.id.spinnerError)).check(matches(withText("Please select a valid category")));
    }

    @Test
    public void testInvalidDescriptionError() {
        onView(withId(R.id.mapFeedDescription)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.mapFeedSubmit)).perform(click());
        onView(withId(R.id.descriptionError)).check(matches(withText("Please enter a valid description")));
    }

    @Test
    public void testInvalidLocationError(){
        onView(withId(R.id.mapFeedSubmit)).perform(click());
        onView(withId(R.id.locationError)).check(matches(withText("Please select the location a specific location")));
    }


    @Test
    public void testClosingFragment(){
        onView(withId(R.id.formClose)).perform(click());
        onView(withId(R.id.mapFragmentContainer)).check(matches(isDisplayed()));
    }
}