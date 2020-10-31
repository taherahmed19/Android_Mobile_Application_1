package com.example.myapplication.Refactor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.example.myapplication.R;
import com.example.myapplication.Refactor.searchAutocomplete.poiLocator.POIFinder;
import com.example.myapplication.Refactor.searchAutocomplete.poiLocator.POILocatorFragment;
import com.example.myapplication.Refactor.searchAutocomplete.searchGeolocation.SearchGeolocation;
import com.google.android.gms.maps.model.LatLng;

import static android.app.Activity.RESULT_OK;

public class RoutingFragment extends Fragment {

    RoutingMap routingMap;
    ImageButton routingSearchButton;
    ImageButton routingSearchPOI;

    Fragment routingSearchAutocompleteFragment;

    public static final int REQUEST_CODE_SEARCH = 1;
    public static final int REQUEST_CODE_POI = 2;

    public RoutingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_routing, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.routingSearchAutocompleteFragment = new RoutingSearchAutocompleteFragment();
        this.routingMap = new RoutingMap(getContext(), getChildFragmentManager());
        this.routingSearchButton = (ImageButton) getView().findViewById(R.id.routingSearchButton);
        this.configureRoutingSearchButton();
        this.configurePOISearchButton();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode==RoutingFragment.REQUEST_CODE_SEARCH){
                String fromLocation = data.getStringExtra("from");
                String whereLocation = data.getStringExtra("where");

                SearchGeolocation searchGeolocation = new SearchGeolocation(getContext(), this);
                searchGeolocation.execute(fromLocation, whereLocation);
            }

            if (requestCode==RoutingFragment.REQUEST_CODE_POI){
                String selectedPOI = data.getStringExtra("selectedPOI");

                POIFinder poiFinder = new POIFinder(getContext(), new LatLng(51.5073899, -0.1364547), this);
                poiFinder.execute(selectedPOI);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.routingMap.getTomtomMap().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void configureRoutingSearchButton(){
        RoutingFragment routingFragment = this;
        this.routingSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoutingSearchAutocompleteFragment routingSearchAutocompleteFragment = new RoutingSearchAutocompleteFragment();
                RoutingSearchFragment routingSearchFragment = new RoutingSearchFragment(routingSearchAutocompleteFragment);
                routingSearchAutocompleteFragment.setRoutingSearchFragment(routingSearchFragment);

                FragmentTransition.TransitionActivityResult(getFragmentManager(), routingSearchFragment, routingFragment, R.anim.top_animation, R.anim.down_animation,
                        R.id.routingSearchPointer, RoutingFragment.REQUEST_CODE_SEARCH, "BLANK_FRAGMENT");
                FragmentTransition.Transition(getFragmentManager(), routingSearchAutocompleteFragment, R.anim.right_animations, R.anim.left_animation, R.id.routingSearchAutoPointer, "");
            }
        });
    }

    void configurePOISearchButton(){
        RoutingFragment routingFragment = this;
        routingSearchPOI = (ImageButton) getView().findViewById(R.id.routingSearchPOI);

        routingSearchPOI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                POILocatorFragment poiLocatorFragment = new POILocatorFragment();
                FragmentTransition.TransitionActivityResult(getFragmentManager(), poiLocatorFragment,
                        routingFragment, R.anim.right_animations, R.anim.left_animation, R.id.routingSearchPointer, RoutingFragment.REQUEST_CODE_POI, "BLANK_FRAGMENT");
            }
        });
    }

    public RoutingMap getRoutingMap() {
        return routingMap;
    }
}