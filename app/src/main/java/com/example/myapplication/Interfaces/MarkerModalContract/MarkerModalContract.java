package com.example.myapplication.Interfaces.MarkerModalContract;

import android.app.Dialog;
import android.content.Context;

public interface MarkerModalContract {

    interface View{
        void handleTokenExpiration();
       void handleCloseButtonClick();
       void handleImageDialogClose(Dialog dialog);
       void handleImageClick(Dialog dialog);
       void createMarkerDeletionDialog();
       void closeUserPost(boolean response);
       void addImageToModal(String encodedString);
       Context getApplicationContext();
    }

    interface Presenter{
    }

    interface Model{
    }

}
