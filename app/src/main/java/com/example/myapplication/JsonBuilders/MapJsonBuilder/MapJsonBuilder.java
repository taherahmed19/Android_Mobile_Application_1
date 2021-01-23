package com.example.myapplication.JsonBuilders.MapJsonBuilder;

import android.location.Location;

import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.Settings.Settings;
import com.example.myapplication.Utils.JsonUtil.JsonUtil;
import com.example.myapplication.Utils.Tools.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapJsonBuilder {
    ArrayList<Marker> markers;
    Settings settings;

    public MapJsonBuilder(ArrayList<Marker> markers, Settings settings) {
        this.markers = markers;
        this.settings = settings;
    }

    public void parseJson(String json){
        if(this.markers != null && this.markers.size() > 0){
            this.markers.removeAll(this.markers);
        }

        try {
            JSONArray jsonArr = new JSONArray(json);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", jsonArr);

            JSONArray data = jsonObject.getJSONArray("data");

            for(int i = 0; i < data.length(); i++){
                if(settings != null){
                    ArrayList<String> selectedItems = settings.getFilterSortBy().getSelectedItems();
                    for (String spinnerItem : selectedItems) {
                        if(Tools.CategoryMarkerSwitch(spinnerItem) == data.getJSONObject(i).getInt("Marker")){
                            addMarker(i, data);
                        }
                    }

                    if(settings.getFilterSortBy().getSelectedItems().size() == 0){
                        addMarker(i, data);
                    }

                }else{
                    addMarker(i, data);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void addMarker(int i, JSONArray data){
        try{
            int id = JsonUtil.getInt(data.getJSONObject(i), "Id");
            int marker = JsonUtil.getInt(data.getJSONObject(i), "Marker");
            String description = JsonUtil.getString(data.getJSONObject(i), "Description");
            String lat = JsonUtil.getString(data.getJSONObject(i), "Lat");
            String lng = JsonUtil.getString(data.getJSONObject(i), "Lng");
            String image = JsonUtil.getString(data.getJSONObject(i), "Image");
            int rating = JsonUtil.getInt(data.getJSONObject(i), "Rating");

            markers.add(new Marker(marker, description, lat, lng, image, id, rating));

            Location location = new Location("");
            location.setLatitude(Double.parseDouble(data.getJSONObject(i).getString("Lat")));
            location.setLongitude(Double.parseDouble(data.getJSONObject(i).getString("Lng")));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Marker> getMarkers() {
        return markers;
    }
}
