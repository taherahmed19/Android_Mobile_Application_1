package com.example.myapplication.Activities.RegisterActivity;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import com.example.myapplication.R;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<RegisterActivity> registerActivityTestRule = new ActivityTestRule<RegisterActivity>(RegisterActivity.class);

    @Test
    public void testRegistrationWithEmptyFirstNameField(){
        Espresso.onView(withId(R.id.registerFirstName)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.registerButton)).perform(click());
        Espresso.onView(withId(R.id.registerFirstNameErrorMessage)).check(matches(withText("Field must be inputted")));
    }

    @Test
    public void testRegistrationWithEmptyLastNameField(){
        Espresso.onView(withId(R.id.registerLastName)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.registerButton)).perform(click());
        Espresso.onView(withId(R.id.registerLastNameErrorMessage)).check(matches(withText("Field must be inputted")));
    }

    @Test
    public void testRegistrationWithEmptyEmailField(){
        Espresso.onView(withId(R.id.registerEmail)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.registerButton)).perform(click());
        Espresso.onView(withId(R.id.registerEmailErrorMessage)).check(matches(withText("Field must be inputted")));
    }

    @Test
    public void testRegistrationWithEmptyPasswordField(){
        Espresso.onView(withId(R.id.registerPassword)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.registerButton)).perform(click());
        Espresso.onView(withId(R.id.registerPasswordErrorMessage)).check(matches(withText("Field must be inputted")));
    }

    @Test
    public void testRegistrationWithEmptyConfirmField(){
        Espresso.onView(withId(R.id.registerConfirmPassword)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.registerButton)).perform(click());
        Espresso.onView(withId(R.id.registerConfirmPasswordErrorMessage)).check(matches(withText("Field must be inputted")));
    }

    @Test
    public void testRegistrationWithInvalidEmail(){
        Espresso.onView(withId(R.id.registerEmail)).perform(typeText("emamail.co.uk"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.registerPassword)).perform(click());
        Espresso.onView(withId(R.id.registerEmailErrorMessage)).check(matches(withText("Enter a valid email")));
    }

    @Test
    public void testRegistrationPasswordsNotMatch(){
        Espresso.onView(withId(R.id.registerPassword)).perform(typeText("password"));
        Espresso.onView(withId(R.id.registerConfirmPassword)).perform(typeText("passwor"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.registerPassword)).perform(click());
        Espresso.onView(withId(R.id.registerPasswordErrorMessage)).check(matches(withText("Password do not match")));
    }

//    @Test
//    public void testReturnToLoginActivity(){
//        Espresso.onView(withId(R.id.registerReturnButton)).perform(click());
//        Espresso.onView(withId(R.id.registerPassword)).check(matches(isDisplayed()));
//    }

}