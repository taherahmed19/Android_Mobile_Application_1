package com.example.myapplication.Interfaces.MarkerModalContract;

import android.app.Dialog;
import android.content.Context;

public interface MarkerModalContract {

    interface View{
       void handleCloseButtonClick();
       void handleImageDialogClose(Dialog dialog);
       void handleImageClick(Dialog dialog);
       void createMarkerDeletionDialog();
       void closeUserPost(boolean response);
       void addImageToModal(String encodedString);
       void handleUpVoteButtonClick();
       void handleDownVoteButtonClick();
       void updateModalRating(boolean response);
       void saveModalRatingState(int rating);
       void setUpVote();
       void setDownVote();
       void removeVote();
       Context getApplicationContext();
    }

    interface Presenter{
    }

    interface Model{
    }

}
