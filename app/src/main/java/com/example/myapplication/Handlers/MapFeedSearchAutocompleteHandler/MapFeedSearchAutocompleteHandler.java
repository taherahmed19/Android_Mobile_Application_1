package com.example.myapplication.Handlers.MapFeedSearchAutocompleteHandler;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.Fragments.MapFeedSearchAutocompleteFragment.MapFeedSearchAutocompleteFragment;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Models.SharedViewModel.SharedViewModel;
import com.example.myapplication.R;
import com.example.myapplication.Refactor.searchAutocomplete.Place;
import com.example.myapplication.Utils.Tools.Tools;

public class MapFeedSearchAutocompleteHandler {

    MapFeedSearchAutocompleteFragment mapFeedSearchAutocompleteFragment;

    ScrollView searchAutocompleteContainer;
    LinearLayout searchAutocompleteSubContainer;
    MapFeedSearchAutocompleteFragment.FragmentAutocompleteListener listener;
    MapFragment mapFragment;

    public MapFeedSearchAutocompleteHandler(MapFeedSearchAutocompleteFragment mapFeedSearchAutocompleteFragment, MapFragment mapFragment) {
        this.mapFeedSearchAutocompleteFragment = mapFeedSearchAutocompleteFragment;
        this.mapFragment = mapFragment;
    }

    void addClickListener(LinearLayout item){

        Place place = (Place) item.getTag();
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTextToEditView();
            }

            void addTextToEditView(){
                listener.onInputAutocompleteSent(place.getDescription());
            }
        });
    }

    public void clearAutocomplete(){
        this.searchAutocompleteSubContainer = (LinearLayout) mapFeedSearchAutocompleteFragment.getView().findViewById(R.id.searchAutocompleteSubContainer);

        if(searchAutocompleteSubContainer.getChildCount() > 0){
            searchAutocompleteSubContainer.removeAllViews();
        }
    }

    public void buildAutocompleteSearchItem(Place place, String mainText, String secondText){
        this.searchAutocompleteContainer = (ScrollView) mapFeedSearchAutocompleteFragment.getView().findViewById(R.id.searchAutocompleteContainer);
        this.searchAutocompleteSubContainer = (LinearLayout) mapFeedSearchAutocompleteFragment.getView().findViewById(R.id.searchAutocompleteSubContainer);

        LinearLayout searchAutocompleteItemContainer = itemContainer(mainText, secondText);
        searchAutocompleteItemContainer.setTag(place);
        addClickListener(searchAutocompleteItemContainer);

        searchAutocompleteSubContainer.addView(searchAutocompleteItemContainer);
    }

    LinearLayout itemContainer(String mainText, String secondText){
        LinearLayout searchAutocompleteItemContainer = new LinearLayout(mapFeedSearchAutocompleteFragment.getActivity());
        searchAutocompleteItemContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        searchAutocompleteItemContainer.setOrientation(LinearLayout.HORIZONTAL);
        searchAutocompleteItemContainer.setBackgroundResource(R.drawable.bottom_border);

        int paddingLeftRight = 20;
        int paddingTopBottom = 10;
        searchAutocompleteItemContainer.setPadding(
                Tools.pixelsToDP(paddingLeftRight, mapFeedSearchAutocompleteFragment.getResources()),
                Tools.pixelsToDP(paddingTopBottom, mapFeedSearchAutocompleteFragment.getResources()),
                Tools.pixelsToDP(paddingLeftRight, mapFeedSearchAutocompleteFragment.getResources()),
                Tools.pixelsToDP(paddingTopBottom, mapFeedSearchAutocompleteFragment.getResources()));

        itemImageView(searchAutocompleteItemContainer);
        itemFullText(searchAutocompleteItemContainer, mainText, secondText);

        return searchAutocompleteItemContainer;
    }

    void itemImageView(LinearLayout searchAutocompleteItemContainer){
        ImageView icon = new ImageView(mapFeedSearchAutocompleteFragment.getActivity());
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        iconParams.gravity = Gravity.CENTER_VERTICAL;
        icon.setLayoutParams(iconParams);

        icon.setBackgroundResource(R.drawable.routing_search_auto_place_background);
        icon.setImageResource(R.drawable.ic_routing_search_auto_place);

        searchAutocompleteItemContainer.addView(icon);
    }

    void itemFullText(LinearLayout searchAutocompleteItemContainer, String mainText, String secondText){
        LinearLayout textContainer = new LinearLayout(mapFeedSearchAutocompleteFragment.getActivity());
        textContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textContainer.setOrientation(LinearLayout.VERTICAL);
        textContainer.setGravity(Gravity.CENTER_VERTICAL);

        int paddingLeftRight = 15;
        textContainer.setPadding(
                Tools.pixelsToDP(paddingLeftRight, mapFeedSearchAutocompleteFragment.getResources()),
                0,
                Tools.pixelsToDP(paddingLeftRight, mapFeedSearchAutocompleteFragment.getResources()),
                0);

        TextView mainTextView = itemMainText(mainText);
        TextView secondTextView = itemSecondText(secondText);

        textContainer.addView(mainTextView);
        textContainer.addView(secondTextView);

        searchAutocompleteItemContainer.addView(textContainer);
    }

    TextView itemMainText(String mainText){
        TextView mainTextView = new TextView(mapFeedSearchAutocompleteFragment.getActivity());

        LinearLayout.LayoutParams mainTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mainTextView.setLayoutParams(mainTextParams);
        mainTextView.setText(mainText);
        mainTextView.setTypeface(mainTextView.getTypeface(), Typeface.BOLD);
        mainTextView.setTextSize(18f);
        mainTextView.setGravity(Gravity.CENTER_VERTICAL);

        return mainTextView;
    }

    TextView itemSecondText(String secondText){
        TextView secondTextView = new TextView(mapFeedSearchAutocompleteFragment.getActivity());

        LinearLayout.LayoutParams mainTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        secondTextView.setLayoutParams(mainTextParams);
        secondTextView.setText(secondText);
        secondTextView.setTypeface(secondTextView.getTypeface(), Typeface.NORMAL);
        secondTextView.setTextSize(16f);
        secondTextView.setGravity(Gravity.CENTER_VERTICAL);

        return secondTextView;
    }

    public Context getContext(){
        return this.mapFeedSearchAutocompleteFragment.getContext();
    }

    public void setListener(MapFeedSearchAutocompleteFragment.FragmentAutocompleteListener listener) {
        this.listener = listener;
    }
}
