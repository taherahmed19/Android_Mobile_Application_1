package com.example.myapplication.Models.Modal;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.myapplication.Interfaces.MarkerModalContract.MarkerModalContract;
import com.example.myapplication.Interfaces.TokenExpirationListener.TokenExpirationListener;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Presenters.MarkerModalPresenter.MarkerModalPresenter;

public class Modal implements MarkerModalContract.Model {

    String firstName;
    String lastName;
    String altName;
    String userPost;
    String encodedImage;
    Bitmap image;
    MarkerModalPresenter markerModalPresenter;
    TokenExpirationListener tokenExpirationListener;

    public Modal(String encodedImage, String firstName, String lastName, MarkerModalPresenter markerModalPresenter, TokenExpirationListener tokenExpirationListener) {
        this.markerModalPresenter = markerModalPresenter;
        this.firstName = firstName;
        this.lastName = lastName;
        this.encodedImage = encodedImage;
        this.altName = "";
        this.userPost = "";
        this.image = null;
        this.tokenExpirationListener = tokenExpirationListener;
    }

    public void decodeImage() {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public void handleModalName() {
        String altFirstName = this.firstName;
        String altLastName = this.lastName;

        this.altName = altFirstName.substring(0, 1).toUpperCase() + altFirstName.substring(1) + " " + altLastName.substring(0, 1).toUpperCase() + altLastName.substring(1);
        this.userPost = "Your post";
    }

    public String getAltName() {
        return altName;
    }

    public String getUserPost() {
        return userPost;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    public String getEncodedImage() {
        return encodedImage;
    }
}
