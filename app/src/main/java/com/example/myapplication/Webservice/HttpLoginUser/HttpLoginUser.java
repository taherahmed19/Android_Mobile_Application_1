package com.example.myapplication.Webservice.HttpLoginUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.Interfaces.LoginListener.LoginListener;
import com.example.myapplication.Models.LoginUser.LoginUser;
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

public class HttpLoginUser extends AsyncTask<String , Void ,String> {

    HttpsURLConnection urlConnection;
    URL url;
    int responseCode;
    @SuppressLint("StaticFieldLeak")
    Context context;
    LoginListener loginListener;
    boolean validCredentials;

    User user;
    LoginUser loginUser;

    /**
     * Async constructor for user to login
     * @param context
     * @param loginListener
     * @param loginUser
     */
    public HttpLoginUser(Context context, LoginListener loginListener, LoginUser loginUser) {
        this.context = context;
        this.loginListener = loginListener;
        this.validCredentials = false;
        this.loginUser = loginUser;
        this.user = new User();
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

        return null;
    }

    /**
     * Handle response data
     * Have user enter application if successful otherwise Toast to user
     * @param response
     */
    @Override
    protected void onPostExecute(String response) {
        if(response != null && response.length() > 0){
            handleJSONResponse(response);
            loginListener.handleSignInAttempt(validCredentials, user);
        }else{
            Toast.makeText(context, context.getString(R.string.login_error_body), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * make request to the web service
     * POST data to web service
     * No token needed at this stage
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

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", this.loginUser.getEmail());
            jsonObject.put("password", this.loginUser.getPassword());

            Log.d("Print", "request " + this.loginUser.getEmail() + " " + this.loginUser.getPassword());

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
     * Handle JSON data
     * Save JWTToken to local storage
     * @param jsonString
     */
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String createApiQuery(){
        return this.context.getResources().getString(R.string.webservice_login_endpoint);
    }
}
