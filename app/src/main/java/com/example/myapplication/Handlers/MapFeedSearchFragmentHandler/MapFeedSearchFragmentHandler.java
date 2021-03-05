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
import android.widget.Toast;

import com.example.myapplication.Fragments.ErrorFragment.ErrorFragment;
import com.example.myapplication.Fragments.MapFeedSearchFragment.MapFeedSearchFragment;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.HttpRequest.HttpMapFeedSearch.HttpMapFeedSearch;
import com.example.myapplication.HttpRequest.HttpMapFeedSearchAutocomplete.HttpMapFeedSearchAutocomplete;
import com.example.myapplication.Interfaces.CurrentLocationListener.CurrentLocationListener;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.Utils.StringConstants.StringConstants;
import com.example.myapplication.Validators.MapFeedSearchFragmentValidator;
import com.google.android.gms.maps.model.LatLng;

import com.example.myapplication.Utils.Tools.Tools;

import static android.app.Activity.RESULT_OK;


public class MapFeedSearchFragmentHandler {

    public static int REQUEST_CODE_SEARCH = 2;

    MapFragment mapFragment;
    MapFeedSearchFragment mapFeedSearchFragment;

    MapFeedSearchFragment.FragmentSearchListener listener;
    CurrentLocationListener currentLocationListener;
    MapFeedSearchFragmentValidator validator;

    Location currentUserLocation;

    public MapFeedSearchFragmentHandler(MapFragment mapFragment, MapFeedSearchFragment mapFeedSearchFragment, CurrentLocationListener currentLocationListener) {
        this.mapFragment = mapFragment;
        this.mapFeedSearchFragment = mapFeedSearchFragment;
        this.validator = new MapFeedSearchFragmentValidator();
        this.currentLocationListener = currentLocationListener;
    }

    public void updateUserLocation(Location location) {
        this.currentUserLocation = location;
    }

    public void configureElements(){
        this.configureCurrentLocationBtn();
        this.configureLocationField();
        this.configureRoutingSearchClose();
        this.configureMapFeedSearchSubmit();
    }

    public void configureLocationField(){
        EditText mapFeedSearch = (EditText) mapFeedSearchFragment.getView().findViewById(R.id.mapFeedSearch);

        mapFeedSearch.setTag(false);
        mapFeedSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mapFeedSearch.setTag(true);
            }
        });

        mapFeedSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(validator.validateLocationField(s)){
                    if (mapFeedSearch.getBackground().getConstantState().equals(mapFeedSearch.getBackground().getConstantState())) {
                        mapFeedSearch.setBackgroundResource(R.drawable.map_search_input_border);
                    }

                    HttpMapFeedSearchAutocomplete httpMapFeedSearchAutocomplete = new HttpMapFeedSearchAutocomplete(getContext(), listener);
                    httpMapFeedSearchAutocomplete.execute(s.toString());
                }else{
                    Log.d("Print", "Search field is empty");
                    if ((Boolean) mapFeedSearch.getTag()) {
                        mapFeedSearch.setBackgroundResource(R.drawable.input_border_error_no_radius);
                        listener.onTriggerResultsClear();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    public int searchFieldLength(){
        EditText mapFeedSearch = (EditText) mapFeedSearchFragment.getView().findViewById(R.id.mapFeedSearch);

        return mapFeedSearch.getText().toString().trim().length();
    }

    void configureMapFeedSearchSubmit(){
        MapFeedSearchFragmentHandler instance = this;

        EditText mapFeedSearch = (EditText) mapFeedSearchFragment.getView().findViewById(R.id.mapFeedSearch);
        ImageButton mapSearchSubmit = (ImageButton) mapFeedSearchFragment.getView().findViewById(R.id.mapSearchSubmit);

        mapSearchSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validator.validateSubmission(mapFeedSearch)){
                    if(mapFeedSearchFragment.getActivity() != null){
                        Tools.HideKeyboard(mapFeedSearchFragment.getActivity());
                    }

                    String searchQuery = mapFeedSearch.getText().toString();
                    HttpMapFeedSearch httpMapFeedSearch = new HttpMapFeedSearch(instance);
                    httpMapFeedSearch.execute(searchQuery);
                } else{
                    mapFeedSearch.setBackgroundResource(R.drawable.input_border_error_no_radius);
                }
            }
        });
    }

    void configureRoutingSearchClose(){
        ImageButton mapFeedSearchClose = (ImageButton) this.mapFragment.getView().findViewById(R.id.mapFeedSearchClose);

        mapFeedSearchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapFeedSearchFragment.getFragmentManager() != null) {
                    mapFeedSearchFragment.getFragmentManager().popBackStack();
                    mapFeedSearchFragment.getFragmentManager().popBackStack();
                }
            }
        });
    }

    void configureCurrentLocationBtn(){
        Button mapFeedCurrentLocation = (Button) mapFeedSearchFragment.getView().findViewById(R.id.mapFeedCurrentLocation);

        mapFeedCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popFragments();
                returnFragmentData(new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude()));
            }
        });
    }

    public void updateEditText(CharSequence text){
        EditText mapFeedSearch = (EditText) this.mapFeedSearchFragment.getView().findViewById(R.id.mapFeedSearch);
        mapFeedSearch.setText(text, TextView.BufferType.EDITABLE);
    }

    public void popFragments(){
        if(mapFeedSearchFragment.getFragmentManager() != null){
            mapFeedSearchFragment.getFragmentManager().popBackStack();
            mapFeedSearchFragment.getFragmentManager().popBackStack();
        }
    }

    public void returnFragmentData(LatLng latLng){
        Intent intent = new Intent(mapFeedSearchFragment.getContext(), MapFragment.class);
        intent.putExtra(StringConstants.INTENT_MAP_FEED_SEARCH_LAT_LNG, latLng);

        if(mapFeedSearchFragment.getTargetFragment() != null){
            mapFeedSearchFragment.getTargetFragment().onActivityResult(mapFeedSearchFragment.getTargetRequestCode(), RESULT_OK, intent);
        }
    }

    public Context getContext(){
        return this.mapFragment.getContext();
    }

    public void setListener(MapFeedSearchFragment.FragmentSearchListener listener) {
        this.listener = listener;
    }
}
