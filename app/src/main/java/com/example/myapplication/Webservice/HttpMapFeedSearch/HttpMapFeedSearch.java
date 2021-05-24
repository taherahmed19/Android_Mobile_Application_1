package com.example.myapplication.Webservice.HttpMapFeedSearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.myapplication.Interfaces.SearchListener.SearchListener;
import com.example.myapplication.JsonBuilders.MapFeedSearchJsonBuilder.MapFeedSearchJsonBuilder;
import com.example.myapplication.R;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;
import com.google.android.gms.maps.model.LatLng;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

public class HttpMapFeedSearch extends AsyncTask<String , Void , LatLng> {

    public static final String QUERY = "https://maps.googleapis.com/maps/api/geocode/json?address={0}&key={1}";

    @SuppressLint("StaticFieldLeak")
    Context context;
    SearchListener listener;

    MapFeedSearchJsonBuilder mapFeedSearchJsonBuilder;

    URL url = null;
    HttpsURLConnection urlConnection = null;
    int responseCode = 0;

    boolean validResponse;

    public HttpMapFeedSearch(Context context, SearchListener listener) {
        this.listener = listener;
        this.context = context;
        this.mapFeedSearchJsonBuilder = new MapFeedSearchJsonBuilder();
        this.validResponse = false;
    }

    @Override
    protected LatLng doInBackground(String... strings) {
        SSL.AllowSSLCertificates();
        LatLng locationLatLng = null;

        String apiRequest = this.createApiQuery(strings[0]);

        try {
            String response = handleRequest(apiRequest);

            if(!TextUtils.isEmpty(response)){
                locationLatLng = mapFeedSearchJsonBuilder.jsonHandler(response);

                this.validResponse = locationLatLng != null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return locationLatLng;
    }

    @Override
    protected void onPostExecute(LatLng latLng) {
        if(validResponse){
            this.listener.popFragments();
            this.listener.returnFragmentData(latLng);
        }else{
            Toast.makeText(context, context.getString(R.string.map_feed_search_error_body_2), Toast.LENGTH_LONG).show();
        }
    }

    String handleRequest(String apiRequest){
        String response = null;
        try {
            url = new URL(apiRequest);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setHostnameVerifier(SSL.DUMMY_VERIFIER);

            response = handleResponse();

        }catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }

    String handleResponse(){
        try {
            responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                return Tools.readStream(urlConnection.getInputStream());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    String createApiQuery(String input){
        return MessageFormat.format(HttpMapFeedSearch.QUERY, Tools.encodeString(input), this.context.getResources().getString(R.string.GOOGLE_API_KEY_2));
    }
}