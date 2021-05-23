package com.example.myapplication.Presenters.SearchPresenter;

import android.location.Location;
import android.text.Editable;
import android.widget.EditText;

import com.example.myapplication.Interfaces.SearchContract.SearchContract;

public class SearchPresenter implements SearchContract.Presenter {

    SearchContract.View view;
    Location currentUserLocation;

    public SearchPresenter(SearchContract.View view) {
        this.view = view;
    }

    public void handleTextSearch(EditText mapFeedSearch, Editable s){
        view.handleTextSearch(mapFeedSearch, s);
    }

    public void handleSearchClose(){
        view.handleSearchClose();
    }

    public void handleCurrentLocationButton(){
        view.handleCurrentLocationButton();
    }

    public void updateLocation(Location location){
        this.currentUserLocation = location;
    }

    public Location getCurrentUserLocation(){
        return this.currentUserLocation;
    }
}
