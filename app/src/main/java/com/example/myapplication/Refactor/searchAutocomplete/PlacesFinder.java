package com.example.myapplication.Refactor.searchAutocomplete;

import android.content.Context;
import android.os.AsyncTask;

import com.example.myapplication.R;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;
import com.example.myapplication.Refactor.RoutingSearchAutocompleteFragment;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

public class PlacesFinder extends AsyncTask<String , Void ,String> {

    static final String QUERY = "https://maps.googleapis.com/maps/api/place/queryautocomplete/json?key={0}&input={1}";

    String server_response;
    Context context;

    Places places;
    PlacesJsonBuilder placesJsonBuilder;
    RoutingSearchAutocompleteFragment routingSearchAutocompleteFragment;

    public PlacesFinder(Context context, RoutingSearchAutocompleteFragment routingSearchAutocompleteFragment) {
        this.context = context;
        this.server_response = "";
        this.placesJsonBuilder = new PlacesJsonBuilder();
        this.routingSearchAutocompleteFragment = routingSearchAutocompleteFragment;
    }

    @Override
    protected String doInBackground(String... strings) {
        SSL.AllowSSLCertificates();
        URL url;
        HttpsURLConnection urlConnection = null;

        String apiRequest = this.createApiQuery(strings[0]);

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
                places = placesJsonBuilder.buildJSON(server_response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String str) {
        for(int i = 0; i < this.places.getPlaces().size(); i++){
            routingSearchAutocompleteFragment.buildAutocompleteSearchItem(this.places.getPlaces().get(i), this.places.getPlaces().get(i).getMainText(),this.places.getPlaces().get(i).getSecondText());
        }
    }

    String createApiQuery(String input){
        return MessageFormat.format(PlacesFinder.QUERY, this.context.getResources().getString(R.string.GOOGLE_API_KEY_2), Tools.encodeString(input));
    }
}
