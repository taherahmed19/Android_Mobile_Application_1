package com.example.myapplication.Activities.RegionSelectorActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Models.Filter.Filter;
import com.example.myapplication.Models.FilterSettings.FilterSettings;
import com.example.myapplication.R;
import com.example.myapplication.Models.FilteredRegion.FilteredRegion;
import com.example.myapplication.HttpRequest.RegionGeolocation.RegionGeolocation;
import com.google.android.gms.maps.model.LatLng;

import java.text.MessageFormat;


public class RegionSelectorActivity extends AppCompatActivity  {

    RegionGeolocation geolocation;
    String[] regions = {"London", "Birmingham", "Manchester", "Liverpool"};
    String regionStateClicked = "";

    public static final String REGION_API = "https://api.opencagedata.com/geocode/v1/json?q={0}&key={1}&countrycode=GB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_selector);

        configureSelectedRegion();
        configureRegionsItems();
        configureCloseButton();
    }

    void configureSelectedRegion(){
        Intent intent = getIntent();
        this.regionStateClicked = intent.getStringExtra("selectedRegion");
    }

    void configureSubmitButtonDefault(){
        Button filterSubmit = (Button) findViewById(R.id.filterSubmit);
        filterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "No Region Selected";
                Toast.makeText(getApplicationContext(), str,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void configureSubmitButtonRegionSelected(final LatLng region, final String regionName){
        Button filterSubmit = (Button) findViewById(R.id.filterSubmit);
        filterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(region != null){
                    Intent intent = new Intent();
                    intent.putExtra("latitude", region.latitude);
                    intent.putExtra("longitude", region.longitude);
                    intent.putExtra("name", regionName);
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    Log.d("print", "Region selected = null" );
                }
            }
        });
    }

    void configureCloseButton(){
        TextView filterClose = (TextView) findViewById(R.id.filterClose);
        filterClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    void configureRegionsItems(){
        LinearLayout regionItems = (LinearLayout) findViewById(R.id.regionItems);

        FilterSettings filterSettings = Filter.retrieveFilterJson(this);
        FilteredRegion filteredRegion = filterSettings.getFilteredRegion();
        String selectedRegion = "";

        if(filteredRegion != null){
            selectedRegion = filteredRegion.getName();
        }

        for(int i = 0; i < regions.length; i++){
            RelativeLayout relativeLayout = new RelativeLayout(this);
            Button btn = new Button(this);
            String btnId = "region" + regions[i] + "Btn";

            styleRelativeLayout(relativeLayout);
            styleRegionButton(btn, regions[i]);

            configureRegionItemSelector(btn, relativeLayout, regionItems);

            relativeLayout.addView(btn);
            regionItems.addView(relativeLayout);

            if(!regionStateClicked.trim().isEmpty() && regionStateClicked.equals(regions[i])){
                addConfirmIcon(relativeLayout);
            }

            if(btn.getText().equals(selectedRegion)){
                addConfirmIcon(relativeLayout);
            }
        }
    }

    void configureRegionItemSelector(Button button, final RelativeLayout relativeLayout, final LinearLayout regionItems){
        final RegionSelectorActivity instance = this;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = (Button)view;
                String region = btn.getText().toString();
                String apiCall = MessageFormat.format(RegionSelectorActivity.REGION_API, region,getResources().getString(R.string.OPEN_CAGE_API_KEY));

                clearConfirmIcons(regionItems);
                addConfirmIcon(relativeLayout);

                geolocation = new RegionGeolocation(getApplicationContext(), instance);
                geolocation.execute(apiCall);
            }
        });
    }

    void styleRegionButton(Button button, String region){
        int sizeInDp = 15;

        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);

        button.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText(region);
        button.setTextSize(18f);
        button.setTextColor(getResources().getColor(R.color.regionButtonTextColour));
        button.setPadding(dpAsPixels,dpAsPixels,dpAsPixels,dpAsPixels);
        button.setAllCaps(false);
        button.setGravity(Gravity.START);
        button.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    void styleRelativeLayout(RelativeLayout relativeLayout){
        relativeLayout.setBackgroundResource(R.drawable.bottom_border);
    }

    void addConfirmIcon(RelativeLayout relativeLayout){
        int sizeInDp = 15;

        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);

        ImageView confirmImageView = new ImageView(this);
        RelativeLayout.LayoutParams imageParams;
        imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        imageParams.setMargins(0,0, dpAsPixels,0);

        confirmImageView.setLayoutParams(imageParams);
        confirmImageView.setImageResource(R.drawable.ic_region_confirm);

        relativeLayout.addView(confirmImageView);
    }

    void clearConfirmIcons(LinearLayout regionItems){
        for(int i = 0; i < regionItems.getChildCount(); i++){
            final View item = regionItems.getChildAt(i);

            ViewGroup subItem = (ViewGroup) item;

            if(subItem.getChildCount() == 2){
                if(subItem.getChildAt(1) instanceof ImageView){
                    subItem.removeView(subItem.getChildAt(1) );
                }
            }
        }
    }

}