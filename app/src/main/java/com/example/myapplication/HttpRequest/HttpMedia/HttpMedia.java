package com.example.myapplication.HttpRequest.HttpMedia;

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
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

public class HttpMedia extends AsyncTask<String , Void ,String> {

    HttpsURLConnection urlConnection;
    URL url;
    int responseCode;

    Context context;
    String encodedImage;

    String server_response;
    int userId;
    int category;
    String description;
    LatLng chosenLocation;

    FeedSubmitListener feedSubmitListener;

    public HttpMedia(Context context, String encodedImage) {
        this.context = context;
        this.encodedImage = encodedImage;
    }

    public HttpMedia(Context context, int userId, int category, String description, LatLng chosenLocation, FeedSubmitListener feedSubmitListener, String encodedImage){
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
            jsonObject.put("marker", this.category);
            jsonObject.put("description", this.description);
            jsonObject.put("lat", String.valueOf(this.chosenLocation.latitude));
            jsonObject.put("lng", String.valueOf(this.chosenLocation.longitude));
            jsonObject.put("encodedString", this.encodedImage);

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

    void handleJSONResponse(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            boolean validPost = jsonObject.getBoolean("validPost");

            feedSubmitListener.handleSubmitStatusMessage(validPost);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String responseString) {
        handleJSONResponse(responseString);
    }

    String createApiQuery(){
        return this.context.getResources().getString(R.string.webservice_insert_post_endpoint);
    }
}
