package com.example.myapplication.Presenters.BottomSheetPresenter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Models.BottomSheet.BottomSheet;
import com.example.myapplication.Models.RadiusMarker.RadiusMarker;
import com.example.myapplication.Handlers.RadiusMarkerHandler.RadiusMarkerStorage;
import com.example.myapplication.Interfaces.BottomSheetContract.BottomSheetContract;
import com.example.myapplication.Interfaces.DeleteRadiusMarkerListener.DeleteRadiusMarkerListener;
import com.example.myapplication.Interfaces.SetRadiusMarkerListener.SetRadiusMarkerListener;
import com.example.myapplication.R;
import com.example.myapplication.Fragments.BottomSheetFragment.BottomSheetFragment;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Webservice.HttpDeleteRadiusMarker.HttpDeleteRadiusMarker;
import com.example.myapplication.Webservice.HttpWriteRadiusMarker.HttpWriteRadiusMarker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

/**
 * Responsible for handling actions from the View and updating the UI as required.
 */
public class BottomSheetPresenter implements BottomSheetContract.Presenter, SetRadiusMarkerListener, DeleteRadiusMarkerListener {

    BottomSheetContract.View view;
    GoogleMap mMap;
    LatLng latLng;
    RadiusMarker radiusMarker;
    BottomSheet bottomSheet;
    FragmentManager fragmentManager;

    public BottomSheetPresenter(GoogleMap mMap, LatLng latLng,
                                RadiusMarker radiusMarker,
                                FragmentManager fragmentManager, BottomSheetContract.View view) {
        this.view = view;
        this.mMap = mMap;
        this.latLng = latLng;
        this.radiusMarker = radiusMarker;
        this.fragmentManager = fragmentManager;
        this.bottomSheet = new BottomSheet(this);
    }

    @Override
    public void handleRadiusMarker(boolean valid){
        if(valid){
            //radiusMarkerStorage.saveSharedPreference(inAppButtonClicked, voiceButtonClicked, latLng);
            fragmentManager.popBackStack();
        }else{
            radiusMarker.removeMarker();
            Toast.makeText(this.view.getApplicationContext(), this.view.getApplicationContext().getString(R.string.radius_marker_set_body), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void handleRadiusMarkerRemoval(boolean valid){
        if(!valid){
            Toast.makeText(this.view.getApplicationContext(), this.view.getApplicationContext().getString(R.string.radius_marker_delete_body), Toast.LENGTH_LONG).show();
        }
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

    public void setInAppButtonClicked(){
        this.view.setInAppButtonClicked();
    }

    public void removeInAppButtonBackground(){
        this.view.removeInAppButtonBackground();
    }

    public void setVoiceButtonClicked(){
        this.view.setVoiceButtonClicked();
    }

    public void removeVoiceButtonBackground(){
        this.view.removeVoiceButtonBackground();
    }

    public void closeBottomSheetView(){
        this.view.closeBottomSheetView();
    }

    public void showRemoveDialog(){
        this.view.showRemoveDialog();
    }

    public void dismissRemoveDialog(){
        this.view.dismissRemoveDialog();
    }

    public void setNotificationsState(){
        this.bottomSheet.setNotificationsState(this.view.getApplicationContext());
    }

    public void resetNotificationsBackgroundState(){
        this.bottomSheet.resetNotificationsBackgroundState();
    }

    public void handleSaveButtonClick(){
        this.view.handleSaveButtonClick();
    }

    public void removeMarker(){
        this.radiusMarker.removeMarker();
    }

    public void updateRadius(double radius){
        this.radiusMarker.getRadiusMarker().setRadius(radius);
    }

    public double getRadiusMarkerRadius(){
        return this.radiusMarker.getRadiusMarker().getRadius();
    }

    public void writeRadiusMarkerDb(){
        this.radiusMarker.writeRadiusMarkerDb(this.view.getApplicationContext(), this);
    }

    public void deleteRadiusMarkerDb(){
        this.radiusMarker.deleteRadiusMarkerDb(this.view.getApplicationContext(), this);
    }

}
