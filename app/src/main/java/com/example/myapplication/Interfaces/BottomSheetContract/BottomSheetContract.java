package com.example.myapplication.Interfaces.BottomSheetContract;

import android.content.Context;
import android.widget.SeekBar;

import com.example.myapplication.Fragments.BottomSheetFragment.BottomSheetFragment;
import com.example.myapplication.Models.RadiusMarker.RadiusMarker;

/**
 * Defines the contract between the BottomSheetFragment view {@link BottomSheetFragment} and the
 * presenter {@link com.example.myapplication.Presenters.BottomSheetPresenter.BottomSheetPresenter}
 */
public interface BottomSheetContract {

    interface View{
        void handleSaveButtonClick();
        void updateRadiusMarkerSeekBar(SeekBar seekBar, int progress);
        void setInAppButtonClicked();
        void removeInAppButtonBackground();
        void setVoiceButtonClicked();
        void removeVoiceButtonBackground();
        void closeBottomSheetView(RadiusMarker radiusMarker);
        void showRemoveDialog();
        void dismissRemoveDialog();
        void handleRadiusMarkerRemoval(Boolean valid);
        Context getApplicationContext();
    }

    interface Presenter{

    }

}
