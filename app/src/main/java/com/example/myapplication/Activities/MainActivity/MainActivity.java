package com.example.myapplication.Activities.MainActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Interfaces.MainContract.MainContract;
import com.example.myapplication.Presenters.MainPresenter.MainPresenter;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData.LoginPreferenceData;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainContract.View {

    private MainPresenter mainPresenter;
    private DrawerLayout drawerLayout;

    /**
     * initial method to execute for activity
     * @param savedInstanceState instance
     */
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

    /**
     * for GPS permission result
     * @param requestCode request code from activity
     * @param permissions premission type
     * @param grantResults result value
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        finish();
        startActivity(getIntent());
    }

    /**
     * Side nav functionality
     * @param item menu item
     * @return boolean
     */
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
        //mainPresenter.handleBackPressed();
    }

    /**
     * Intent to be received from Firebase service
     * @param intent instance inject from service.
     */
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
            sendBroadcast(mapFragmentIntent);
        }
    }

    /**
     * Configure XML
     */
    void configureDrawerLayout(){
        drawerLayout = this.findViewById(R.id.homepage);
    }

    /**
     * Configure XML
     */
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

    /**
     * XML layout order
     */
    void configureNavigation(){
        configureNavigationListener();
        configureNavHeaderName();
        configureNavHeaderEmail();
        configureNavHeaderUserIcon();
    }

    /**
     * Configure XML
     */
    void configureNavigationListener(){
        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Configure XML
     */
    void configureNavHeaderName(){
        String firstName = LoginPreferenceData.getUserFirstName(this);
        String lastName = LoginPreferenceData.getUserLastName(this);

        String concatName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1) + " " + lastName.substring(0, 1).toUpperCase() + lastName.substring(1);

        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);

        TextView text = headerLayout.findViewById(R.id.nav_header_name);
        text.setText(concatName);
    }

    /**
     * Configure XML
     */
    void configureNavHeaderEmail(){
        String email = LoginPreferenceData.getUserEmail(this);

        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);

        TextView text = headerLayout.findViewById(R.id.nav_header_email);
        text.setText(email);
    }

    /**
     * Configure XML
     */
    void configureNavHeaderUserIcon(){
        String firstName = LoginPreferenceData.getUserFirstName(this);
        String lastName = LoginPreferenceData.getUserLastName(this);

        String concatInitials = firstName.substring(0, 1).toUpperCase() + " " + lastName.substring(0, 1).toUpperCase();

        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);

        TextView nav_header_name_logo = (TextView) headerLayout.findViewById(R.id.nav_header_name_logo);
        nav_header_name_logo.setText(concatInitials);
    }

    /**
     * Reset local storage
     */
    void resetSharedPreference(){
        getApplicationContext().getSharedPreferences("Main_Fragment_Map_State", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Main_MapFeed_Map_State", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Map_Filter_Fragment", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Region_Geolocation_State", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Radius_Marker_Settings", 0).edit().clear().apply();
        getApplicationContext().getSharedPreferences("Jwt_token", 0).edit().clear().apply();
    }

   /* @Override
    public void handleBackPressed() {
        Fragment fragment = (Fragment) this.getSupportFragmentManager().findFragmentByTag("findThisFragment");

        if (fragment != null) {
            mainPresenter.handleBackPressMapFragments(fragment);
        }
    }

    @Override
    public void handleBackPressMapFragments(Fragment fragment) {
        SearchFragment searchFragment = (SearchFragment) fragment.getChildFragmentManager().findFragmentByTag(SearchFragment.TAG);
        SearchResultsFragment searchResultsFragment = (SearchResultsFragment) fragment.getChildFragmentManager().findFragmentByTag(SearchResultsFragment.TAG);

        if(searchFragment != null){
            searchFragment.getFragmentManager().popBackStack();
        }

        if(searchResultsFragment != null){
            searchResultsFragment.getFragmentManager().popBackStack();
        }
    }*/
}