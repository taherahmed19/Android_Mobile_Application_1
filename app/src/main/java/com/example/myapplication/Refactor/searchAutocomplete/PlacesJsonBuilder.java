package com.example.myapplication.Refactor.searchAutocomplete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlacesJsonBuilder {

    public PlacesJsonBuilder() {
    }

    public Places buildJSON(String jsonString){
        Places places = null;

        try {
            places = new Places();
            JSONObject placesObject = new JSONObject(jsonString);

            JSONArray predictions = placesObject.getJSONArray("predictions");

            for(int i = 0; i < predictions.length(); i++){
                String predictionsItem = predictions.getJSONObject(i).getString("description");
                JSONArray predictionsTypes = predictions.getJSONObject(i).getJSONArray("types");
                JSONObject predictionsFormatting = predictions.getJSONObject(i).getJSONObject("structured_formatting");
                String mainText = predictionsFormatting.getString("main_text");
                String secondText = predictionsFormatting.getString("secondary_text");

                places.addAutocompletePlace(predictionsItem, predictionsTypes, mainText, secondText);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return places;
    }
}
