package com.example.myapplication.Handlers.RatingHandler;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.R;

public class RatingHandler {

    MarkerModalFragment markerModalFragment;
    boolean isUpvote;
    boolean isDownvote;

    public RatingHandler(MarkerModalFragment markerModalFragment) {
        this.markerModalFragment = markerModalFragment;
        this.isUpvote = false;
        this.isDownvote = false;
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
                Log.d("Print", "Clicked upvote button");
                if(!isUpvote){
                    isUpvote = true;
                    upVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_upvote_arrow_clicked));
                    downVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_downvote_arrow));
                }else{
                    isUpvote = false;
                    upVoteButton.setBackgroundResource(R.drawable.ic_upvote_arrow);
                }
            }
        });

        downVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isDownvote){
                    isDownvote = true;
                    downVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_downvote_arrow_clicked));
                    upVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_upvote_arrow));
                }else{
                    isDownvote = false;
                    downVoteButton.setImageDrawable(markerModalFragment.getResources().getDrawable(R.drawable.ic_downvote_arrow));
                }
            }
        });

    }
}
