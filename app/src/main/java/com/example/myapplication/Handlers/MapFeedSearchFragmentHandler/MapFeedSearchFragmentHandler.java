package com.example.myapplication.Handlers.MapFeedSearchFragmentHandler;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.Fragments.SearchFragment.SearchFragment;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.HttpRequest.HttpMapFeedSearch.HttpMapFeedSearch;
import com.example.myapplication.HttpRequest.HttpMapFeedSearchAutocomplete.HttpMapFeedSearchAutocomplete;
import com.example.myapplication.Interfaces.CurrentLocationListener.CurrentLocationListener;
import com.example.myapplication.R;
import com.example.myapplication.Utils.StringConstants.StringConstants;
import com.example.myapplication.Validators.MapFeedSearchFragmentValidator;
import com.google.android.gms.maps.model.LatLng;

import com.example.myapplication.Utils.Tools.Tools;

import static android.app.Activity.RESULT_OK;


public class MapFeedSearchFragmentHandler {

    public static int REQUEST_CODE_SEARCH = 2;

    MapFragment mapFragment;
    SearchFragment searchFragment;

    CurrentLocationListener currentLocationListener;
    MapFeedSearchFragmentValidator validator;


    public MapFeedSearchFragmentHandler(MapFragment mapFragment, SearchFragment searchFragment, CurrentLocationListener currentLocationListener) {
        this.mapFragment = mapFragment;
        this.searchFragment = searchFragment;
        this.validator = new MapFeedSearchFragmentValidator();
        this.currentLocationListener = currentLocationListener;
    }

//    public void updateUserLocation(Location location) {
//    }

//    public int searchFieldLength(){
//        EditText mapFeedSearch = (EditText) searchFragment.getView().findViewById(R.id.mapFeedSearch);
//
//        return mapFeedSearch.getText().toString().trim().length();
//    }

//    void configureMapFeedSearchSubmit(){
//        MapFeedSearchFragmentHandler instance = this;
//
//        EditText mapFeedSearch = (EditText) searchFragment.getView().findViewById(R.id.mapFeedSearch);
//        ImageButton mapSearchSubmit = (ImageButton) searchFragment.getView().findViewById(R.id.mapSearchSubmit);
//
//        mapSearchSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(validator.validateSubmission(mapFeedSearch)){
//                    if(searchFragment.getActivity() != null){
//                        Tools.HideKeyboard(searchFragment.getActivity());
//                    }
//
//                    String searchQuery = mapFeedSearch.getText().toString();
//                    HttpMapFeedSearch httpMapFeedSearch = new HttpMapFeedSearch(instance);
//                    httpMapFeedSearch.execute(searchQuery);
//                } else{
//                    mapFeedSearch.setBackgroundResource(R.drawable.input_border_error_no_radius);
//                }
//            }
//        });
//    }


//    public void updateEditText(CharSequence text){
//        EditText mapFeedSearch = (EditText) this.searchFragment.getView().findViewById(R.id.mapFeedSearch);
//        mapFeedSearch.setText(text, TextView.BufferType.EDITABLE);
//    }
//
//    public void popFragments(){
//        if(searchFragment.getFragmentManager() != null){
//            searchFragment.getFragmentManager().popBackStack();
//            searchFragment.getFragmentManager().popBackStack();
//        }
//    }
//
//    public void returnFragmentData(LatLng latLng){
//        Intent intent = new Intent(searchFragment.getContext(), MapFragment.class);
//        intent.putExtra(StringConstants.INTENT_MAP_FEED_SEARCH_LAT_LNG, latLng);
//
//        if(searchFragment.getTargetFragment() != null){
//            searchFragment.getTargetFragment().onActivityResult(searchFragment.getTargetRequestCode(), RESULT_OK, intent);
//        }
//    }
//
//    public Context getContext(){
//        return this.mapFragment.getContext();
//    }
//
//    public void setListener(SearchFragment.FragmentSearchListener listener) {
//        this.listener = listener;
//    }
}
