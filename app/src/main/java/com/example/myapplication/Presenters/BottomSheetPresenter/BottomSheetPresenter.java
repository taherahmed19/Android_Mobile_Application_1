package com.example.myapplication.Presenters.BottomSheetPresenter;

import android.widget.SeekBar;

import com.example.myapplication.Interfaces.BottomSheetContract.BottomSheetContract;
import com.example.myapplication.Interfaces.DeleteRadiusMarkerListener.DeleteRadiusMarkerListener;
import com.example.myapplication.Interfaces.SetRadiusMarkerListener.SetRadiusMarkerListener;
import com.example.myapplication.Models.BottomSheet.BottomSheet;
import com.example.myapplication.Models.RadiusMarker.RadiusMarker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Responsible for handling actions from the View and updating the UI as required.
 */
public class BottomSheetPresenter implements BottomSheetContract.Presenter, SetRadiusMarkerListener, DeleteRadiusMarkerListener {

    BottomSheetContract.View view;
    GoogleMap mMap;
    LatLng latLng;
    RadiusMarker radiusMarker;
    BottomSheet bottomSheet;

    public BottomSheetPresenter(GoogleMap mMap, LatLng latLng,
                                RadiusMarker radiusMarker,
                                BottomSheetContract.View view) {
        this.view = view;
        this.mMap = mMap;
        this.latLng = latLng;
        this.radiusMarker = radiusMarker;
        this.bottomSheet = new BottomSheet(this);
    }

    @Override
    public void handleRadiusMarker(boolean valid){
        if(valid){
            this.view.handleRadiusMarker();
        }else{
            radiusMarker.removeMarker();
        }
    }

    @Override
    public void handleRadiusMarkerRemoval(boolean valid){
        this.view.handleRadiusMarkerRemoval(valid);
    }

    public void makeApiRequestWriteRadiusMarker(){
        this.radiusMarker.makeApiRequestWriteRadiusMarker(this.view.getApplicationContext(), this,
                bottomSheet.isInAppButtonClicked(), bottomSheet.isVoiceButtonClicked());
    }

    public void makeApiRequestDeleteRadiusMarker(){
        this.radiusMarker.makeApiRequestDeleteRadiusMarker(this.view.getApplicationContext(), this);
    }

    public void handleSaveButtonClick(){
        this.view.handleSaveButtonClick();
    }

    public void updateRadiusMarkerSeekBar(SeekBar seekBar, int progress){
        this.view.updateRadiusMarkerSeekBar(seekBar, progress);
    }

    public void updateRadiusMarkerInAppButton(){
        this.bottomSheet.updateRadiusMarkerInAppButton();
    }

    public void updateRadiusMarkerVoiceButton(){
        this.bottomSheet.updateRadiusMarkerVoiceButton();
    }

    public void removeVoiceButtonBackground(){
        this.view.removeVoiceButtonBackground();
    }

    public void removeInAppButtonBackground(){
        this.view.removeInAppButtonBackground();
    }

    public void setVoiceButtonClicked(){
        this.view.setVoiceButtonClicked();
    }

    public void setInAppButtonClicked(){
        this.view.setInAppButtonClicked();
    }

    public void setNotificationsState() {
        this.bottomSheet.setNotificationsState(this.view.getApplicationContext());
    }

    public void closeBottomSheetView(){
        this.view.closeBottomSheetView(radiusMarker);
    }

    public void showRemoveDialog(){
        this.view.showRemoveDialog();
    }

    public void dismissRemoveDialog(){
        this.view.dismissRemoveDialog();
    }

    public void resetNotificationsBackgroundState(){
        this.bottomSheet.resetNotificationsBackgroundState();
    }

    public void removeMarker(){
        this.radiusMarker.removeMarker();
    }

    public void setOriginalRadius(){
        this.radiusMarker.setOriginalRadius(this.radiusMarker.getRadius());
    }

    public void updateRadius(double radius){
        this.radiusMarker.updateRadius(radius);
    }

    public double getRadiusMarkerRadius(){
        return this.radiusMarker.getRadiusMarker().getRadius();
    }

    public boolean isInAppButtonClicked() {
        return this.bottomSheet.isInAppButtonClicked();
    }

    public boolean isVoiceButtonClicked() {
        return this.bottomSheet.isVoiceButtonClicked();
    }

    public void resetRadiusMarkerSize(){
        this.radiusMarker.updateRadius(this.radiusMarker.getOriginalRadius());
    }
}
