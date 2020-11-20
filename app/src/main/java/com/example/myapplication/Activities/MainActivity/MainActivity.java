package com.example.myapplication.Activities.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.Fragments.MapFeedSearchAutocompleteFragment.MapFeedSearchAutocompleteFragment;
import com.example.myapplication.Fragments.MapFeedSearchFragment.MapFeedSearchFragment;
import com.example.myapplication.Handlers.MapHandler.MapHandler;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.R;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resetSharedPreference();
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

//    @Override
//    public String callback() {
//        return "current location " + currentLocation.currentLocation;
//    }

//    void BottomNavigationBar(){
//        final MainFragment mainFragment = new MainFragment();
//        final ActivityUserAlerts activityUserAlerts = new ActivityUserAlerts();
//        final RoutingFragment routingFragment = new RoutingFragment();
//
//        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                switch (item.getItemId()) {
//                    case R.id.homepage_fragment:
//                        bottomNavOpenFragment(mainFragment);
//                        break;
//                    case R.id.map_fragment:
//                        bottomNavOpenFragment(activityUserAlerts);
//                        break;
//                    case R.id.weather_fragment:
//                        break;
//                    case R.id.travel_fragment:
//                        bottomNavOpenFragment(routingFragment);
//                        break;
//                }
//                return true;
//            }
//
//        });
//    }

//    public void bottomNavOpenFragment(Fragment fragment){
//        try{
//            FragmentTransition.OpenFragment(getSupportFragmentManager(), fragment, R.id.homepage, "");
//        }catch(Exception e){
//            throw new RuntimeException(e.toString());
//        }
//    }

}