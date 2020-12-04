package com.example.myapplication.HttpRequest.HttpRegisterUser;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.example.myapplication.Handlers.RegisterActivityHandler.RegisterActivityHandler;
import com.example.myapplication.Interfaces.RegisterListener.RegisterListener;
import com.example.myapplication.R;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

public class HttpRegisterUser extends AsyncTask<String , Void ,String> {
    HttpsURLConnection urlConnection;
    URL url;
    int responseCode;
    Context context;
    RegisterActivityHandler registerActivityHandler;
    RegisterListener registerListener;

    public HttpRegisterUser(Context context, RegisterActivityHandler registerActivityHandler, RegisterListener registerListener) {
        this.context = context;
        this.registerActivityHandler = registerActivityHandler;
        this.registerListener = registerListener;
    }

    @Override
    protected void onPreExecute() {
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
        boolean response = Boolean.parseBoolean(responseString);
        registerListener.handleRegistrationAttempt(response);
    }

    String createApiQuery(){
        return MessageFormat.format(this.context.getResources().getString(R.string.webservice_register_endpoint),
                this.registerActivityHandler.getFirstName(), this.registerActivityHandler.getLastName(),
                this.registerActivityHandler.getEmail(), this.registerActivityHandler.getPassword());
    }
}
