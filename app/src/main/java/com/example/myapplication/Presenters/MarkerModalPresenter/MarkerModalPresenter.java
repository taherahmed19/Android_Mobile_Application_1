package com.example.myapplication.Presenters.MarkerModalPresenter;

import android.app.Dialog;
import android.graphics.Bitmap;

import com.example.myapplication.Interfaces.MarkerImageListener.MarkerImageListener;
import com.example.myapplication.Interfaces.MarkerListener.MarkerListener;
import com.example.myapplication.Interfaces.MarkerModalContract.MarkerModalContract;
import com.example.myapplication.Interfaces.TokenExpirationListener.TokenExpirationListener;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.Modal.Modal;

public class MarkerModalPresenter implements MarkerListener, MarkerImageListener, TokenExpirationListener {

    MarkerModalContract.View view;
    Marker marker;
    Modal modal;

    public MarkerModalPresenter(MarkerModalContract.View view, Marker marker) {
        this.view = view;
        this.marker = marker;
        this.modal = new Modal(marker.getEncodedImage(), marker.getFirstName(), marker.getLastName(), this, this);
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
    public void handleTokenExpiration() {
        this.view.handleTokenExpiration();
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
    public void createMarkerDeletionDialog() {
        this.view.createMarkerDeletionDialog();
    }

    public void makeApiCallDeleteMarker() {
        this.marker.makeApiCallDeleteMarker(this.view.getApplicationContext(), this, this);
    }

    public void makeApiCallCreateGetMarkerImage() {
        this.marker.makeApiCallCreateGetMarkerImage(this.view.getApplicationContext(), this, this);
    }

    public void decodeModalImage() {
        this.modal.decodeImage();
    }

    public void updateModalEncodedImage(String encodedImage) {
        this.marker.setEncodedImage(encodedImage);//note needed?
        this.modal.setEncodedImage(encodedImage);
    }

    public void updateModalPostName() {
        this.modal.handleModalName();
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

}
