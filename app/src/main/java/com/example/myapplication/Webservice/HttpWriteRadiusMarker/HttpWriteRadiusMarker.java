package com.example.myapplication.Webservice.HttpWriteRadiusMarker;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.myapplication.Interfaces.SetRadiusMarkerListener.SetRadiusMarkerListener;
import com.example.myapplication.R;
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

    int userId;
    int isMonitoring;
    String lat;
    String lon;
    String radius;

    public HttpWriteRadiusMarker(Context context, int userId, int isMonitoring, String lat, String lon, String radius, SetRadiusMarkerListener setRadiusMarkerListener) {
        this.context = context;
        this.userId = userId;
        this.isMonitoring = isMonitoring;
        this.lat = lat;
        this.lon = lon;
        this.radius = radius;
        this.setRadiusMarkerListener = setRadiusMarkerListener;
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

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", this.userId);
            jsonObject.put("isMonitoring", this.isMonitoring);
            jsonObject.put("latitude", this.lat);
            jsonObject.put("longitude", this.lon);
            jsonObject.put("radius", this.radius);

            Log.d("Print", "body - " + jsonObject.toString());

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
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String str) {
        boolean valid = Boolean.parseBoolean(str);
        Log.d("Print", "Radius marker response " + str);
        setRadiusMarkerListener.handleRadiusMarker(valid);
    }

    String createApiQuery(){
        return this.context.getResources().getString(R.string.webservice_write_radius_marker);
    }

}
