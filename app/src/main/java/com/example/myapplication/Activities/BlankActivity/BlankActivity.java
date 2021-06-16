package com.example.myapplication.Activities.BlankActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
        this.blankPresenter.loadStartActivity();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.blankPresenter.loadStartActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                this.blankPresenter.loadStartActivity();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    } //onActivityResult

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
        startActivityForResult(newActivity, 1);

    }

}