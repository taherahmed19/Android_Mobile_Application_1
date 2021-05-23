package com.example.myapplication.Webservice.HttpMarkerDelete;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.myapplication.Interfaces.MarkerListener.MarkerListener;
import com.example.myapplication.R;
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

    public HttpMarkerDelete(Context context, int markerId, MarkerListener markerListener){
        this.context = context;
        this.markerId = String.valueOf(markerId);
        this.markerListener = markerListener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        SSL.AllowSSLCertificates();

        String apiRequest = this.createApiQuery();
        Log.d("Print", "Delete marker api request " + apiRequest);

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
        boolean postDeleted = Boolean.parseBoolean(str);
        markerListener.deleteUserPost(postDeleted);
    }

    String createApiQuery(){
        return MessageFormat.format(this.context.getResources().getString(R.string.webservice_delete_marker), this.markerId);
    }

}
