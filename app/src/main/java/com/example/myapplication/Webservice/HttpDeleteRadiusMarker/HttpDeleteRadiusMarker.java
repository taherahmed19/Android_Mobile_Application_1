package com.example.myapplication.Webservice.HttpDeleteRadiusMarker;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.myapplication.Interfaces.DeleteRadiusMarkerListener.DeleteRadiusMarkerListener;
import com.example.myapplication.Interfaces.TokenExpirationListener.TokenExpirationListener;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.JWTToken.JWTToken;
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
    TokenExpirationListener tokenExpirationListener;

    String userId;

    public HttpDeleteRadiusMarker(Context context, int userId, DeleteRadiusMarkerListener deleteRadiusMarkerListener, TokenExpirationListener tokenExpirationListener) {
        this.context = context;
        this.userId = String.valueOf(userId);
        this.deleteRadiusMarkerListener = deleteRadiusMarkerListener;
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
        if (response != null && response.length() > 0) {
            if (response.equals("401")) {
                tokenExpirationListener.handleTokenExpiration();
            } else {
                boolean valid = Boolean.parseBoolean(response);
                deleteRadiusMarkerListener.handleRadiusMarkerRemoval(valid);
            }
        } else {
            Toast.makeText(context, "Error, Try again later", Toast.LENGTH_LONG).show();
        }
    }

    String createApiQuery(){
        return MessageFormat.format(this.context.getResources().getString(R.string.webservice_delete_radius_marker), this.userId);
    }

}
