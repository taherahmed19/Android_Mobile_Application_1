package com.example.myapplication.Fragments.SearchAutocompleteFragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Interfaces.FragmentAutocompleteListener.FragmentAutocompleteListener;
import com.example.myapplication.Interfaces.SearchAutocompleteContract.SearchAutocompleteContract;
import com.example.myapplication.Models.Place.Place;
import com.example.myapplication.Presenters.SearchAutocompletePresenter.SearchAutocompletePresenter;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Tools.Tools;

public class SearchAutocompleteFragment extends Fragment implements SearchAutocompleteContract.View {

    public final static String TAG = SearchAutocompleteFragment.class.getName();

    SearchAutocompletePresenter searchAutocompletePresenter;
    FragmentAutocompleteListener listener;

    ScrollView searchAutocompleteContainer;
    LinearLayout searchAutocompleteSubContainer;

    public SearchAutocompleteFragment(FragmentAutocompleteListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchAutocompletePresenter = new SearchAutocompletePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_map_feed_search_autocomplete, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
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
        this.searchAutocompleteSubContainer = (LinearLayout) getView().findViewById(R.id.searchAutocompleteSubContainer);

        if(searchAutocompleteSubContainer.getChildCount() > 0){
            searchAutocompleteSubContainer.removeAllViews();
        }
    }

    public void buildAutocompleteSearchItem(Place place, String mainText, String secondText){
        this.searchAutocompleteContainer = (ScrollView) getView().findViewById(R.id.searchAutocompleteContainer);
        this.searchAutocompleteSubContainer = (LinearLayout) getView().findViewById(R.id.searchAutocompleteSubContainer);

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
                Tools.pixelsToDP(paddingLeftRight, getResources().getDisplayMetrics().density),
                Tools.pixelsToDP(paddingTopBottom, getResources().getDisplayMetrics().density),
                Tools.pixelsToDP(paddingLeftRight, getResources().getDisplayMetrics().density),
                Tools.pixelsToDP(paddingTopBottom, getResources().getDisplayMetrics().density));

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
                Tools.pixelsToDP(paddingLeftRight, getResources().getDisplayMetrics().density),
                0,
                Tools.pixelsToDP(paddingLeftRight, getResources().getDisplayMetrics().density),
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

    public Context getContext(){
        return this.getContext();
    }
}