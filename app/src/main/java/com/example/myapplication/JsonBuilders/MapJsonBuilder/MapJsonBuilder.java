package com.example.myapplication.JsonBuilders.MapJsonBuilder;

import android.location.Location;
import android.util.Log;

import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Utils.JsonUtil.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapJsonBuilder {
    ArrayList<Marker> markers;

    public MapJsonBuilder(ArrayList<Marker> markers) {
        this.markers = markers;
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
                addMarker(i, data);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void addMarker(int i, JSONArray data){
        try{
            int userId = JsonUtil.getInt(data.getJSONObject(i), "UserId");
            int id = JsonUtil.getInt(data.getJSONObject(i), "Id");
            String firstName = JsonUtil.getString(data.getJSONObject(i), "UserFirstName");
            String lastName = JsonUtil.getString(data.getJSONObject(i), "UserLastName");
            String category = JsonUtil.getString(data.getJSONObject(i), "Category");
            String description = JsonUtil.getString(data.getJSONObject(i), "Description");
            String lat = JsonUtil.getString(data.getJSONObject(i), "Lat");
            String lng = JsonUtil.getString(data.getJSONObject(i), "Lng");
            String encodedImage = JsonUtil.getString(data.getJSONObject(i), "Image");
            int rating = JsonUtil.getInt(data.getJSONObject(i), "Rating");

            Log.d("Print", "Image " + encodedImage);

            markers.add(new Marker(userId, firstName, lastName, category, description, lat, lng, encodedImage, id, rating));

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
