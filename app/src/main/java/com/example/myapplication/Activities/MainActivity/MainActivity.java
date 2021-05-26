package com.example.myapplication.Activities.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.Fragments.SearchAutocompleteFragment.SearchAutocompleteFragment;
import com.example.myapplication.Fragments.SearchFragment.SearchFragment;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Interfaces.MainContract.MainContract;
import com.example.myapplication.Presenters.MainPresenter.MainPresenter;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainContract.View {

    private MainPresenter mainPresenter;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter = new MainPresenter(this);

        configureDrawerLayout();
        configureHeaderMenuButton();
        configureNavigation();
        resetSharedPreference();
        onNewIntent(getIntent());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_sign_out) {
            LoginPreferenceData.clear(this.getApplicationContext());
            this.finish();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        mainPresenter.handleBackPressed();
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

    void configureDrawerLayout(){
        drawerLayout = this.findViewById(R.id.homepage);
    }

    void configureHeaderMenuButton(){
        ImageButton userMenu = (ImageButton) this.findViewById(R.id.userMenu);

        userMenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    void configureNavigation(){
        configureNavigationListener();
        configureNavHeaderName();
        configureNavHeaderEmail();
        configureNavHeaderUserIcon();
    }

    void configureNavigationListener(){
        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    void configureNavHeaderName(){
        String firstName = LoginPreferenceData.getUserFirstName(this);
        String lastName = LoginPreferenceData.getUserLastName(this);

        String concatName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1) + " " + lastName.substring(0, 1).toUpperCase() + lastName.substring(1);

        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);

        TextView text = headerLayout.findViewById(R.id.nav_header_name);
        text.setText(concatName);
    }

    void configureNavHeaderEmail(){
        String email = LoginPreferenceData.getUserEmail(this);

        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);

        TextView text = headerLayout.findViewById(R.id.nav_header_email);
        text.setText(email);
    }

    void configureNavHeaderUserIcon(){
        String firstName = LoginPreferenceData.getUserFirstName(this);
        String lastName = LoginPreferenceData.getUserLastName(this);

        String concatInitials = firstName.substring(0, 1).toUpperCase() + " " + lastName.substring(0, 1).toUpperCase();

        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);

        TextView nav_header_name_logo = (TextView) headerLayout.findViewById(R.id.nav_header_name_logo);
        nav_header_name_logo.setText(concatInitials);
    }

    void resetSharedPreference(){
        getApplicationContext().getSharedPreferences("Main_Fragment_Map_State", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Main_MapFeed_Map_State", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Map_Filter_Fragment", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Region_Geolocation_State", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Radius_Marker_Settings", 0).edit().clear().apply();
    }

    @Override
    public void handleBackPressed() {
        Fragment fragment = (Fragment) this.getSupportFragmentManager().findFragmentByTag("findThisFragment");

        if (fragment != null) {
            mainPresenter.handleBackPressMapFragments(fragment);
        }
    }

    @Override
    public void handleBackPressMapFragments(Fragment fragment) {
        SearchFragment searchFragment = (SearchFragment) fragment.getChildFragmentManager().findFragmentByTag(SearchFragment.TAG);
        SearchAutocompleteFragment searchAutocompleteFragment = (SearchAutocompleteFragment) fragment.getChildFragmentManager().findFragmentByTag(SearchAutocompleteFragment.TAG);

        if(searchFragment != null){
            searchFragment.getFragmentManager().popBackStack();
        }

        if(searchAutocompleteFragment != null){
            searchAutocompleteFragment.getFragmentManager().popBackStack();
        }
    }
}