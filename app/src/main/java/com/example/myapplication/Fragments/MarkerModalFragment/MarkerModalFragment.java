package com.example.myapplication.Fragments.MarkerModalFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.Models.Marker.Marker;

public class MarkerModalFragment extends Fragment {
    Marker marker;

    public MarkerModalFragment(Marker marker) {
        this.marker = marker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_marker_modal, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateModal();
        configureCloseButton();
    }

    void configureCloseButton(){
        LinearLayout modalCloseButton = (LinearLayout) getView().findViewById(R.id.modalCloseButton);
        modalCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }

    void populateModal(){
        populateCategory();
        populateDescription();
    }

    void populateImage(){

    }

    void populateCategory() {
        TextView modalCategory = getView().findViewById(R.id.modalCategory);
        String category = "";
        int markerType = marker.getMarker();

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

    void populateDescription(){
        String description = marker.getDescription();
        TextView modalDescription = (TextView) getView().findViewById(R.id.modalDescription);

        modalDescription.setText(description);
    }
}