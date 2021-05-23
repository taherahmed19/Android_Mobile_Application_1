package com.example.myapplication.Fragments.FeedFragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.HttpRequest.HttpMap.HttpMap;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Tools.Tools;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.LoadingSpinner.LoadingSpinner;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    HttpMap httpMap;
    UserAlertsFragmentElements userAlertsFragmentElements;
    LoadingSpinner loadingSpinner;

    public FeedFragment(){
        this.httpMap = null;
        this.userAlertsFragmentElements = new UserAlertsFragmentElements();
        this.loadingSpinner = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_feed, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.loadingSpinner = new LoadingSpinner((ProgressBar) getView().findViewById(R.id.feedLoadingSpinner));

    }

    public void renderFeed(ArrayList<Marker> markers){
        for(int i = 0; i < markers.size(); i++){
            Marker marker = markers.get(i);
//            userAlertsFragmentElements.createFeedItem(marker.getMarker(), marker.getDescription(), marker.getLat(), marker.getLng());
        }
    }

    private class UserAlertsFragmentElements{

        void createFeedItem(int markerType, String description, String lat, String lng){
            double latitude = Double.parseDouble(lat);
            double longitude = Double.parseDouble(lng);

            LinearLayout feedContainer = (LinearLayout) getView().findViewById(R.id.feedContainer);


            styleFeedItem(markerType, description, latitude, longitude, feedContainer);
        }

        void styleFeedItem(int markerType, String description, Double latitude, Double longitude, LinearLayout feedContainer){
            RelativeLayout feedItemLayout = new RelativeLayout(getActivity());
            int feedItemLayoutPadding = Tools.pixelsToDP(10, getActivity().getResources());
            feedItemLayout.setPadding(feedItemLayoutPadding, feedItemLayoutPadding, feedItemLayoutPadding, feedItemLayoutPadding);
            feedItemLayout.setBackgroundResource(R.drawable.bottom_border);
            feedItemLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            LinearLayout feedItemListLayout = new LinearLayout(getActivity());
            int feedItemListLayoutWidth = Tools.pixelsToDP(300, getActivity().getResources());
            feedItemListLayout.setLayoutParams(new LinearLayout.LayoutParams(feedItemListLayoutWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
            feedItemListLayout.setOrientation(LinearLayout.VERTICAL);


            String category = categoryPicker(markerType);
            int feedItemTitleBottomMargin = Tools.pixelsToDP(5, getActivity().getResources());
            LinearLayout.LayoutParams feedItemTitleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            feedItemTitleParams.setMargins(0,0,0,feedItemTitleBottomMargin);

            TextView feedItemTitle = new TextView(getActivity());
            feedItemTitle.setLayoutParams(feedItemTitleParams);
            feedItemTitle.setText(category);
            feedItemTitle.setTypeface(feedItemTitle.getTypeface(), Typeface.BOLD);
            feedItemTitle.setTextSize(18f);
            feedItemTitle.setTextColor(Color.parseColor("#000000"));

            TextView feedItemDescription = new TextView(getActivity());
            feedItemDescription.setLayoutParams(feedItemTitleParams);
            feedItemDescription.setText(description);
            feedItemDescription.setTextSize(16f);
            feedItemDescription.setTextColor(Color.parseColor("#000000"));
            feedItemDescription.setMaxLines(1);
            feedItemDescription.setEllipsize(TextUtils.TruncateAt.END);

            String feedItemTimestampText = "00:00:00 - username";
            TextView feedItemTimestamp = new TextView(getActivity());
            feedItemTimestamp.setLayoutParams(feedItemTitleParams);
            feedItemTimestamp.setText(feedItemTimestampText);
            feedItemTimestamp.setTextSize(14f);

            int icon = Tools.MarkerPicker(markerType);
            ImageView feedItemIcon = new ImageView(getActivity());
            RelativeLayout.LayoutParams feedItemIconParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            feedItemIconParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            feedItemIconParams.addRule(RelativeLayout.CENTER_VERTICAL);
            feedItemIcon.setLayoutParams(feedItemIconParams);
            feedItemIcon.setBackgroundResource(icon);

            feedItemListLayout.addView(feedItemTitle);
            feedItemListLayout.addView(feedItemDescription);
            feedItemListLayout.addView(feedItemTimestamp);

            feedItemLayout.addView(feedItemListLayout);
            feedItemLayout.addView(feedItemIcon);
            feedContainer.addView(feedItemLayout);
        }

        String categoryPicker(int category){
            String categoryChosen = "";
            switch (category) {
                case 1:
                    categoryChosen = "Environment";
                    break;
                case 2:
                    categoryChosen = "Weather";
                    break;
                case 3:
                    categoryChosen = "People";
                    break;
                default:
                    categoryChosen = "None";
                    break;
            }

            return categoryChosen;
        }

    }

}