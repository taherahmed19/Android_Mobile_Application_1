package com.example.myapplication.Presenters.LoginPresenter;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import com.example.myapplication.Activities.LoginActivity.LoginActivity;
import com.example.myapplication.R;

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

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    @Test
    public void testLoginWithEmptyEmailError(){
        onView(withId(R.id.loginEmail)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.loginSubmitButton)).perform(click());
        onView(withId(R.id.loginEmailErrorMessage)).check(matches(withText("Field must be inputted")));
    }

    @Test
    public void testLoginWithEmptyPasswordError(){
        onView(withId(R.id.loginPassword)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.loginSubmitButton)).perform(click());
        onView(withId(R.id.loginEmailErrorMessage)).check(matches(withText("Field must be inputted")));
    }

    @Test
    public void testLoginWithInvalidEmailError(){
        onView(withId(R.id.loginEmail)).perform(typeText("email"));
        closeSoftKeyboard();
        onView(withId(R.id.loginPassword)).perform(click());
        onView(withId(R.id.loginEmailErrorMessage)).check(matches(withText("Enter a valid email")));
    }

    @Test
    public void testOpeningRegisterActivity(){
        onView(withId(R.id.loginRegisterButton)).perform(click());
        onView(withId(R.id.registerActivity)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWithValidCredentials(){
        onView(withId(R.id.loginEmail)).perform(typeText("email@email.com"));
        onView(withId(R.id.loginPassword)).perform(typeText("password"));
        onView(withId(R.id.loginSubmitButton)).perform(click());
        onView(withId(R.id.homepage)).check(matches(isDisplayed()));
    }
}