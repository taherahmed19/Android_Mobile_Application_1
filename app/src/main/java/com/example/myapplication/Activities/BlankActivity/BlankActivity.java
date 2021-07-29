package com.example.myapplication.Activities.BlankActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Activities.LoginActivity.LoginActivity;
import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.Interfaces.BlankContract.BlankContract;
import com.example.myapplication.Presenters.BlankPresenter.BlankPresenter;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData.LoginPreferenceData;
import com.google.firebase.iid.FirebaseInstanceId;


public class BlankActivity extends AppCompatActivity implements BlankContract.View {

    BlankPresenter blankPresenter;

    /**
     * initial method to execute for activity
     * @param savedInstanceState instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

        this.blankPresenter = new BlankPresenter(this);
        this.blankPresenter.loadStartActivity();

        Log.d("Print", "Firebase token "+ FirebaseInstanceId.getInstance().getToken());
    }

    /**
     * Executed after GPS permissions enabled to re-render the mao
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        this.blankPresenter.loadStartActivity();
    }

    /**
     * To execute once returned from main activity.
     * @param requestCode request from activity
     * @param resultCode response from custom class
     * @param data data to parse
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                this.blankPresenter.loadStartActivity();
            }

        }
    }

    /**
     * Decide which activity to render.
     * Dependent on if user if logged in
     */
    @Override
    public void loadStartActivity(){
        boolean userLoggedIn = LoginPreferenceData.getUserLoggedIn(this);
        final Class<? extends Activity> activity;

        if(userLoggedIn){
            activity = MainActivity.class;
        }else{
            activity = LoginActivity.class;
        }

        Intent newActivity = new Intent(this, activity);
        //request code for onActivityResult
        startActivityForResult(newActivity, 1);
    }

}