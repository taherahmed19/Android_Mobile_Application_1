package com.example.myapplication.JsonBuilders.MapFeedSearchAutocompleteJsonBuilder;

import android.util.Log;

import com.example.myapplication.Refactor.searchAutocomplete.Places;
import com.example.myapplication.Utils.JsonUtil.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapFeedSearchAutocompleteJsonBuilder {

    public Places jsonHandler(String jsonString){
        Places places = getAutocompleteData(jsonString);

        return places;
    }

    public Places getAutocompleteData(String jsonString){
        Places places = null;

        try {
            places = new Places();
            JSONObject placesObject = new JSONObject(jsonString);

            JSONArray predictions = placesObject.getJSONArray("predictions");

            for(int i = 0; i < predictions.length(); i++){
                String predictionsItem = JsonUtil.getString(predictions.getJSONObject(i), "description");

                JSONArray predictionsTypes = JsonUtil.getJsonArray(predictions.getJSONObject(i), "types");

                JSONObject predictionsFormatting = JsonUtil.getJsonObject(predictions.getJSONObject(i), "structured_formatting");

                String mainText = JsonUtil.getString(predictionsFormatting, "main_text");
                String secondText = JsonUtil.getString(predictionsFormatting, "secondary_text");

                places.addAutocompletePlace(predictionsItem, predictionsTypes, mainText, secondText);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return places;
    }


}
