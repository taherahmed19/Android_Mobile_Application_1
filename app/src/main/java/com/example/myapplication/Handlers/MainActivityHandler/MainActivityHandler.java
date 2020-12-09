package com.example.myapplication.Handlers.MainActivityHandler;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.google.android.material.navigation.NavigationView;
import com.tomtom.online.sdk.common.util.StringUtils;

import org.w3c.dom.Text;

public class MainActivityHandler {

    MainActivity mainActivity;
    DrawerLayout drawerLayout;

    public MainActivityHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void configure(){
        configureDrawerLayout();
        configureHeaderMenuButton();
        configureNavigation();
    }

    void configureDrawerLayout(){
        drawerLayout = this.mainActivity.findViewById(R.id.homepage);
    }

    void configureHeaderMenuButton(){
        ImageButton userMenu = (ImageButton) this.mainActivity.findViewById(R.id.userMenu);

        userMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    void configureNavigation(){
        configureNavName();
    }

    void configureNavName(){
        String firstName = LoginPreferenceData.getUserFirstName(this.mainActivity);
        String lastName = LoginPreferenceData.getUserLastName(this.mainActivity);
        String concatName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1) + " " + lastName.substring(0, 1).toUpperCase() + lastName.substring(1);

        NavigationView navigationView = (NavigationView) this.mainActivity.findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);

        TextView text = headerLayout.findViewById(R.id.nav_header_name);
        text.setText(concatName);
    }
}
