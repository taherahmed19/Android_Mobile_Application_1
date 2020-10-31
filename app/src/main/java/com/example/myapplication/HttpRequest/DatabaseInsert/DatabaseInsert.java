package com.example.myapplication.HttpRequest.DatabaseInsert;

import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DatabaseInsert extends AsyncTask<String , Void ,String> {
    String server_response;

    public DatabaseInsert(){
        this.server_response = "";
    }

    @Override
    protected String doInBackground(String... strings) {
        SSL.AllowSSLCertificates();
        URL url;
        HttpsURLConnection urlConnection = null;

        try {
            url = new URL(strings[0]);

            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setHostnameVerifier(SSL.DUMMY_VERIFIER);

            int responseCode = 0;
            try {
                responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = Tools.readStream(urlConnection.getInputStream());
                    Log.d("print", "Inserted data " + server_response);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return server_response;
    }

    @Override
    protected void onPostExecute(String str) {
    }




}
