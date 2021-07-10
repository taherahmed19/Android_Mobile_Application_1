package com.example.myapplication.Webservice.HttpRatings;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.myapplication.Interfaces.RatingsListener.RatingsListener;
import com.example.myapplication.Interfaces.TokenExpirationListener.TokenExpirationListener;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.JWTToken.JWTToken;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

public class HttpRatings extends AsyncTask<String , Void ,String> {

    HttpsURLConnection urlConnection;
    URL url;
    int responseCode;
    Context context;
    String markerId;
    boolean isUpVote;

    RatingsListener ratingsListener;
    TokenExpirationListener tokenExpirationListener;

    public HttpRatings(Context context, int markerId, boolean isUpVote, RatingsListener ratingsListener, TokenExpirationListener tokenExpirationListener){
        this.context = context;
        this.markerId = String.valueOf(markerId);
        this.isUpVote = isUpVote;
        this.ratingsListener = ratingsListener;
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
        if(response != null && response.length() > 0){
            if (response.equals("401")) {
                tokenExpirationListener.handleTokenExpiration();
            } else {
                boolean ratingUpdated = Boolean.parseBoolean(response);
                ratingsListener.updateModalRating(ratingUpdated);
            }
        }else{
            Toast.makeText(context, "Error, Try again later", Toast.LENGTH_LONG).show();
        }

    }

    String createApiQuery(){
        return MessageFormat.format(this.context.getResources().getString(R.string.webservice_update_marker_rating), this.markerId, this.isUpVote);
    }
}
