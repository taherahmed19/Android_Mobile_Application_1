package com.example.myapplication.Models.Marker;

import android.content.Context;

import com.example.myapplication.Interfaces.MarkerImageListener.MarkerImageListener;
import com.example.myapplication.Interfaces.MarkerListener.MarkerListener;
import com.example.myapplication.Interfaces.TokenExpirationListener.TokenExpirationListener;
import com.example.myapplication.Webservice.HttpMarkerDelete.HttpMarkerDelete;
import com.example.myapplication.Webservice.HttpMarkerImage.HttpMarkerImage;

public class Marker {

    private String firstName;
    private String lastName;
    private String category;
    private String description;
    private String lat;
    private String lng;
    private String encodedImage;
    private int userId;
    private int id;

        public Marker(int userId, String firstName, String lastName, String category, String description,
                  String lat, String lng, String encodedImage, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.category = category;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.encodedImage = encodedImage;
        this.id = id;
        this.userId = userId;
    }

    public void makeApiCallDeleteMarker(Context context, MarkerListener markerListener, TokenExpirationListener tokenExpirationListener){
        HttpMarkerDelete httpMarkerDelete = new HttpMarkerDelete(context, id, markerListener, tokenExpirationListener);
        httpMarkerDelete.execute();
    }

    public void makeApiCallCreateGetMarkerImage(Context context, MarkerImageListener markerImageListener, TokenExpirationListener tokenExpirationListener){
        HttpMarkerImage httpMarkerImage = new HttpMarkerImage(context, id, markerImageListener, tokenExpirationListener);
        httpMarkerImage.execute();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
