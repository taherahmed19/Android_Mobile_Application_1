package com.example.myapplication.Handlers.RatingHandler;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.HttpRequest.HttpRatings.HttpRatings;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.R;

public class RatingHandler {

    MarkerModalFragment markerModalFragment;
    Marker marker;

    boolean isUpvoteClicked;
    boolean isDownvoteClicked;
    int rating;
    int ratingTemp;

    public RatingHandler(MarkerModalFragment markerModalFragment, Marker marker) {
        this.markerModalFragment = markerModalFragment;
        this.isUpvoteClicked = false;
        this.isDownvoteClicked = false;
        this.rating = marker.getRating();
        this.ratingTemp = rating;
        this.marker = marker;
    }

    public void configure(){
        configureRatingButtons();
    }

    public void setRating(){
        this.rating = ratingTemp;
    }

    public int getRating() {
        return rating;
    }

    void configureRatingButtons(){
        ImageButton upVoteButton = (ImageButton) this.markerModalFragment.getView().findViewById(R.id.upVoteButton);
        ImageButton downVoteButton = (ImageButton) this.markerModalFragment.getView().findViewById(R.id.downVoteButton);

        upVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUpVoteClicked();
                upVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_upvote_arrow_clicked));
                downVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_downvote_arrow));

                ratingTemp++;
                submitMarkerRating(isUpvoteClicked);
            }
        });

        downVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDownVoteClicked();
                downVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_downvote_arrow_clicked));
                upVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_upvote_arrow));

                ratingTemp--;
                submitMarkerRating(isUpvoteClicked);
            }
        });
    }

    void submitMarkerRating(boolean isUpVote){
        HttpRatings httpRatings = new HttpRatings(this.markerModalFragment.getContext(), marker.getId(), isUpVote, this.markerModalFragment);
        httpRatings.execute("https://10.0.2.2:443/api/getmarkers");
    }

    void isUpVoteClicked(){
        if(isUpvoteClicked){
            isUpvoteClicked = false;
        }else{
            isUpvoteClicked = true;
        }
        isDownvoteClicked = false;
    }

    void isDownVoteClicked(){
        if(isDownvoteClicked){
            isDownvoteClicked = false;
        }else{
            isDownvoteClicked = true;
        }
        isUpvoteClicked = false;
    }
}
