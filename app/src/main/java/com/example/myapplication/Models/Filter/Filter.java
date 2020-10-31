package com.example.myapplication.Models.Filter;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.Models.FilterSettings.FilterSettings;
import com.example.myapplication.Models.FilteredRegion.FilteredRegion;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Filter {

    public Filter(){
    }

    public static void saveFilterState(Context context, String filteredJSON){
        SharedPreferences filterState = context.getSharedPreferences("Map_Filter_Fragment", 0);
        SharedPreferences.Editor filterEditor = filterState.edit();
        filterEditor.putString("Map_Filter", filteredJSON);
        filterEditor.apply();
    }

    public static FilterSettings retrieveFilterJson(FragmentActivity fragmentActivity){
        SharedPreferences pref = fragmentActivity.getSharedPreferences("Map_Filter_Fragment", 0);
        String mapTypeString = pref.getString("Map_Filter", "default");

        JSONObject filter = null;

        FilteredRegion filteredRegion = null;
        ArrayList<Integer> categories = new ArrayList<>();

        try {
            if(!mapTypeString.equals("default")){
                filter = new JSONObject(mapTypeString);

                try{
                    JSONArray jsonArray = filter.getJSONArray("categories");

                    for (int i = 0; i < jsonArray.length(); i++){
                        categories.add(Integer.parseInt(jsonArray.get(i).toString()));
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                try{
                    JSONObject region = filter.getJSONObject("region");
                    filteredRegion = new FilteredRegion(new LatLng(region.getDouble("latitude"), region.getDouble("longitude"))
                            ,region.getString("city"));
                }catch(Exception e){
                 //   e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new FilterSettings(filteredRegion, categories);
    }

}
