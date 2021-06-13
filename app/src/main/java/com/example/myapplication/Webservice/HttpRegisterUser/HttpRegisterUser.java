package com.example.myapplication.Webservice.HttpRegisterUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.Interfaces.RegisterListener.RegisterListener;
import com.example.myapplication.Models.RegisterUser.RegisterUser;
import com.example.myapplication.Models.User.User;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.JWTToken.JWTToken;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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
            urlConnection.setRequestMethod("POST");
            urlConnection.setHostnameVerifier(SSL.DUMMY_VERIFIER);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("firstName", this.registerUser.getFirstName());
            jsonObject.put("lastName", this.registerUser.getLastName());
            jsonObject.put("email", this.registerUser.getEmail());
            jsonObject.put("password", this.registerUser.getPassword());

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
    protected void onPostExecute(String response) {
        if(response != null && response.length() > 0){
            handleJSONResponse(response);
            registerListener.handleRegistrationAttempt(validCredentials, user);
        }else{
            Toast.makeText(context, "Error, Try again later", Toast.LENGTH_LONG);
        }
    }

    void handleJSONResponse(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            validCredentials = jsonObject.getBoolean("validCredentials");
            user.setId(jsonObject.getInt("userId"));
            user.setFirstName(jsonObject.getString("userFirstName"));
            user.setLastName(jsonObject.getString("userLastName"));
            user.setEmail(jsonObject.getString("userEmail"));
            user.setToken(jsonObject.getString("token"));

            JWTToken.saveToken(context, user.getToken());

            Log.d("Print", "Token = " + user.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String createApiQuery(){
        return this.context.getResources().getString(R.string.webservice_register_endpoint);
    }
}
