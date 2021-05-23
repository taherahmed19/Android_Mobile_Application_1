package com.example.myapplication.Webservice.HttpRatings;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.example.myapplication.Interfaces.RatingsListener.RatingsListener;
import com.example.myapplication.R;
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

    public HttpRatings(Context context, int markerId, boolean isUpVote, RatingsListener ratingsListener){
        this.context = context;
        this.markerId = String.valueOf(markerId);
        this.isUpVote = isUpVote;
        this.ratingsListener = ratingsListener;
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
        boolean ratingUpdated = Boolean.parseBoolean(str);
        ratingsListener.updateModalRating(ratingUpdated);
    }

    String createApiQuery(){
        return MessageFormat.format(this.context.getResources().getString(R.string.webservice_update_marker_rating), this.markerId, this.isUpVote);
    }
}
