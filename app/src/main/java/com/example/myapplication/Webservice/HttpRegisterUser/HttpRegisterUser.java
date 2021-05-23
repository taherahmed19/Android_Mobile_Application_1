package com.example.myapplication.Webservice.HttpRegisterUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.example.myapplication.Interfaces.RegisterListener.RegisterListener;
import com.example.myapplication.Models.RegisterUser.RegisterUser;
import com.example.myapplication.Models.User.User;
import com.example.myapplication.R;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

public class HttpRegisterUser extends AsyncTask<String , Void ,String> {

    HttpsURLConnection urlConnection;
    URL url;
    int responseCode;
    @SuppressLint("StaticFieldLeak")
    Context context;
    RegisterListener registerListener;

    boolean validCredentials;
    User user;
    RegisterUser registerUser;

    public HttpRegisterUser(Context context, RegisterListener registerListener, RegisterUser registerUser) {
        this.context = context;
        this.registerListener = registerListener;
        this.registerUser = registerUser;
        this.user = new User();
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

        return null;
    }

    String handleRequest(String apiRequest){
        String response = null;
        try {
            url = new URL(apiRequest);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setHostnameVerifier(SSL.DUMMY_VERIFIER);

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
    protected void onPostExecute(String responseString) {
        handleJSONResponse(responseString);
        registerListener.handleRegistrationAttempt(validCredentials, user);
    }

    void handleJSONResponse(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            validCredentials = jsonObject.getBoolean("validCredentials");
            user.setId(jsonObject.getInt("userId"));
            user.setFirstName(jsonObject.getString("userFirstName"));
            user.setLastName(jsonObject.getString("userLastName"));
            user.setEmail(jsonObject.getString("userEmail"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String createApiQuery(){
        return MessageFormat.format(this.context.getResources().getString(R.string.webservice_register_endpoint),
                this.registerUser.getFirstName(), this.registerUser.getLastName(),
                this.registerUser.getEmail(), this.registerUser.getPassword());
    }
}
