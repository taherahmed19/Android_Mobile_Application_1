package com.example.myapplication.Refactor.searchAutocomplete.searchGeolocation;

import android.content.Context;
import android.os.AsyncTask;

import com.example.myapplication.R;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;
import com.example.myapplication.Refactor.RoutingFragment;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

public class SearchGeolocation extends AsyncTask<String , Void ,String> {

    public static final String QUERY = "https://maps.googleapis.com/maps/api/geocode/json?address={0}&key={1}";

    String server_response;
    Context context;

    SearchJsonBuilder searchJsonBuilder;
    RoutingFragment routingFragment;
    Route route;

    public SearchGeolocation(Context context, RoutingFragment routingFragment) {
        this.context = context;
        this.server_response = "";
        this.searchJsonBuilder = new SearchJsonBuilder();
        this.routingFragment = routingFragment;
        this.route = null;
    }

    @Override
    protected String doInBackground(String... strings) {
        SSL.AllowSSLCertificates();
        URL url = null;
        HttpsURLConnection urlConnection = null;

        String apiRequestFrom = this.createApiQuery(strings[0]);
        String apiRequestWhere = this.createApiQuery(strings[1]);

        String sourceLocationResponse = this.request(apiRequestFrom, url, urlConnection);
        String destinationLocationResponse = this.request(apiRequestWhere, url, urlConnection);

        this.route = searchJsonBuilder.buildJSON(sourceLocationResponse, destinationLocationResponse);

        return "";
    }

    @Override
    protected void onPostExecute(String str) {
        routingFragment.getRoutingMap().drawRoute(this.route.source.getLatLng(), this.route.destination.getLatLng());
        routingFragment.getRoutingMap().setMapFocus(this.route.source.getLatLng());
    }

    String request(String apiRequest, URL url, HttpsURLConnection urlConnection){
        String server_response = null;

        try {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return server_response;
    }

    String createApiQuery(String input){
        return MessageFormat.format(SearchGeolocation.QUERY, Tools.encodeString(input), this.context.getResources().getString(R.string.GOOGLE_API_KEY_2));
    }
}
