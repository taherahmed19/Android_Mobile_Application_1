package com.example.myapplication.Fragments.MapFragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myapplication.Adapters.LockableViewPager.LockableViewPager;
import com.example.myapplication.Fragments.MapFeedSearchAutocompleteFragment.MapFeedSearchAutocompleteFragment;
import com.example.myapplication.Fragments.MapFeedSearchFragment.MapFeedSearchFragment;
import com.example.myapplication.Fragments.MapFilterFragment.MapFilterFragment;
import com.example.myapplication.Handlers.MapFeedFragmentHandler.MapFeedFragmentHandler;
import com.example.myapplication.HttpRequest.HttpMap.HttpMap;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Models.LoadingSpinner.LoadingSpinner;
import com.example.myapplication.Models.Settings.Settings;
import com.example.myapplication.R;
import com.example.myapplication.Refactor.searchAutocomplete.Place;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

public class MapFragment extends Fragment implements MapFeedSearchFragment.FragmentSearchListener, MapFeedSearchAutocompleteFragment.FragmentAutocompleteListener
                                                     , MapFilterFragment.FragmentMapFilterListener {
    CurrentLocation currentLocation;
    HttpMap httpMap;
    SupportMapFragment mapFragment;

    LoadingSpinner loadingSpinner;
    MapFeedFragmentHandler mapFeedFragmentHandler;

    LockableViewPager viewPager;

    public MapFragment(LockableViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mapFeedFragmentHandler = new MapFeedFragmentHandler(this, viewPager);

        this.loadingSpinner = new LoadingSpinner((ProgressBar) getView().findViewById(R.id.feedLoadingSpinner));
        this.mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        this.mapFeedFragmentHandler.configureElements();

        this.httpMap = new HttpMap(getActivity(), getChildFragmentManager(), mapFragment, this, loadingSpinner, null);
        this.httpMap.execute("https://10.0.2.2:443/api/getmarkers");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.loadingSpinner.show();

        LatLng latLng = this.mapFeedFragmentHandler.handleResult(requestCode, resultCode, data);

        if(latLng != null){
            if(httpMap.getDatabaseMarkerMap() != null){
                httpMap.getDatabaseMarkerMap().setMapLocation(latLng);
                this.loadingSpinner.hide();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            updateMapFeed();
        }else{
            try{
                if(httpMap != null){
                    httpMap.saveMapCameraPosition();
                    httpMap = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void updateMapFeed(){
        try{
            if(httpMap == null){
//                databaseMarkers = new DatabaseMarkers(getActivity(), getChildFragmentManager(), mapFragment, this, loadingSpinner, null);
//                databaseMarkers.execute("https://10.0.2.2:443/api/getmarkers");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showFeedMapContainer(){
        this.mapFeedFragmentHandler.showFeedMapContainer();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String permissions[], @NotNull int[] grantResults) {
        if (requestCode == CurrentLocation.MY_PERMISSIONS_REQUEST_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                currentLocation.accessGeolocation();
            }
        }
    }

    @Override
    public void onInputAutocompleteSent(CharSequence input) {
        this.mapFeedFragmentHandler.getMapFeedSearchFragment().updateEditText(input);
    }

    @Override
    public void onInputSearchSent(CharSequence input) {
        this.mapFeedFragmentHandler.getMapFeedSearchAutocompleteFragment().updateEditText(input);
    }

    @Override
    public void onSearchTextChanged(Place place, String mainText, String secondText) {
        this.mapFeedFragmentHandler.getMapFeedSearchAutocompleteFragment().buildAutocompleteSearchItem(place, mainText, secondText);
    }

    @Override
    public void onTriggerResultsClear() {
        this.mapFeedFragmentHandler.getMapFeedSearchAutocompleteFragment().clearAutocomplete();
    }

    @Override
    public int checkSearchFieldLength() {
        return this.mapFeedFragmentHandler.getMapFeedSearchFragment().searchFieldLength();
    }

    @Override
    public void onSettingsUpdated(Settings settings) {
        try{
            httpMap = new HttpMap(getActivity(), getChildFragmentManager(), mapFragment, this, loadingSpinner, settings);
            httpMap.execute("https://10.0.2.2:443/api/getmarkers");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}