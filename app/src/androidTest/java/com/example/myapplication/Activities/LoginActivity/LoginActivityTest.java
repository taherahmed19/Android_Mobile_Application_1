package com.example.myapplication.Activities.LoginActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.UiThreadTestRule;

import com.example.myapplication.Activities.BlankActivity.BlankActivity;
import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.Activities.RegisterActivity.RegisterActivity;
import com.example.myapplication.Handlers.LoginActivityHandler.LoginActivityHandler;
import com.example.myapplication.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    LoginActivityHandler loginActivityHandler = null;

    @Before
    public void setUp(){
        Intents.init();
        loginActivityHandler = new LoginActivityHandler(loginActivityActivityTestRule.getActivity());
        loginActivityActivityTestRule.launchActivity(new Intent());
    }

    @Test
    public void testLaunch()  {
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void testOpeningRegisterActivity(){
        onView(withId(R.id.loginRegisterButton)).perform(click());

        intended(hasComponent(RegisterActivity.class.getName()));
    }

    @Test
    public void testEmailField(){
        EditText loginEmail = (EditText) loginActivityActivityTestRule.getActivity().findViewById(R.id.loginEmail);
        onView(withId(R.id.loginEmail)).perform(typeText("email@email.com"));

        assertThat(loginEmail.getText().toString(), is("email@email.com"));
    }

    @Test
    public void testPasswordField(){
        EditText loginPassword = (EditText) loginActivityActivityTestRule.getActivity().findViewById(R.id.loginPassword);
        onView(withId(R.id.loginPassword)).perform(typeText("password"));

        assertThat(loginPassword.getText().toString(), is("password"));
    }

    @Test
    public void testSignIn() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);

        loginActivityHandler.setEmail("email@email.com");
        loginActivityHandler.setEmail("password");

        Service.doSomething(new Callback() {

            @Override
            public void onResponse(){
                // test response data
                // assertEquals(..
                // assertTrue(..
                // etc
                signal.countDown();// notify the count down latch
            }

        });
        signal.await();// wait for callback

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loginActivityHandler.submitSignIn();

            }

            @Override
            public void onResponse(){
                // test response data
                // assertEquals(..
                // assertTrue(..
                // etc
                signal.countDown();// notify the count down latch
            }
        });

        intended(hasComponent(MainActivity.class.getName()));
    }

    @After
    public void tearDown(){
        Intents.release();
    }

}