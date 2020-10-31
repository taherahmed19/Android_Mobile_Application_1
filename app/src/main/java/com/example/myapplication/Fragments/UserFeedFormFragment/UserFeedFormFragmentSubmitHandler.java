package com.example.myapplication.Fragments.UserFeedFormFragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragments.ConfirmFragment.ConfirmFragment;
import com.example.myapplication.Fragments.ErrorFragment.ErrorFragment;
import com.example.myapplication.HttpRequest.DatabaseInsert.DatabaseInsert;
import com.example.myapplication.R;
import com.google.android.gms.maps.model.LatLng;

public class UserFeedFormFragmentSubmitHandler {
    DatabaseInsert databaseInsert;
    FragmentManager fragmentManager;

    UserFeedFormFragment userFeedForm;

    String description;
    LatLng geolocation;
    int category;

    boolean submitted;

    public UserFeedFormFragmentSubmitHandler(FragmentManager fragmentManager, UserFeedFormFragment userFeedForm, String description, LatLng geolocation, int category) {
        this.databaseInsert = new DatabaseInsert();
        this.fragmentManager = fragmentManager;
        this.userFeedForm = userFeedForm;
        this.description = description;
        this.geolocation = geolocation;
        this.category = category;
        this.submitted = false;
    }

    public void submit(){
        ErrorFragment errorFragment = new ErrorFragment(userFeedForm,
                userFeedForm.getResources().getString(R.string.post_error),
                userFeedForm.getResources().getString(R.string.post_error_message));

        ConfirmFragment confirmFragment = new ConfirmFragment(userFeedForm);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.userFeedForm,confirmFragment,"");
        transaction.addToBackStack(null);
        transaction.commit();
        String apiURL = "https://10.0.2.2:443/api/insertPost?marker=" + this.category + "&description=" + this.description + "&lat=" + this.geolocation.latitude + "&lng=" + this.geolocation.longitude;
        databaseInsert.execute(apiURL);
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }
}
