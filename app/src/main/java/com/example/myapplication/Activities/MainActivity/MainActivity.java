package com.example.myapplication.Activities.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.myapplication.Fragments.MapFeedSearchAutocompleteFragment.MapFeedSearchAutocompleteFragment;
import com.example.myapplication.Fragments.MapFeedSearchFragment.MapFeedSearchFragment;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Handlers.MainActivityHandler.MainActivityHandler;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MainActivityHandler mainActivityHandler;

    public MainActivity() {
        this.mainActivityHandler = new MainActivityHandler(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mainActivityHandler.configure();

        resetSharedPreference();
        onNewIntent(getIntent());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        this.mainActivityHandler.handleNavMenuItemListener(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag("findThisFragment");

        if (fragment != null) {
            handleBackPressMapFragments(fragment);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent.getExtras() != null){
            Intent mapFragmentIntent = new Intent(MapFragment.class.toString());
            mapFragmentIntent.putExtra("voiceEnabled", intent.getExtras().getBoolean("voiceEnabled"));
            mapFragmentIntent.putExtra("userId", intent.getExtras().getString("userId"));
            mapFragmentIntent.putExtra("markerId", intent.getExtras().getString("markerId"));
            mapFragmentIntent.putExtra("category", intent.getExtras().getString("category"));
            mapFragmentIntent.putExtra("description", intent.getExtras().getString("description"));
            mapFragmentIntent.putExtra("lat", intent.getExtras().getString("lat"));
            mapFragmentIntent.putExtra("lng", intent.getExtras().getString("lng"));
            mapFragmentIntent.putExtra("firstName", intent.getExtras().getString("firstName"));
            mapFragmentIntent.putExtra("lastName", intent.getExtras().getString("lastName"));
            mapFragmentIntent.putExtra("rating", intent.getExtras().getString("rating"));
            sendBroadcast(mapFragmentIntent);
        }
    }

    void resetSharedPreference(){
        getApplicationContext().getSharedPreferences("Main_Fragment_Map_State", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Main_MapFeed_Map_State", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Map_Filter_Fragment", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Region_Geolocation_State", 0).edit().clear().apply();
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