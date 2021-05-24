package com.example.myapplication.Presenters.MarkerModalPresenter;

import android.app.Dialog;
import android.graphics.Bitmap;

import com.example.myapplication.Interfaces.MarkerImageListener.MarkerImageListener;
import com.example.myapplication.Interfaces.MarkerListener.MarkerListener;
import com.example.myapplication.Interfaces.MarkerModalContract.MarkerModalContract;
import com.example.myapplication.Interfaces.RatingsListener.RatingsListener;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.Modal.Modal;

public class MarkerModalPresenter implements MarkerListener, RatingsListener, MarkerImageListener {

    MarkerModalContract.View view;
    Marker marker;
    Modal modal;

    public MarkerModalPresenter(MarkerModalContract.View view, Marker marker) {
        this.view = view;
        this.marker = marker;
        this.modal = new Modal(marker.getEncodedImage(), marker.getFirstName(), marker.getLastName(), this);
    }

    @Override
    public void deleteUserPost(boolean response) {
        this.view.closeUserPost(response);
    }

    @Override
    public void handleMarkerImage(String encodedString) {
        this.view.addImageToModal(encodedString);
    }

    @Override
    public void updateModalRating(boolean response) {
        this.view.updateModalRating(response);
    }

    @Override
    public void saveModalRatingState(int rating) {
        this.view.saveModalRatingState(rating);
    }

    public void handleCloseButtonClick() {
        this.view.handleCloseButtonClick();
    }

    public void handleImageDialogClose(Dialog dialog) {
        this.view.handleImageDialogClose(dialog);
    }

    public void handleImageClick(Dialog dialog) {
        this.view.handleImageClick(dialog);
    }

    public void handleUpVoteButtonClick() {
        this.modal.handleUpVoteButtonClick();
    }

    public void handleDownVoteButtonClick() {
        this.modal.handleDownVoteButtonClick();
    }

    public void createMarkerDeletionDialog() {
        this.view.createMarkerDeletionDialog();
    }

    public void makeApiCallDeleteMarker() {
        this.marker.makeApiCallDeleteMarker(this.view.getApplicationContext(), this);
    }

    public void makeApiCallCreateGetMarkerImage() {
        this.marker.makeApiCallCreateGetMarkerImage(this.view.getApplicationContext(), this);
    }

    public void decodeModalImage() {
        this.modal.decodeImage();
    }

    public void updateModalEncodedImage(String encodedImage) {
        this.marker.setEncodedImage(encodedImage);
    }

    public void updateModalPostName() {
        this.modal.handleModalName();
    }

    public void submitMarkerRating() {
        this.modal.makeApiCall(this.view.getApplicationContext(), marker);
    }

    public void updateRating() {
        this.modal.updateRating();
    }

    public void configureUserRatingState() {
        this.modal.configureUserRatingState(this.view.getApplicationContext(), this.marker);
    }

    public void saveSettingsSharedPreference() {
        this.modal.saveSettingsSharedPreference(this.view.getApplicationContext(), marker);
    }

    public void setUpVote() {
        this.view.setUpVote();
    }

    public void setDownVote() {
        this.view.setDownVote();
    }

    public void removeVote() {
        this.view.removeVote();
    }

    public String getAltName() {
        return this.modal.getAltName();
    }

    public String getUserPost() {
        return this.modal.getUserPost();
    }

    public Bitmap getModal() {
        return this.modal.getImage();
    }

    public Marker getMarker() {
        return marker;
    }

    public int getRating() {
        return this.modal.getRating();
    }
}
