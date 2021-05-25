package com.example.myapplication.Webservice.HttpDeleteRadiusMarker;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.example.myapplication.Models.RadiusMarker.RadiusMarker;
import com.example.myapplication.Interfaces.DeleteRadiusMarkerListener.DeleteRadiusMarkerListener;
import com.example.myapplication.R;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

public class HttpDeleteRadiusMarker extends AsyncTask<String , Void ,String> {

    HttpsURLConnection urlConnection;
    URL url;
    int responseCode;
    Context context;
    DeleteRadiusMarkerListener deleteRadiusMarkerListener;

    RadiusMarker radiusMarker;

    int userId;

    public HttpDeleteRadiusMarker(Context context, int userId, DeleteRadiusMarkerListener deleteRadiusMarkerListener,
                                  RadiusMarker radiusMarker) {
        this.context = context;
        this.userId = userId;
        this.deleteRadiusMarkerListener = deleteRadiusMarkerListener;
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
    protected void onPostExecute(String str) {
        boolean valid = Boolean.parseBoolean(str);
        deleteRadiusMarkerListener.handleRadiusMarkerRemoval(valid);
    }

    String createApiQuery(){
        return MessageFormat.format(this.context.getResources().getString(R.string.webservice_delete_radius_marker), this.userId);
    }

}
