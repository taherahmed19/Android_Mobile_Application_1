package com.example.myapplication.Interfaces.FormContract;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.EditText;

public interface FormContract {

    interface View{
        void handleTokenExpiration();
        void openGallery();
        void openCamera();
        void handleCameraDialog();
        void handleCloseButtonClick();
        void handleSubmitButtonClick();
        void handleLocationButtonClick();
        boolean handleDescriptionScroll(EditText mapFeedDescription, android.view.View v, MotionEvent event);
        Context getApplicationContext();
    }

    interface Presenter{
    }

    interface Model{
    }

}
