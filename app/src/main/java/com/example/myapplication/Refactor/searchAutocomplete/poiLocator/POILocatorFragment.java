package com.example.myapplication.Refactor.searchAutocomplete.poiLocator;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

import static android.app.Activity.RESULT_OK;

public class POILocatorFragment extends Fragment {

    Button selectedButton;

    POILocatorFragmentElements poiLocatorFragmentElements;

    public POILocatorFragment() {
        this.selectedButton = null;
        this.poiLocatorFragmentElements = new POILocatorFragmentElements();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_p_o_i_locator, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        poiLocatorFragmentElements.configurePOISelection();
        poiLocatorFragmentElements.configureSubmitButton();
        poiLocatorFragmentElements.configureCloseButton();
    }

    private class POILocatorFragmentElements{

        void configureSubmitButton(){
            Button poiLocatorSubmit = (Button) getView().findViewById(R.id.poiLocatorSubmit);

            poiLocatorSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(selectedButton != null){
                        String selectedButtonText = retrieveButtonText(selectedButton.getId());

                        Intent intent = new Intent(getContext(), POILocatorFragment.class);
                        intent.putExtra("selectedPOI", selectedButtonText);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
                        getFragmentManager().popBackStack();
                    }
                }
            });
        }

        void configureCloseButton(){
            TextView POILocatorCloseButton = (TextView) getView().findViewById(R.id.POILocatorCloseButton);

            POILocatorCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
        }

        void configurePOISelection(){
            Button poiRestaurant = (Button) getView().findViewById(R.id.poiRestaurant);
            Button poiCinema = (Button) getView().findViewById(R.id.poiCinema);
            Button poiPark = (Button) getView().findViewById(R.id.poiPark);
            Button poiAirport = (Button) getView().findViewById(R.id.poiAirport);
            Button poiLeisureCentre = (Button) getView().findViewById(R.id.poiLeisureCentre);

            addPOISelectionListener(poiRestaurant);
            addPOISelectionListener(poiCinema);
            addPOISelectionListener(poiPark);
            addPOISelectionListener(poiAirport);
            addPOISelectionListener(poiLeisureCentre);
        }

        void addPOISelectionListener(Button button){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedButton = button;

                    String selectedButtonText = retrieveButtonText(button.getId());
                    Log.d("Print", "Button clicked " + selectedButtonText);
                }
            });
        }

        String retrieveButtonText(int buttonId){
            String selectedButtonText = "";

            switch (buttonId){
                case R.id.poiRestaurant:
                    TextView poiRestaurantText = (TextView) getView().findViewById(R.id.poiRestaurantText);
                    selectedButtonText = poiRestaurantText.getText().toString();
                    break;
                case R.id.poiCinema:
                    TextView poiCinemaText = (TextView) getView().findViewById(R.id.poiCinemaText);
                    selectedButtonText = poiCinemaText.getText().toString();
                    break;
                case R.id.poiPark:
                    TextView poiParkText = (TextView) getView().findViewById(R.id.poiParkText);
                    selectedButtonText = poiParkText.getText().toString();
                    break;
                case R.id.poiAirport:
                    TextView poiAirportText = (TextView) getView().findViewById(R.id.poiAirportText);
                    selectedButtonText = poiAirportText.getText().toString();
                    break;
                case R.id.poiLeisureCentre:
                    TextView poiLeisureCentreText = (TextView) getView().findViewById(R.id.poiLeisureCentreText);
                    selectedButtonText = poiLeisureCentreText.getText().toString();
                    break;
            }

            return selectedButtonText;
        }

    }
}