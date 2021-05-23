package com.example.myapplication.Fragments.SearchFragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Webservice.HttpMapFeedSearchAutocomplete.HttpMapFeedSearchAutocomplete;
import com.example.myapplication.Interfaces.CurrentLocationListener.CurrentLocationListener;
import com.example.myapplication.Interfaces.FragmentSearchListener.FragmentSearchListener;
import com.example.myapplication.Interfaces.SearchContract.SearchContract;
import com.example.myapplication.Interfaces.SearchListener.SearchListener;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Presenters.SearchPresenter.SearchPresenter;
import com.example.myapplication.R;
import com.example.myapplication.Utils.StringConstants.StringConstants;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;

public class SearchFragment extends Fragment implements CurrentLocationListener, SearchListener, SearchContract.View {

    public final static String TAG = SearchFragment.class.getName();
    public static int REQUEST_CODE_SEARCH = 2;

    MapFragment mapFragment;
    CurrentLocation currentLocation;

    SearchPresenter searchPresenter;
    FragmentSearchListener listener;

    public SearchFragment(MapFragment mapFragment, FragmentSearchListener listener) {
        this.listener = listener;
        this.mapFragment = mapFragment;
    }

    @Override
    public void updateUserLocation(Location location) {
        searchPresenter.updateLocation(location);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.currentLocation = new CurrentLocation(getActivity(), this);
        this.searchPresenter = new SearchPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_feed_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.configureCurrentLocationBtn();
        this.configureLocationField();
        this.configureSearchClose();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        currentLocation.startLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        currentLocation.stopLocationUpdates();
    }

    @Override
    public void handleTextSearch(EditText mapFeedSearch, Editable s){
        mapFeedSearch.setTag(true);

        if(validateLocationField(s)){
            if (mapFeedSearch.getBackground().getConstantState().equals(mapFeedSearch.getBackground().getConstantState())) {
                mapFeedSearch.setBackgroundResource(R.drawable.map_search_input_border);
            }

            HttpMapFeedSearchAutocomplete httpMapFeedSearchAutocomplete = new HttpMapFeedSearchAutocomplete(getContext(), listener);
            httpMapFeedSearchAutocomplete.execute(s.toString());
        }else{
            if ((Boolean) mapFeedSearch.getTag()) {
                mapFeedSearch.setBackgroundResource(R.drawable.input_border_error_no_radius);
                listener.onTriggerResultsClear();
            }
        }
    }

    @Override
    public void handleSearchClose(){
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void handleCurrentLocationButton(){
        popFragments();
        returnFragmentData(new LatLng(searchPresenter.getCurrentUserLocation().getLatitude(),
                searchPresenter.getCurrentUserLocation().getLongitude()));
    }

    @Override
    public void popFragments(){
        if(getFragmentManager() != null){
            getFragmentManager().popBackStack();
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void returnFragmentData(LatLng latLng){
        Intent intent = new Intent(getContext(), MapFragment.class);
        intent.putExtra(StringConstants.INTENT_MAP_FEED_SEARCH_LAT_LNG, latLng);

        if(getTargetFragment() != null){
            getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
        }
    }

    public int searchFieldLength(){
        EditText mapFeedSearch = (EditText) getView().findViewById(R.id.mapFeedSearch);

        return mapFeedSearch.getText().toString().trim().length();
    }

    public void updateEditText(CharSequence text){
        EditText mapFeedSearch = (EditText) this.getView().findViewById(R.id.mapFeedSearch);
        mapFeedSearch.setText(text, TextView.BufferType.EDITABLE);
    }

    public boolean validateSubmission(EditText mapFeedSearch){
        return !TextUtils.isEmpty(mapFeedSearch.getText());
    }

    public boolean validateLocationField(Editable s){
        return s.toString().trim().length() > 0;
    }

    public Context getContext(){
        return this.mapFragment.getContext();
    }

    void configureLocationField(){
        EditText mapFeedSearch = (EditText) getView().findViewById(R.id.mapFeedSearch);

        mapFeedSearch.setTag(false);
        mapFeedSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        mapFeedSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                searchPresenter.handleTextSearch(mapFeedSearch, s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    void configureSearchClose(){
        ImageButton mapFeedSearchClose = (ImageButton) this.mapFragment.getView().findViewById(R.id.mapFeedSearchClose);

        mapFeedSearchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPresenter.handleSearchClose();
            }
        });
    }

    void configureCurrentLocationBtn(){
        Button mapFeedCurrentLocation = (Button) getView().findViewById(R.id.mapFeedCurrentLocation);

        mapFeedCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPresenter.handleCurrentLocationButton();
            }
        });
    }


}