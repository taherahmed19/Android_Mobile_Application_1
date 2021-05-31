package com.example.myapplication.Presenters.LoginPresenter;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import com.example.myapplication.Activities.LoginActivity.LoginActivity;
import com.example.myapplication.R;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    @Test
    public void testLoginWithEmptyEmail(){
        Espresso.onView(withId(R.id.loginEmail)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.loginSubmitButton)).perform(click());
        Espresso.onView(withId(R.id.loginEmailErrorMessage)).check(matches(withText("Field must be inputted")));
    }

    @Test
    public void testLoginWithEmptyPassword(){
        Espresso.onView(withId(R.id.loginPassword)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.loginSubmitButton)).perform(click());
        Espresso.onView(withId(R.id.loginEmailErrorMessage)).check(matches(withText("Field must be inputted")));
    }

    @Test
    public void testLoginWithInvalidEmail(){
        Espresso.onView(withId(R.id.loginEmail)).perform(typeText("email"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.loginPassword)).perform(click());
        Espresso.onView(withId(R.id.loginEmailErrorMessage)).check(matches(withText("Enter a valid email")));
    }

    @Test
    public void openRegisterActivity(){
        Espresso.onView(withId(R.id.loginRegisterButton)).perform(click());
        Espresso.onView(withId(R.id.registerActivity)).check(matches(isDisplayed()));
    }

}