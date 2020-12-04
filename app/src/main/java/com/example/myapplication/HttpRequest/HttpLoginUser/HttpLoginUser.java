package com.example.myapplication.HttpRequest.HttpLoginUser;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.myapplication.Handlers.LoginActivityHandler.LoginActivityHandler;
import com.example.myapplication.Interfaces.LoginListener.LoginListener;
import com.example.myapplication.R;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

public class HttpLoginUser extends AsyncTask<String , Void ,String> {

    HttpsURLConnection urlConnection;
    URL url;
    int responseCode;
    Context context;
    LoginActivityHandler loginActivityHandler;
    LoginListener loginListener;

    public HttpLoginUser(Context context, LoginActivityHandler loginActivityHandler, LoginListener loginListener) {
        this.context = context;
        this.loginActivityHandler = loginActivityHandler;
        this.loginListener = loginListener;
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
        loginListener.handleSignInAttempt(response);
    }

    String createApiQuery(){
        return MessageFormat.format(this.context.getResources().getString(R.string.webservice_login_endpoint),
                this.loginActivityHandler.getEmail(), this.loginActivityHandler.getPassword());
    }
}
