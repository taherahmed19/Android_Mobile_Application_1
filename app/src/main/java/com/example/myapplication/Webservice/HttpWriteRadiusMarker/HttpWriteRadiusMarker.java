package com.example.myapplication.Webservice.HttpWriteRadiusMarker;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.myapplication.Interfaces.SetRadiusMarkerListener.SetRadiusMarkerListener;
import com.example.myapplication.Interfaces.TokenExpirationListener.TokenExpirationListener;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.JWTToken.JWTToken;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpWriteRadiusMarker extends AsyncTask<String , Void ,String> {

    HttpsURLConnection urlConnection;
    URL url;
    int responseCode;
    Context context;

    SetRadiusMarkerListener setRadiusMarkerListener;
    TokenExpirationListener tokenExpirationListener;

    int userId;
    int isMonitoring;
    String lat;
    String lon;
    String radius;
    int inApp;
    int voice;

    public HttpWriteRadiusMarker(Context context, int userId, int isMonitoring, String lat, String lon, String radius,
                                 int inApp, int voice, SetRadiusMarkerListener setRadiusMarkerListener,
                                 TokenExpirationListener tokenExpirationListener) {
        this.context = context;
        this.userId = userId;
        this.isMonitoring = isMonitoring;
        this.lat = lat;
        this.lon = lon;
        this.radius = radius;
        this.inApp = inApp;
        this.voice = voice;
        this.setRadiusMarkerListener = setRadiusMarkerListener;
        this.tokenExpirationListener = tokenExpirationListener;
    }

    @Override
    protected String doInBackground(String... strings) {
        SSL.AllowSSLCertificates();

        String apiRequest = this.createApiQuery();

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
            urlConnection.setRequestMethod("POST");
            urlConnection.setHostnameVerifier(SSL.DUMMY_VERIFIER);

            String basicAuth = "Bearer " + JWTToken.getToken(context);
            urlConnection.setRequestProperty("Authorization", basicAuth);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", this.userId);
            jsonObject.put("isMonitoring", this.isMonitoring);
            jsonObject.put("latitude", this.lat);
            jsonObject.put("longitude", this.lon);
            jsonObject.put("radius", this.radius);
            jsonObject.put("inApp", this.inApp);
            jsonObject.put("voice", this.voice);


            BufferedOutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();
            out.close();

            urlConnection.connect();

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
            }else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                return String.valueOf(responseCode);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        if(response != null && response.length() > 0){
            if (response.equals("401")) {
                tokenExpirationListener.handleTokenExpiration();
            } else {
                boolean valid = Boolean.parseBoolean(response);
                setRadiusMarkerListener.handleRadiusMarker(valid);
            }
        }else{
            Toast.makeText(context, "Error, Try again later", Toast.LENGTH_LONG);
        }
    }

    String createApiQuery(){
        return this.context.getResources().getString(R.string.webservice_write_radius_marker);
    }

}
