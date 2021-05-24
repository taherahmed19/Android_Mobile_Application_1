package com.example.myapplication.Presenters.MarkerModalPresenter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.example.myapplication.Interfaces.MarkerImageListener.MarkerImageListener;
import com.example.myapplication.Interfaces.MarkerListener.MarkerListener;
import com.example.myapplication.Interfaces.MarkerModalContract.MarkerModalContract;
import com.example.myapplication.Interfaces.RatingsListener.RatingsListener;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.Modal.Modal;
import com.example.myapplication.Webservice.HttpMarkerDelete.HttpMarkerDelete;
import com.example.myapplication.Webservice.HttpMarkerImage.HttpMarkerImage;

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

    public void handleCloseButtonClick(){
        this.view.handleCloseButtonClick();
    }

    public void handleImageDialogClose(Dialog dialog){
        this.view.handleImageDialogClose(dialog);
    }

    public void handleImageClick(Dialog dialog){
        this.view.handleImageClick(dialog);
    }

    public void createMarkerDeletionDialog(){
        this.view.createMarkerDeletionDialog();
    }

    //move to marker model
    public void makeApiCallDeleteMarker(){
        HttpMarkerDelete httpMarkerDelete = new HttpMarkerDelete(view.getApplicationContext(), this.marker.getId(), this);
        httpMarkerDelete.execute();
    }

    public void makeApiCallCreateGetMarkerImage(){
        HttpMarkerImage httpMarkerImage = new HttpMarkerImage(view.getApplicationContext(), this.marker.getId(), this);
        httpMarkerImage.execute();
    }

    public void decodeModalImage(){
        this.modal.decodeImage();
    }

    public Bitmap getModal(){
        return this.modal.getImage();
    }

    public Marker getMarker() {
        return marker;
    }

    public void updateModalEncodedImage(String encodedImage){
        this.marker.setEncodedImage(encodedImage);
    }

    public void updateModalPostName(){
        this.modal.handleModalName();
    }

    public String getAltName(){
        return this.modal.getAltName();
    }

    public String getUserPost(){
        return this.modal.getUserPost();
    }

    public void configureUserRatingState(){
        this.modal.configureUserRatingState(this.view.getApplicationContext(), this.marker);
    }

    public void saveSettingsSharedPreference(){
        this.modal.saveSettingsSharedPreference(this.view.getApplicationContext(), marker);
    }

    public void setUpVote(){
        this.view.setUpVote();
    }

    public void setDownVote(){
        this.view.setDownVote();
    }

    public void removeVote(){
        this.view.removeVote();
    }

    public void handleUpVoteButtonClick(){
        if(!getUpVoteClicked()){
            isUpVoteClicked();
            setUpVote();

            incrementRating();
            submitMarkerRating();
        }else{
            isUpVoteClicked();
            removeVote();

            setRatingToOriginalValue();
            submitMarkerRating();
        }
    }

    public void handleDownVoteButtonClick(){
        if(!getDownVoteClicked()){
            isDownVoteClicked();
            setDownVote();

            decrementRating();
            submitMarkerRating();
        }else{
            isDownVoteClicked();
            removeVote();

            setRatingToOriginalValue();
            submitMarkerRating();
        }
    }

    public boolean getUpVoteClicked(){
        return this.modal.getUpVoteClicked();
    }

    public boolean getDownVoteClicked(){
        return this.modal.getDownVoteClicked();
    }

    public void isUpVoteClicked(){
        this.modal.isUpVoteClicked();
    }

    public void isDownVoteClicked(){
        this.modal.isDownVoteClicked();
    }

    public void submitMarkerRating(){
        this.modal.makeApiCall(this.view.getApplicationContext(), marker);
    }

    public void incrementRating(){
        this.modal.incrementRating();
    }

    public void decrementRating(){
        this.modal.decrementRating();
    }

    public void setRatingToOriginalValue(){
        this.modal.setRatingToOriginalValue();
    }

    public void updateRating(){
        this.modal.updateRating();
    }

    public int getRating(){
        return this.modal.getRating();
    }
}
