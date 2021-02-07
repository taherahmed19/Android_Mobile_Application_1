package com.example.myapplication.Activities.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.myapplication.Fragments.MapFeedSearchAutocompleteFragment.MapFeedSearchAutocompleteFragment;
import com.example.myapplication.Fragments.MapFeedSearchFragment.MapFeedSearchFragment;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Handlers.MainActivityHandler.MainActivityHandler;
import com.example.myapplication.HttpRequest.HttpFirebaseToken.HttpFirebaseToken;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.Objects;

import timber.log.Timber;

import static com.example.myapplication.Models.CurrentLocation.CurrentLocation.MY_PERMISSIONS_REQUEST_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MainActivityHandler mainActivityHandler;
    boolean received;

    public MainActivity() {
        this.mainActivityHandler = new MainActivityHandler(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mainActivityHandler.configure();

        resetSharedPreference();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        this.mainActivityHandler.handleNavMenuItemListener(item);

        return true;
    }

    void resetSharedPreference(){
        getApplicationContext().getSharedPreferences("Main_Fragment_Map_State", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Main_MapFeed_Map_State", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Map_Filter_Fragment", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Region_Geolocation_State", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Map_Filter_Fragment_Settings", 0).edit().clear().apply();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag("findThisFragment");

        if (fragment != null) {
            handleBackPressMapFragments(fragment);
        }
    }

    void handleBackPressMapFragments(Fragment fragment){
        MapFeedSearchFragment mapFeedSearchFragment = (MapFeedSearchFragment) fragment.getChildFragmentManager().findFragmentByTag(MapFeedSearchFragment.TAG);
        MapFeedSearchAutocompleteFragment mapFeedSearchAutocompleteFragment = (MapFeedSearchAutocompleteFragment) fragment.getChildFragmentManager().findFragmentByTag(MapFeedSearchAutocompleteFragment.TAG);

        if(mapFeedSearchFragment != null){
            mapFeedSearchFragment.getFragmentManager().popBackStack();
        }

        if(mapFeedSearchAutocompleteFragment != null){
            mapFeedSearchAutocompleteFragment.getFragmentManager().popBackStack();
        }
    }
}