package com.example.myapplication.Refactor.searchAutocomplete.poiLocator;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;
import com.example.myapplication.Refactor.RoutingFragment;
import com.example.myapplication.Refactor.searchAutocomplete.searchGeolocation.Route;
import com.google.android.gms.maps.model.LatLng;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

public class POIFinder extends AsyncTask<String , Void ,String> {

    static final String QUERY = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location={0},{1}&rankby=distance&type={2}&key={3}";

    String server_response;
    Context context;
    LatLng latLng;

    POIJsonBuilder poiJsonBuilder;
    Route route;
    RoutingFragment routingFragment;

    public POIFinder(Context context, LatLng latLng, RoutingFragment routingFragment) {
        this.context = context;
        this.latLng = latLng;
        this.server_response = "";
        this.poiJsonBuilder = new POIJsonBuilder();
        this.route = null;
        this.routingFragment = routingFragment;
    }

    @Override
    protected String doInBackground(String... strings) {
        SSL.AllowSSLCertificates();
        URL url;
        HttpsURLConnection urlConnection = null;

        String apiRequest = this.createApiQuery(strings[0], String.valueOf(this.latLng.latitude), String.valueOf(this.latLng.longitude));

        try {
            Log.d("Print", "Api request " + apiRequest);
            url = new URL(apiRequest);

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
                this.route = poiJsonBuilder.buildJSON(server_response, new LatLng(51.509865, -0.118092));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String str) {
        if(this.route != null){
            Log.d("Print", "Post execute lat lng " + this.route.getSource().getLatLng() + " " + this.route.getDestination().getLatLng());
            routingFragment.getRoutingMap().drawRoute(this.route.getSource().getLatLng(), this.route.getDestination().getLatLng());
        }
    }

    String createApiQuery(String input, String lat, String lon){
        return MessageFormat.format(POIFinder.QUERY, lat, lon,Tools.encodeString(input), this.context.getResources().getString(R.string.GOOGLE_API_KEY_2));
    }
}
