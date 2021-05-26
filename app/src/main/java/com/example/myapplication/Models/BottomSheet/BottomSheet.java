package com.example.myapplication.Models.BottomSheet;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myapplication.Presenters.BottomSheetPresenter.BottomSheetPresenter;

import java.util.Objects;

public class BottomSheet {

    boolean inAppButtonClicked;
    boolean voiceButtonClicked;

    BottomSheetPresenter bottomSheetPresenter;

    public BottomSheet(BottomSheetPresenter bottomSheetPresenter) {
        this.bottomSheetPresenter = bottomSheetPresenter;
    }

    public void updateRadiusMarkerInAppButton(){
        if(!inAppButtonClicked){
            inAppButtonClicked = true;
            voiceButtonClicked = false;
            this.bottomSheetPresenter.setInAppButtonClicked();
        }else{
            inAppButtonClicked = false;
            this.bottomSheetPresenter.removeInAppButtonBackground();
        }
    }

    public void updateRadiusMarkerVoiceButton(){
        if(!voiceButtonClicked){
            voiceButtonClicked = true;
            inAppButtonClicked = false;
            this.bottomSheetPresenter.setVoiceButtonClicked();
        }else{
            voiceButtonClicked = false;
            this.bottomSheetPresenter.removeVoiceButtonBackground();
        }
    }

    public void setNotificationsState(Context context){
        SharedPreferences settingsPreference = Objects.requireNonNull(context).getSharedPreferences("Radius_Marker_Settings", 0);
        boolean inAppButtonState = settingsPreference.getBoolean("inAppNotifications", false);
        boolean voiceButtonState = settingsPreference.getBoolean("voiceNotifications", false);

        inAppButtonClicked = inAppButtonState;
        voiceButtonClicked = voiceButtonState;
    }

    public void resetNotificationsBackgroundState(){
        if(inAppButtonClicked){
            this.bottomSheetPresenter.setInAppButtonClicked();
        }else if(voiceButtonClicked){
            this.bottomSheetPresenter.setVoiceButtonClicked();
        }else{
            inAppButtonClicked = true;
            this.bottomSheetPresenter.setInAppButtonClicked();
        }
    }

    public boolean isInAppButtonClicked() {
        return inAppButtonClicked;
    }

    public boolean isVoiceButtonClicked() {
        return voiceButtonClicked;
    }
}
