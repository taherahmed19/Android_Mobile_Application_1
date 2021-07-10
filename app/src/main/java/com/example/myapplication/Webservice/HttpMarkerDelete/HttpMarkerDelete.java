package com.example.myapplication.Webservice.HttpMarkerDelete;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.myapplication.Interfaces.MarkerListener.MarkerListener;
import com.example.myapplication.Interfaces.TokenExpirationListener.TokenExpirationListener;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.JWTToken.JWTToken;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

public class HttpMarkerDelete extends AsyncTask<Void , Void ,String> {

    HttpsURLConnection urlConnection;
    URL url;
    int responseCode;

    Context context;
    String markerId;

    MarkerListener markerListener;
    TokenExpirationListener tokenExpirationListener;

    public HttpMarkerDelete(Context context, int markerId, MarkerListener markerListener, TokenExpirationListener tokenExpirationListener){
        this.context = context;
        this.markerId = String.valueOf(markerId);
        this.markerListener = markerListener;
        this.tokenExpirationListener = tokenExpirationListener;
    }

    @Override
    protected String doInBackground(Void... voids) {
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
        if(response != null && response.length() > 0){
            if (response.equals("401")) {
                tokenExpirationListener.handleTokenExpiration();
            } else {
                boolean postDeleted = Boolean.parseBoolean(response);
                markerListener.deleteUserPost(postDeleted);
            }
        }else{
            Toast.makeText(context, "Error, Try again later", Toast.LENGTH_LONG).show();
        }
    }

    String createApiQuery(){
        return MessageFormat.format(this.context.getResources().getString(R.string.webservice_delete_marker), this.markerId);
    }

}
