package com.example.myapplication.Refactor.searchAutocomplete;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Places {

    ArrayList<Place> places;

    public Places() {
        this.places = new ArrayList<>();
    }

    public void addAutocompletePlace(String description, JSONArray typesJson, String mainText, String secondText){
        ArrayList<String> types = convertJSONArray(typesJson);

        this.places.add(new Place(description, types, mainText, secondText));
    }

    ArrayList<String> convertJSONArray(JSONArray typesJson){
        ArrayList<String> types = new ArrayList<>();

        if(typesJson.length() > 0){
            for(int i = 0; i < typesJson.length(); i++){
                try {
                    types.add(typesJson.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return types;
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }
}

