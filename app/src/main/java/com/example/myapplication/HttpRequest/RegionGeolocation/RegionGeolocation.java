package com.example.myapplication.HttpRequest.RegionGeolocation;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.Activities.RegionSelectorActivity.RegionSelectorActivity;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RegionGeolocation extends AsyncTask<String , Void ,String> {

    Context context;
    LatLng region;
    RegionSelectorActivity regionSelectorActivity;
    String regionName;

    public RegionGeolocation(Context context, RegionSelectorActivity regionSelectorActivity){
        this.context = context;
        this.region = null;
        this.regionSelectorActivity = regionSelectorActivity;
        this.regionName = "";
    }

    @Override
    protected String doInBackground(String... strings) {
        SSL.AllowSSLCertificates();
        URL url;
        HttpsURLConnection urlConnection = null;
        String server_response = "";

        try {
            url = new URL(strings[0]);

            try {
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setHostnameVerifier(SSL.DUMMY_VERIFIER);

            }catch (Exception e){
                e.printStackTrace();
            }

            int responseCode = 0;
            try {
                responseCode = urlConnection.getResponseCode();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(responseCode == HttpURLConnection.HTTP_OK){
                server_response = Tools.readStream(urlConnection.getInputStream());

                buildGeocoordinates(server_response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String str) {
        regionSelectorActivity.configureSubmitButtonRegionSelected(this.region, this.regionName);
    }

    void buildGeocoordinates(String response){

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject firstResult = (JSONObject) jsonObject.getJSONArray("results").get(0);
            JSONObject geoCoordinates = firstResult.getJSONObject("geometry");
            double lat = geoCoordinates.getDouble("lat");
            double lng = geoCoordinates.getDouble("lng");

            region = new LatLng(lat, lng);

            JSONObject geoFullLocation = firstResult.getJSONObject("components");
            String city = geoFullLocation.getString("city");

            regionName = city;

        }catch (JSONException err){
            Log.d("Error", err.toString());
        }
    }

    public LatLng getRegion() {
        return region;
    }

    public void setRegion(LatLng region) {
        this.region = region;
    }
}
