package com.example.myapplication.HttpRequest.HttpFeed;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.myapplication.Interfaces.FeedSubmitListener.FeedSubmitListener;
import com.example.myapplication.R;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

public class HttpFeed extends AsyncTask<String , Void ,String> {

    HttpsURLConnection urlConnection;
    URL url;
    int responseCode;
    String server_response;
    Context context;

    int userId;
    int category;
    String description;
    LatLng chosenLocation;
    String encodedImage;

    FeedSubmitListener feedSubmitListener;

    public HttpFeed(Context context, int userId, int category, String description, LatLng chosenLocation, FeedSubmitListener feedSubmitListener, String encodedImage){
        this.server_response = "";
        this.context = context;
        this.userId = userId;
        this.category = category;
        this.description = description;
        this.chosenLocation = chosenLocation;
        this.feedSubmitListener = feedSubmitListener;
        this.encodedImage = encodedImage;
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

        return server_response;
    }

    @Override
    protected void onPostExecute(String responseString) {
        handleJSONResponse(responseString);
    }

    String handleRequest(String apiRequest){
        String response = null;
        try {
            url = new URL(apiRequest);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setHostnameVerifier(SSL.DUMMY_VERIFIER);

            BufferedOutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(this.encodedImage);
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
            Log.d("Print", "Response += " + jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            boolean validPost = jsonObject.getBoolean("validPost");

            feedSubmitListener.handleSubmitStatusMessage(validPost);

            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String createApiQuery(){
        return "";
//        return MessageFormat.format(this.context.getResources().getString(R.string.webservice_insert_post_endpoint),
//                this.userId, this.category, this.description, String.valueOf(this.chosenLocation.latitude), String.valueOf(this.chosenLocation.longitude));
    }
}
