package com.example.myapplication.Presenters.FormPresenter;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.Interfaces.FeedSubmitListener.FeedSubmitListener;
import com.example.myapplication.Interfaces.FormContract.FormContract;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Webservice.HttpMarker.HttpMarker;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;

public class FormPresenter implements FormContract.Presenter {

    FormContract.View view;
    LatLng chosenLocation;
    LatLng currentLatLng;
    String encodedImage;

    public FormPresenter(FormContract.View view) {
        this.view = view;
    }

    public void handleLocationButtonClick(){
        this.view.handleLocationButtonClick();
    }

    public boolean handleDescriptionScroll(EditText mapFeedDescription, View v, MotionEvent event){
        return this.view.handleDescriptionScroll(mapFeedDescription, v, event);
    }

    public void updateChosenLocation(double lat, double lng){
        this.chosenLocation = new LatLng(lat, lng);
    }

    public LatLng getChosenLocation(){
        return this.chosenLocation;
    }

    public void updateEncodedImage(Bitmap photo){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        this.encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public String getEncodedImage(){
        return this.encodedImage;
    }

    public void updateCurrentLatLng(Location location){
        this.currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void makeApiCall(int userID, String category, String description, FeedSubmitListener feedSubmitListener){
        HttpMarker httpMarker = new HttpMarker(view.getApplicationContext(), userID, category, description, chosenLocation ,feedSubmitListener, this.encodedImage);
        httpMarker.execute();
    }

    public void handleSubmitButtonClick(){
        this.view.handleSubmitButtonClick();
    }

    public void handleCloseButtonClick(){
        this.view.handleCloseButtonClick();
    }

    public void handleCameraDialog(){
        this.view.handleCameraDialog();
    }

    public void openCamera(){
        this.view.openCamera();
    }

    public void openGallery(){
        this.view.openGallery();
    }
}
