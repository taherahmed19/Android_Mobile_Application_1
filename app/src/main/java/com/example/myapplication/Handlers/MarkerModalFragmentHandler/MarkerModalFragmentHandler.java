package com.example.myapplication.Handlers.MarkerModalFragmentHandler;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;

public class MarkerModalFragmentHandler {

    MarkerModalFragment markerModalFragment;

    public MarkerModalFragmentHandler(MarkerModalFragment markerModalFragment) {
        this.markerModalFragment = markerModalFragment;
    }

    public void configure(){
        configureRating();
        configureCategory();
        configureDescription();
        configureName();
        configureCloseButton();
    }

    public void updateRatingValue(int rating){
        TextView markerRating = (TextView) this.markerModalFragment.getView().findViewById(R.id.markerRating);
        markerRating.setText(String.valueOf(rating));
    }

    void configureCloseButton(){
        ImageButton modalCloseButton = (ImageButton) this.markerModalFragment.getView().findViewById(R.id.modalCloseButton);
        modalCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerModalFragment.getFragmentManager().popBackStack();
            }
        });
    }

    void configureRating(){
        TextView markerRating = (TextView) this.markerModalFragment.getView().findViewById(R.id.markerRating);
        String rating = String.valueOf(this.markerModalFragment.getMarker().getRating());
        markerRating.setText(rating);
    }

    void configureCategory() {
        TextView modalCategory = this.markerModalFragment.getView().findViewById(R.id.modalCategory);
        String category = "";
        int markerType = this.markerModalFragment.getMarker().getMarker();

        switch (markerType) {
            case 1:
                category = "Environment";
                break;
            case 2:
                category = "Weather";
                break;
            case 3:
                category = "People";
                break;
            default:
                category = "None";
                break;
        }

        modalCategory.setText(category);
    }

    void configureDescription(){
        String description = this.markerModalFragment.getMarker().getDescription();
        TextView modalDescription = (TextView) this.markerModalFragment.getView().findViewById(R.id.modalDescription);

        //modalDescription.setText(description);
    }

    void configureName(){
        String firstName = LoginPreferenceData.getUserFirstName(this.markerModalFragment.getContext());
        String lastName = LoginPreferenceData.getUserLastName(this.markerModalFragment.getContext());

        String concatName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1) + " " + lastName.substring(0, 1).toUpperCase() + lastName.substring(1);

        TextView text = this.markerModalFragment.getView().findViewById(R.id.marker_user_name);
        text.setText(concatName + " " + this.markerModalFragment.getMarker().getId());
    }

    void configureImage(){

    }
}
