package com.example.myapplication.Fragments.MapFeedSearchAutocompleteFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Handlers.MapFeedSearchAutocompleteHandler.MapFeedSearchAutocompleteHandler;
import com.example.myapplication.R;
import com.example.myapplication.Refactor.searchAutocomplete.Place;

public class MapFeedSearchAutocompleteFragment extends Fragment {

    public final static String TAG = MapFeedSearchAutocompleteFragment.class.getName();

    MapFeedSearchAutocompleteHandler mapFeedSearchAutocompleteHandler;
    FragmentAutocompleteListener listener;
    MapFragment mapFragment;

    public MapFeedSearchAutocompleteFragment(MapFragment mapFragment) {
        this.mapFragment = mapFragment;
    }

    public interface FragmentAutocompleteListener {
        void onInputAutocompleteSent(CharSequence input);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_map_feed_search_autocomplete, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mapFeedSearchAutocompleteHandler = new MapFeedSearchAutocompleteHandler(this, mapFragment);

        if(mapFragment != null && mapFragment instanceof FragmentAutocompleteListener){
            this.mapFeedSearchAutocompleteHandler.setListener((FragmentAutocompleteListener) mapFragment);
        }else{
            throw new RuntimeException(mapFragment.toString() + " must implemented FragmentAutocompleteListener");
        }
    }

    public void clearAutocomplete(){
        this.mapFeedSearchAutocompleteHandler.clearAutocomplete();
    }

    public void buildAutocompleteSearchItem(Place place, String mainText, String secondText){
        mapFeedSearchAutocompleteHandler.buildAutocompleteSearchItem(place, place.getMainText(), place.getSecondText());
    }

    public void updateEditText(CharSequence newText) { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(this.mapFeedSearchAutocompleteHandler != null){
             this.mapFeedSearchAutocompleteHandler.setListener(null);
        }
    }
}