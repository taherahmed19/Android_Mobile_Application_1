package com.example.myapplication.Webservice.HttpMarker;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.myapplication.Interfaces.FeedSubmitListener.FeedSubmitListener;
import com.example.myapplication.R;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpMarker extends AsyncTask<String , Void ,String> {

    HttpsURLConnection urlConnection;
    URL url;
    int responseCode;

    Context context;
    String encodedImage;

    String server_response;
    int userId;
    String category;
    String description;
    LatLng chosenLocation;

    FeedSubmitListener feedSubmitListener;

    public HttpMarker(Context context, int userId, String category, String description, LatLng chosenLocation, FeedSubmitListener feedSubmitListener, String encodedImage){
        this.server_response = "";
        this.context = context;
        this.userId = userId;
        this.category = category;
        this.description = description;
        this.chosenLocation = chosenLocation;
        this.encodedImage = encodedImage;
        this.feedSubmitListener = feedSubmitListener;
    }

    @Override
    protected String doInBackground(String... strings) {
        SSL.AllowSSLCertificates();

        String apiRequest = this.createApiQuery();
        Log.d("Print2", apiRequest);
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
            jsonObject.put("category", this.category);
            jsonObject.put("description", this.description);
            jsonObject.put("lat", String.valueOf(this.chosenLocation.latitude));
            jsonObject.put("lng", String.valueOf(this.chosenLocation.longitude));
            jsonObject.put("encodedString", this.encodedImage);

            Log.d("Print", "body " + jsonObject.toString());

            BufferedOutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();
            out.close();

            urlConnection.connect();
            responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                response =  Tools.readStream(urlConnection.getInputStream());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        Log.d("Print", "response? " + response);
        boolean valid = Boolean.parseBoolean(response);
        feedSubmitListener.handleSubmitStatusMessage(valid);
    }

    String createApiQuery(){
        return this.context.getResources().getString(R.string.webservice_insert_post_endpoint);
    }
}
