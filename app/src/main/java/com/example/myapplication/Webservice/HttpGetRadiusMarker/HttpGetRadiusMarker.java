package com.example.myapplication.Webservice.HttpGetRadiusMarker;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.myapplication.Interfaces.CustomMarkerListener.CustomMarkerListener;
import com.example.myapplication.R;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

public class HttpGetRadiusMarker extends AsyncTask<String , Void ,String> {

    HttpsURLConnection urlConnection;
    URL url;
    int responseCode;
    Context context;

    String userId;
    CustomMarkerListener customMarkerListener;

    public HttpGetRadiusMarker(Context context, int userId, CustomMarkerListener customMarkerListener) {
        this.context = context;
        this.userId = String.valueOf(userId);
        this.customMarkerListener = customMarkerListener;
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

    @Override
    protected void onPostExecute(String json) {
        try {
            Log.d("Print", "Response = " + json);

            if(json.length() > 0){
                JSONObject jsonObject = new JSONObject(json);
                double lat = jsonObject.getDouble("lat");
                double lon = jsonObject.getDouble("lon");
                double radius = jsonObject.getDouble("radius");
                boolean inApp = jsonObject.getBoolean("inApp");
                boolean voice = jsonObject.getBoolean("voice");

                customMarkerListener.renderRadiusMarker(lat, lon, radius, inApp, voice);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    String createApiQuery(){
        return MessageFormat.format(this.context.getResources().getString(R.string.webservice_get_radius_marker), this.userId);
    }
}
