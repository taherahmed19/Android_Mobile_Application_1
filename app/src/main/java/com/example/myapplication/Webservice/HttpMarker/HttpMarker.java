package com.example.myapplication.Webservice.HttpMarker;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.myapplication.Interfaces.FeedSubmitListener.FeedSubmitListener;
import com.example.myapplication.Interfaces.TokenExpirationListener.TokenExpirationListener;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.JWTToken.JWTToken;
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

public class HttpMarker extends AsyncTask<String, Void, String> {

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
    TokenExpirationListener tokenExpirationListener;

    public HttpMarker(Context context, int userId, String category, String description,
                      LatLng chosenLocation, FeedSubmitListener feedSubmitListener, String encodedImage,
                      TokenExpirationListener tokenExpirationListener) {
        this.server_response = "";
        this.context = context;
        this.userId = userId;
        this.category = category;
        this.description = description;
        this.chosenLocation = chosenLocation;
        this.encodedImage = encodedImage;
        this.feedSubmitListener = feedSubmitListener;
        this.tokenExpirationListener = tokenExpirationListener;
    }

    @Override
    protected String doInBackground(String... strings) {
        SSL.AllowSSLCertificates();

        String apiRequest = this.createApiQuery();

        try {
            String response = handleRequest(apiRequest);

            if (!TextUtils.isEmpty(response)) {
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    String handleRequest(String apiRequest) {
        String response = null;
        try {
            url = new URL(apiRequest);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setHostnameVerifier(SSL.DUMMY_VERIFIER);

            String basicAuth = "Bearer " + JWTToken.getToken(context);
            urlConnection.setRequestProperty("Authorization", basicAuth);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", this.userId);
            jsonObject.put("category", this.category);
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
            response = handleResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    String handleResponse() {
        try {
            responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return Tools.readStream(urlConnection.getInputStream());
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                return String.valueOf(responseCode);
            }
        } catch (Exception e) {
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
                feedSubmitListener.handleSubmitStatusMessage(valid);
            }
        } else {
            Toast.makeText(context, "Error, Try again later", Toast.LENGTH_LONG).show();
        }
    }

    String createApiQuery() {
        return this.context.getResources().getString(R.string.webservice_insert_post_endpoint);
    }
}
