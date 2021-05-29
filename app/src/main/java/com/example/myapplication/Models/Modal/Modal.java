package com.example.myapplication.Models.Modal;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.example.myapplication.Interfaces.MarkerModalContract.MarkerModalContract;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Presenters.MarkerModalPresenter.MarkerModalPresenter;
import com.example.myapplication.Webservice.HttpRatings.HttpRatings;

public class Modal implements MarkerModalContract.Model {

    String firstName;
    String lastName;
    String altName;
    String userPost;
    String encodedImage;
    Bitmap image;
    int originalRating;
    int rating;
    int ratingTemp;
    boolean isUpVoteClicked;
    boolean isDownVoteClicked;

    MarkerModalPresenter markerModalPresenter;

    public Modal(String encodedImage, String firstName, String lastName, MarkerModalPresenter markerModalPresenter) {
        this.markerModalPresenter = markerModalPresenter;
        this.firstName = firstName;
        this.lastName = lastName;
        this.encodedImage = encodedImage;
        this.altName = "";
        this.userPost = "";
        this.image = null;
        originalRating = 0;
        rating = 0;
        ratingTemp = 0;
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

    public void saveSettingsSharedPreference(Context context, Marker marker) {
        SharedPreferences settingsPreference = context.getSharedPreferences("Marker_Modal_Fragment_Ratings", 0);
        SharedPreferences.Editor preferenceEditor = settingsPreference.edit();

        if (!isDownVoteClicked && !isUpVoteClicked) {
            preferenceEditor.putString(String.valueOf(marker.getId() + "isUpvoteClicked"), "");
        } else {
            preferenceEditor.putString(String.valueOf(marker.getId() + "isUpvoteClicked"), Boolean.toString(isUpVoteClicked));
        }

        preferenceEditor.putInt(String.valueOf(marker.getId() + "rating"), rating);
        preferenceEditor.apply();
    }

    public void isUpVoteClicked() {
        if (isUpVoteClicked) {
            isUpVoteClicked = false;
        } else {
            isUpVoteClicked = true;
        }
        isDownVoteClicked = false;
    }

    public void isDownVoteClicked() {
        if (isDownVoteClicked) {
            isDownVoteClicked = false;
        } else {
            isDownVoteClicked = true;
        }
        isUpVoteClicked = false;
    }

    public void makeApiCall(Context context, Marker marker) {
        HttpRatings httpRatings = new HttpRatings(context, marker.getId(), isUpVoteClicked, markerModalPresenter);
        httpRatings.execute("");
    }

    public void incrementRating() {
        ratingTemp = originalRating + 1;
    }

    public void decrementRating() {
        ratingTemp = originalRating - 1;
    }

    public void setRatingToOriginalValue() {
        ratingTemp = originalRating;
    }

    public void configureUserRatingState(Context context, Marker marker) {
        boolean isUpVoteChosen;
        String isUpVoteChosenPrefStr;
        SharedPreferences settingsPreference = context.getSharedPreferences("Marker_Modal_Fragment_Ratings", 0);

        isUpVoteChosenPrefStr = settingsPreference.getString(String.valueOf(marker.getId() + "isUpvoteClicked"), "");

        if (!TextUtils.isEmpty(isUpVoteChosenPrefStr)) {
            isUpVoteChosen = Boolean.parseBoolean(isUpVoteChosenPrefStr);

            if (isUpVoteChosen) {
                this.markerModalPresenter.setUpVote();
                this.isUpVoteClicked = true;
                this.isDownVoteClicked = false;
            } else {
                this.markerModalPresenter.setDownVote();
                this.isDownVoteClicked = true;
                this.isUpVoteClicked = false;
            }
        } else {
            this.markerModalPresenter.removeVote();
            this.isUpVoteClicked = false;
            this.isDownVoteClicked = false;
        }

        int rating = settingsPreference.getInt(String.valueOf(marker.getId() + "rating"), -10);

        if (rating != -10) {
            this.rating = rating;
            this.ratingTemp = rating;
            this.markerModalPresenter.saveModalRatingState(rating);
        }
    }

    public void handleUpVoteButtonClick() {
        if (!getUpVoteClicked()) {
            isUpVoteClicked();
            this.markerModalPresenter.setUpVote();

            incrementRating();
            this.markerModalPresenter.submitMarkerRating();
        } else {
            isUpVoteClicked();
            this.markerModalPresenter.removeVote();

            setRatingToOriginalValue();
            this.markerModalPresenter.submitMarkerRating();
        }
    }

    public void handleDownVoteButtonClick() {
        if (!getDownVoteClicked()) {
            isDownVoteClicked();
            this.markerModalPresenter.setDownVote();

            decrementRating();
            this.markerModalPresenter.submitMarkerRating();
        } else {
            isDownVoteClicked();
            this.markerModalPresenter.removeVote();

            setRatingToOriginalValue();
            this.markerModalPresenter.submitMarkerRating();
        }
    }

    public String getAltName() {
        return altName;
    }

    public String getUserPost() {
        return userPost;
    }

    public void updateRating() {
        this.rating = ratingTemp;
    }

    public int getRating() {
        return rating;
    }

    public boolean getUpVoteClicked() {
        return isUpVoteClicked;
    }

    public boolean getDownVoteClicked() {
        return isDownVoteClicked;
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
