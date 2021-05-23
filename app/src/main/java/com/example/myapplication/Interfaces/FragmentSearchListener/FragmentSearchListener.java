package com.example.myapplication.Interfaces.FragmentSearchListener;

import com.example.myapplication.Refactor.searchAutocomplete.Place;

public interface FragmentSearchListener {

    void onInputSearchSent(CharSequence input);
    void onSearchTextChanged(Place place, String mainText, String secondText);
    void onTriggerResultsClear();
    int checkSearchFieldLength();
}
