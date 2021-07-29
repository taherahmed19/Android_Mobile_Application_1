package com.example.myapplication.Webservice.HttpFirebaseToken;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpFirebaseToken extends AsyncTask<String , Void ,String> {

    HttpsURLConnection urlConnection;
    URL url;
    int responseCode;
    Context context;

    int userId;
    String firebaseToken;

    /**
     * Constructor to init new firebase token for user
     * @param context application context
     * @param userId user id - db
     * @param firebaseToken unique fb token
     */
    public HttpFirebaseToken(Context context, int userId, String firebaseToken){
        this.context = context;
        this.userId = userId;
        this.firebaseToken = firebaseToken;
    }

    /**
     * Background method for async class - not working off main thread
     * @param strings
     * @return
     */
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

    /**
     * make request to the web service
     * @param request
     * @return
     */
    String handleRequest(String request){
        String response = null;
        try {
            url = new URL(request);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setHostnameVerifier(SSL.DUMMY_VERIFIER);

            Log.d("Print", "Body request " + this.userId + " " + this.firebaseToken);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", this.userId);
            jsonObject.put("token", this.firebaseToken);

            BufferedOutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();
            out.close();

            urlConnection.connect();

            response = handleResponse();
        }catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }

    /**
     * code to handle response from the web service
     * @return
     */
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

    /**
     * handle response data
     * Toast to UI no fragment handling made
     * No 401 handling needed at this stage - user not logged in during this request
     * @param response
     */
    @Override
    protected void onPostExecute(String response) {
        if(response != null && response.length() > 0){
            boolean valid = Boolean.parseBoolean(response);

            if(!valid){
                Toast.makeText(this.context, "Unable to connect to application. Retry later.", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(context, "Error, Try again later?", Toast.LENGTH_LONG).show();
        }
    }

    String createApiQuery(){
        return this.context.getResources().getString(R.string.webservice_save_firebase_token);
    }

}
