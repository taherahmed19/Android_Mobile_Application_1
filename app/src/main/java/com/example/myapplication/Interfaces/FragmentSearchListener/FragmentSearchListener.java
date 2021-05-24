package com.example.myapplication.Interfaces.FragmentSearchListener;

import com.example.myapplication.Models.Place.Place;

public interface FragmentSearchListener {

    void onSearchTextChanged(Place place, String mainText, String secondText);
    void onTriggerResultsClear();
    int checkSearchFieldLength();

}
