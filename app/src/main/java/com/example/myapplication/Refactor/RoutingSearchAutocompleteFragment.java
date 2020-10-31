package com.example.myapplication.Refactor;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.Utils.Tools.Tools;
import com.example.myapplication.Refactor.searchAutocomplete.Place;

public class RoutingSearchAutocompleteFragment extends Fragment {

    ScrollView searchAutocompleteContainer;
    LinearLayout searchAutocompleteSubContainer;

    RoutingSearchAutocompleteFragmentElements routingSearchAutocompleteFragmentElements;
    RoutingSearchFragment routingSearchFragment;

    public RoutingSearchAutocompleteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_routing_search_autocomplete, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.searchAutocompleteContainer = (ScrollView) getView().findViewById(R.id.searchAutocompleteContainer);
        this.searchAutocompleteSubContainer = (LinearLayout) getView().findViewById(R.id.searchAutocompleteSubContainer);
        this.routingSearchAutocompleteFragmentElements = new RoutingSearchAutocompleteFragmentElements();
    }

    public void buildAutocompleteSearchItem(Place place, String mainText, String secondText){
        this.routingSearchAutocompleteFragmentElements.buildAutocompleteSearchItem(place, mainText, secondText);
    }

    public void clearAutocomplete(){
        this.routingSearchAutocompleteFragmentElements.clearAutocomplete();
    }

    public void setRoutingSearchFragment(RoutingSearchFragment routingSearchFragment) {
        this.routingSearchFragment = routingSearchFragment;
    }

    private class RoutingSearchAutocompleteFragmentElements{

        void clearAutocomplete(){
            if(searchAutocompleteSubContainer.getChildCount() > 0){
                searchAutocompleteSubContainer.removeAllViews();
            }
        }

        void buildAutocompleteSearchItem(Place place, String mainText, String secondText){
            LinearLayout searchAutocompleteItemContainer = itemContainer(mainText, secondText);
            searchAutocompleteItemContainer.setTag(place);
            addClickListener(searchAutocompleteItemContainer);

            searchAutocompleteSubContainer.addView(searchAutocompleteItemContainer);
        }

        LinearLayout itemContainer(String mainText, String secondText){
            LinearLayout searchAutocompleteItemContainer = new LinearLayout(getActivity());
            searchAutocompleteItemContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            searchAutocompleteItemContainer.setOrientation(LinearLayout.HORIZONTAL);
            searchAutocompleteItemContainer.setBackgroundResource(R.drawable.bottom_border);

            int paddingLeftRight = 20;
            int paddingTopBottom = 10;
            searchAutocompleteItemContainer.setPadding(
                    Tools.pixelsToDP(paddingLeftRight, getResources()),
                    Tools.pixelsToDP(paddingTopBottom, getResources()),
                    Tools.pixelsToDP(paddingLeftRight, getResources()),
                    Tools.pixelsToDP(paddingTopBottom, getResources()));

            itemImageView(searchAutocompleteItemContainer);
            itemFullText(searchAutocompleteItemContainer, mainText, secondText);

            return searchAutocompleteItemContainer;
        }

        void itemImageView(LinearLayout searchAutocompleteItemContainer){
            ImageView icon = new ImageView(getActivity());
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            iconParams.gravity = Gravity.CENTER_VERTICAL;
            icon.setLayoutParams(iconParams);

            icon.setBackgroundResource(R.drawable.routing_search_auto_place_background);
            icon.setImageResource(R.drawable.ic_routing_search_auto_place);

            searchAutocompleteItemContainer.addView(icon);
        }

        void itemFullText(LinearLayout searchAutocompleteItemContainer, String mainText, String secondText){
            LinearLayout textContainer = new LinearLayout(getActivity());
            textContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textContainer.setOrientation(LinearLayout.VERTICAL);
            textContainer.setGravity(Gravity.CENTER_VERTICAL);

            int paddingLeftRight = 15;
            textContainer.setPadding(
                    Tools.pixelsToDP(paddingLeftRight, getResources()),
                    0,
                    Tools.pixelsToDP(paddingLeftRight, getResources()),
                    0);

            TextView mainTextView = itemMainText(mainText);
            TextView secondTextView = itemSecondText(secondText);

            textContainer.addView(mainTextView);
            textContainer.addView(secondTextView);

            searchAutocompleteItemContainer.addView(textContainer);
        }

        TextView itemMainText(String mainText){
            TextView mainTextView = new TextView(getActivity());

            LinearLayout.LayoutParams mainTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mainTextView.setLayoutParams(mainTextParams);
            mainTextView.setText(mainText);
            mainTextView.setTypeface(mainTextView.getTypeface(), Typeface.BOLD);
            mainTextView.setTextSize(18f);
            mainTextView.setGravity(Gravity.CENTER_VERTICAL);

            return mainTextView;
        }

        TextView itemSecondText(String secondText){
            TextView secondTextView = new TextView(getActivity());

            LinearLayout.LayoutParams mainTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            secondTextView.setLayoutParams(mainTextParams);
            secondTextView.setText(secondText);
            secondTextView.setTypeface(secondTextView.getTypeface(), Typeface.NORMAL);
            secondTextView.setTextSize(16f);
            secondTextView.setGravity(Gravity.CENTER_VERTICAL);

            return secondTextView;
        }

        void addClickListener(LinearLayout item){
            Place place = (Place) item.getTag();
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addTextToEditView();
                }

                void addTextToEditView(){
                    routingSearchFragment.updateEditText(place.getDescription());
                }
            });
        }
    }
}