package com.example.myapplication.Handlers.RatingHandler;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.R;

public class RatingHandler {

    MarkerModalFragment markerModalFragment;
    boolean isUpvoteClicked;
    boolean isDownvoteClicked;

    public RatingHandler(MarkerModalFragment markerModalFragment) {
        this.markerModalFragment = markerModalFragment;
    }

    public void configure(){
        configureRatingButtons();
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

                Log.d("Print", "Upvote clicked " + isUpvoteClicked + " " + isDownvoteClicked);
            }
        });

        downVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDownVoteClicked();
                downVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_downvote_arrow_clicked));
                upVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_upvote_arrow));

                Log.d("Print", "Upvote clicked " + isDownvoteClicked + " " + isUpvoteClicked);

            }
        });

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
