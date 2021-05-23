package com.example.myapplication.Handlers.RatingHandler;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.Webservice.HttpRatings.HttpRatings;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.R;

public class RatingHandler {

    MarkerModalFragment markerModalFragment;
    Marker marker;

    boolean isUpVoteClicked;
    boolean isDownVoteClicked;
    int originalRating;
    int rating;
    int ratingTemp;
    ImageButton upVoteButton;
    ImageButton downVoteButton;

    public RatingHandler(MarkerModalFragment markerModalFragment, Marker marker) {
        this.markerModalFragment = markerModalFragment;
        this.isUpVoteClicked = false;
        this.isDownVoteClicked = false;
        this.rating = marker.getRating();
        this.originalRating = rating;
        this.ratingTemp = rating;
        this.marker = marker;
    }

    public void saveSettingsSharedPreference(){
        SharedPreferences settingsPreference = this.markerModalFragment.getContext().getSharedPreferences("Marker_Modal_Fragment_Ratings", 0);
        SharedPreferences.Editor preferenceEditor = settingsPreference.edit();

        if(!isDownVoteClicked && !isUpVoteClicked){
            preferenceEditor.putString(String.valueOf(this.marker.getId() + "isUpvoteClicked"), "");
        }else{
            preferenceEditor.putString(String.valueOf(this.marker.getId() + "isUpvoteClicked"), Boolean.toString(isUpVoteClicked));
        }

        preferenceEditor.putInt(String.valueOf(this.marker.getId() + "rating"), rating);
        preferenceEditor.apply();
    }

    public void configure(){
        configureUpVoteButton();
        configureDownVoteButton();
        configureUserRatingState();
        configureRatingButtons();
    }

    public void setRating(){
        this.rating = ratingTemp;
    }

    public int getRating() {
        return rating;
    }

    void configureUpVoteButton(){
        this.upVoteButton = (ImageButton) this.markerModalFragment.getView().findViewById(R.id.upVoteButton);
    }

    void configureDownVoteButton(){
        this.downVoteButton = (ImageButton) this.markerModalFragment.getView().findViewById(R.id.downVoteButton);
    }

    void configureRatingButtons(){
        upVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isUpVoteClicked){
                    isUpVoteClicked();
                    setUpVote();

                    ratingTemp = originalRating + 1;
                    submitMarkerRating(isUpVoteClicked);
                }else{
                    isUpVoteClicked();
                    removeVote();
                    ratingTemp= originalRating;
                    submitMarkerRating(false);
                }

            }
        });

        downVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isDownVoteClicked){
                    isDownVoteClicked();
                    setDownVote();

                    ratingTemp= originalRating -1;
                    submitMarkerRating(isUpVoteClicked);
                }else{
                    isDownVoteClicked();
                    removeVote();
                    ratingTemp = originalRating;
                    submitMarkerRating(true);
                }
            }
        });
    }

    void configureUserRatingState(){
        boolean isUpVoteChosen;
        String isUpVoteChosenPrefStr;
        SharedPreferences settingsPreference = this.markerModalFragment.getContext().getSharedPreferences("Marker_Modal_Fragment_Ratings", 0);

        isUpVoteChosenPrefStr = settingsPreference.getString(String.valueOf(this.marker.getId() + "isUpvoteClicked"), "");

        if(!TextUtils.isEmpty(isUpVoteChosenPrefStr)) {
            isUpVoteChosen = Boolean.parseBoolean(isUpVoteChosenPrefStr);

            if (isUpVoteChosen) {
                setUpVote();
                this.isUpVoteClicked = true;
                this.isDownVoteClicked = false;
            } else {
                setDownVote();
                this.isDownVoteClicked = true;
                this.isUpVoteClicked = false;
            }
        } else{
            removeVote();
            this.isUpVoteClicked = false;
            this.isDownVoteClicked = false;
        }

        int rating = settingsPreference.getInt(String.valueOf(this.marker.getId() + "rating"), -10);

        if(rating != -10){
            this.rating = rating;
            this.ratingTemp = rating;
            this.markerModalFragment.saveModalRatingState(rating);
        }
    }

    void submitMarkerRating(boolean isUpVote){
        HttpRatings httpRatings = new HttpRatings(this.markerModalFragment.getContext(), marker.getId(), isUpVote, this.markerModalFragment);
        httpRatings.execute("");
    }

    void setUpVote(){
        upVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_upvote_arrow_clicked));
        downVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_downvote_arrow));
    }

    void setDownVote(){
        downVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_downvote_arrow_clicked));
        upVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_upvote_arrow));
    }

    void removeVote(){
        upVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_upvote_arrow));
        downVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_downvote_arrow));
    }

    void isUpVoteClicked(){
        if(isUpVoteClicked){
            isUpVoteClicked = false;
        }else{
            isUpVoteClicked = true;
        }
        isDownVoteClicked = false;
    }

    void isDownVoteClicked(){
        if(isDownVoteClicked){
            isDownVoteClicked = false;
        }else{
            isDownVoteClicked = true;
        }
        isUpVoteClicked = false;
    }
}
