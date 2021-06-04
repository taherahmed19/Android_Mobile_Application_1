package com.example.myapplication.Activities.RegisterActivity;

import androidx.test.rule.ActivityTestRule;

import com.example.myapplication.R;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<RegisterActivity> registerActivityTestRule = new ActivityTestRule<RegisterActivity>(RegisterActivity.class);

    @Test
    public void testRegistrationWithEmptyFirstNameFieldError(){
        onView(withId(R.id.registerFirstName)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.registerFirstNameErrorMessage)).check(matches(withText("Field must be inputted")));
    }

    @Test
    public void testRegistrationWithEmptyLastNameFieldError(){
        onView(withId(R.id.registerLastName)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.registerLastNameErrorMessage)).check(matches(withText("Field must be inputted")));
    }

    @Test
    public void testRegistrationWithEmptyEmailFieldError(){
        onView(withId(R.id.registerEmail)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.registerEmailErrorMessage)).check(matches(withText("Field must be inputted")));
    }

    @Test
    public void testRegistrationWithEmptyPasswordFieldError(){
        onView(withId(R.id.registerPassword)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.registerPasswordErrorMessage)).check(matches(withText("Field must be inputted")));
    }

    @Test
    public void testRegistrationWithEmptyConfirmFieldError(){
        onView(withId(R.id.registerConfirmPassword)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.registerConfirmPasswordErrorMessage)).check(matches(withText("Field must be inputted")));
    }

    @Test
    public void testRegistrationWithInvalidEmailError(){
        onView(withId(R.id.registerEmail)).perform(typeText("emamail.co.uk"));
        closeSoftKeyboard();
        onView(withId(R.id.registerPassword)).perform(click());
        onView(withId(R.id.registerEmailErrorMessage)).check(matches(withText("Enter a valid email")));
    }

    @Test
    public void testRegistrationPasswordsNotMatch(){
        onView(withId(R.id.registerPassword)).perform(typeText("password"));
        onView(withId(R.id.registerConfirmPassword)).perform(typeText("passwor"));
        closeSoftKeyboard();
        onView(withId(R.id.registerPassword)).perform(click());
        onView(withId(R.id.registerPasswordErrorMessage)).check(matches(withText("Password do not match")));
    }

//    @Test
//    public void testReturnToLoginActivity(){
//        Espresso.onView(withId(R.id.registerReturnButton)).perform(click());
//        Espresso.onView(withId(R.id.registerPassword)).check(matches(isDisplayed()));
//    }
}