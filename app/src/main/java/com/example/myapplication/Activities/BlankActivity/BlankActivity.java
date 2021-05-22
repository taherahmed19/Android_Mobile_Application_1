package com.example.myapplication.Activities.BlankActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.Activities.LoginActivity.LoginActivity;
import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.Interfaces.BlankContract.BlankContract;
import com.example.myapplication.Presenters.BlankPresenter.BlankPresenter;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;

public class BlankActivity extends AppCompatActivity implements BlankContract.View {

    BlankPresenter blankPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

        this.blankPresenter = new BlankPresenter(this);
        this.blankPresenter.renderActivities();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.blankPresenter.renderActivities();
    }

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
        this.startActivity(newActivity);
    }

}