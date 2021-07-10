package com.example.myapplication.Webservice.HttpMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.myapplication.Interfaces.CustomMarkerListener.CustomMarkerListener;
import com.example.myapplication.Interfaces.TokenExpirationListener.TokenExpirationListener;
import com.example.myapplication.JsonBuilders.MapJsonBuilder.MapJsonBuilder;
import com.example.myapplication.Models.LoadingSpinner.LoadingSpinner;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.JWTToken.JWTToken;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class HttpMap extends AsyncTask<String , Void ,String> {

    @SuppressLint("StaticFieldLeak")
    Context context;
    HttpsURLConnection urlConnection;
    URL url;
    int responseCode;

    ArrayList<Marker> markers;
    LoadingSpinner loadingSpinner;
    MapJsonBuilder mapJsonBuilder;

    CustomMarkerListener customMarkerListener;
    TokenExpirationListener tokenExpirationListener;

    public HttpMap(LoadingSpinner loadingSpinner, Context context, CustomMarkerListener customMarkerListener, TokenExpirationListener tokenExpirationListener){
        this.customMarkerListener = customMarkerListener;
        this.context = context;
        this.markers = new ArrayList<>();
        this.loadingSpinner = loadingSpinner;
        this.mapJsonBuilder = new MapJsonBuilder(this.markers);
        this.tokenExpirationListener = tokenExpirationListener;
    }

    @Override
    protected void onPreExecute() {
        this.loadingSpinner.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        SSL.AllowSSLCertificates();

        String apiRequest = createApiQuery();

        try {
            String response = handleRequest(apiRequest);

            if(!TextUtils.isEmpty(response)){
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    String handleRequest(String apiRequest){
        String response = null;
        try {
            url = new URL(apiRequest);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setHostnameVerifier(SSL.DUMMY_VERIFIER);

            String basicAuth = "Bearer " + JWTToken.getToken(context);
            urlConnection.setRequestProperty("Authorization", basicAuth);

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
            }else if(responseCode == HttpURLConnection.HTTP_UNAUTHORIZED){
                return String.valueOf(responseCode);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null && response.length() > 0) {
            if(response.equals("401")){
                tokenExpirationListener.handleTokenExpiration();
            }else{
                this.mapJsonBuilder.parseJson(response);
                this.loadingSpinner.hide();
                ArrayList<Marker> markers = this.mapJsonBuilder.getMarkers();
                if (this.markers != null) {
                    customMarkerListener.addMarkerData(markers);
                    customMarkerListener.detectRadiusMarker();
                }
            }
        } else {
            Toast.makeText(context, "Error, Try again late?", Toast.LENGTH_LONG).show();
        }
    }

    String createApiQuery(){
        return this.context.getResources().getString(R.string.webservice_markers);
    }
}
