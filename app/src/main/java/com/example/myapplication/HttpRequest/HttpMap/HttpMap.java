package com.example.myapplication.HttpRequest.HttpMap;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.FeedFragment.FeedFragment;
import com.example.myapplication.Interfaces.CustomMarkerListener.CustomMarkerListener;
import com.example.myapplication.JsonBuilders.MapJsonBuilder.MapJsonBuilder;
import com.example.myapplication.Models.Settings.Settings;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.LoadingSpinner.LoadingSpinner;
import com.example.myapplication.Handlers.MapHandler.MapHandler;
import com.google.android.gms.maps.SupportMapFragment;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class HttpMap extends AsyncTask<String , Void ,String> {

    @SuppressLint("StaticFieldLeak")
    FragmentActivity fragmentActivity;
    FragmentManager fragmentManager;

    SupportMapFragment supportMapFragment;

    HttpsURLConnection urlConnection;
    URL url;
    int responseCode;

    ArrayList<Marker> markers;

    LoadingSpinner loadingSpinner;
    Fragment viewPagerFragment;
    Settings settings;

    MapHandler mapHandler;
    MapJsonBuilder mapJsonBuilder;

    CustomMarkerListener customMarkerListener;

    public HttpMap(FragmentActivity fragmentActivity, FragmentManager fragmentManager, SupportMapFragment supportMapFragment,
                   Fragment viewPagerFragment, LoadingSpinner loadingSpinner, Settings settings, CustomMarkerListener customMarkerListener){
        this.fragmentActivity = fragmentActivity;
        this.markers = new ArrayList<>();
        this.supportMapFragment = supportMapFragment;
        this.viewPagerFragment = viewPagerFragment;
        this.loadingSpinner = loadingSpinner;
        this.settings = settings;
        this.mapJsonBuilder = new MapJsonBuilder(this.markers, settings);
        this.responseCode = 0;
        this.customMarkerListener = customMarkerListener;
    }

    public HttpMap(FragmentActivity fragmentActivity, FragmentManager fragmentManager, Fragment viewPagerFragment, LoadingSpinner loadingSpinner){
        this.fragmentActivity = fragmentActivity;
        this.markers = new ArrayList<>();
        this.viewPagerFragment = viewPagerFragment;
        this.loadingSpinner = loadingSpinner;
        this.mapJsonBuilder = new MapJsonBuilder(this.markers, settings);
    }

    @Override
    protected void onPreExecute() {
        this.loadingSpinner.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        SSL.AllowSSLCertificates();

        String apiRequest = strings[0];
        try {
            String response = handleRequest(apiRequest);

            if(!TextUtils.isEmpty(response)){
                this.mapJsonBuilder.parseJson(response);
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
            urlConnection.setHostnameVerifier(SSL.DUMMY_VERIFIER);

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
    protected void onPostExecute(String str) {
        this.loadingSpinner.hide();
        ArrayList<Marker> markers = this.mapJsonBuilder.getMarkers();

        if(this.viewPagerFragment instanceof FeedFragment){
            FeedFragment feedFragment = (FeedFragment) this.viewPagerFragment;
            feedFragment.renderFeed(markers);

        }else{
            if(this.markers != null){
                customMarkerListener.addMarkerData(markers);
            }
        }
    }

//    //refactor
    public void saveMapCameraPosition(){
        if(mapHandler != null){
            mapHandler.saveMapCameraPosition();
        }
    }

    //refactor
    public MapHandler getDatabaseMarkerMap() {
        return mapHandler;
    }

    String createApiQuery(String input){
        return "";
    }
}
