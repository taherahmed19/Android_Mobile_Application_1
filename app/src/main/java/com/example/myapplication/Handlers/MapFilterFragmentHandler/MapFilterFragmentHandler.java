package com.example.myapplication.Handlers.MapFilterFragmentHandler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Activities.RegionSelectorActivity.RegionSelectorActivity;
import com.example.myapplication.Adapters.CustomFilterSpinnerAdapter.CustomFilterSpinnerAdapter;
import com.example.myapplication.Adapters.SeekBarAdapter.SeekBarAdapter;
import com.example.myapplication.Fragments.MapFilterFragment.MapFilterFragment;
import com.example.myapplication.Models.Filter.FilterSortBy.FilterSortBy;
import com.example.myapplication.Models.FilteredRegion.FilteredRegion;
import com.example.myapplication.Models.Settings.Settings;
import com.example.myapplication.Models.SpinnerItem.SpinnerItem;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Objects;

public class MapFilterFragmentHandler {

    MapFilterFragment mapFilterFragment;

    FilteredRegion filteredRegion;

    ViewPager viewPager;
    FilterSortBy filterSortBy;
    MapFilterFragment.FragmentMapFilterListener listener;

    ArrayList<SpinnerItem> selectedItemsSaved;

    public MapFilterFragmentHandler(MapFilterFragment mapFilterFragment, ViewPager viewPager) {
        this.mapFilterFragment = mapFilterFragment;
        this.viewPager = viewPager;
        this.filterSortBy = new FilterSortBy();
        this.selectedItemsSaved = new ArrayList<>();
    }

    public void configure(){
        configureSubmitButton();
        configureFormCloseButton();
        configureSeekBar();
        configureCategorySpinner();
    }

    public void configureCategorySpinner(){
        Spinner spinner = mapFilterFragment.getView().findViewById(R.id.filterSpinner);
        ArrayList<SpinnerItem> customList = createMarkerSpinnerItems();
        CustomFilterSpinnerAdapter adapter = new CustomFilterSpinnerAdapter(Objects.requireNonNull(mapFilterFragment.getActivity()), customList);

        spinner.setAdapter(adapter);
        spinner.setDropDownVerticalOffset(125);
        spinnerListener(spinner);
    }

    void spinnerListener(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position != 0) {
                    SpinnerItem item = (SpinnerItem) spinner.getSelectedItem();

                    ArrayList<String> tempItems = filterSortBy.getTempItems();
                    ArrayList<String> selectedItems = filterSortBy.getSelectedItems();

                    if (!tempItems.contains(item.getName()) && !selectedItems.contains(item.getName())) {
                        item.setConfirmation(R.drawable.ic_map_filter_confirm);
                        filterSortBy.addTempItem(item.getName());
                        selectedItemsSaved.add(item);
                    } else {
                        item.setConfirmation(R.drawable.empty);
                        filterSortBy.removeTempItem(item.getName());
                        selectedItemsSaved.remove(item);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }

        });
    }

    public void configureRegionSelector(){
        RelativeLayout regionSelector = (RelativeLayout) mapFilterFragment.getView().findViewById(R.id.regionSelectorActivity);
        regionSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedRegion = "";
                if(filteredRegion != null){
                    selectedRegion = filteredRegion.getName();
                }

                Intent intent = new Intent(mapFilterFragment.getActivity(), RegionSelectorActivity.class);
                intent.putExtra("selectedRegion", selectedRegion);

                mapFilterFragment.startActivityForResult(intent,1);
            }
        });
    }

    public void configureSeekBar(){
        SeekBar seekBar = (SeekBar) mapFilterFragment.getView().findViewById(R.id.filterSeekBar);
        TextView maxValueText = (TextView) mapFilterFragment.getView().findViewById(R.id.maxValueText);

        SeekBarAdapter seekBarAdapter = new SeekBarAdapter(seekBar,maxValueText);
    }

    public void configureFormCloseButton(){
        final TextView formClose = (TextView) Objects.requireNonNull(mapFilterFragment.getView()).findViewById(R.id.formClose);
        formClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> selectedItems = filterSortBy.getSelectedItems();
                ArrayList<String> tempItems = filterSortBy.getTempItems();

                for (String spinnerItem : tempItems) {
                    if(!selectedItems.contains(spinnerItem)){
                        tempItems.remove(spinnerItem);
                    }
                }
                mapFilterFragment.getParentFragmentManager().popBackStack();
            }
        });
    }

    public void configureSubmitButton(){
        Button mapFeedSubmit = (Button) mapFilterFragment.getView().findViewById(R.id.mapFilterSubmit);
        mapFeedSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
//                viewPager.getAdapter().notifyDataSetChanged(); //add to new post submission
                mapFilterFragment.getParentFragmentManager().popBackStack();
            }
        });
    }

    public void updateRegionLocationName(String region){
        TextView regionName = (TextView) mapFilterFragment.getView().findViewById(R.id.regionName);
        regionName.setText(region);
    }

    public void submit(){
        ArrayList<String> selectedItems = filterSortBy.getSelectedItems();
        ArrayList<String> tempItems = filterSortBy.getTempItems();

        for (String spinnerItem : tempItems) {
            if(!selectedItems.contains(spinnerItem)){
                selectedItems.add(spinnerItem);
            }
        }

        ArrayList<String> itemsToRemove = new ArrayList<>();
        for (String spinnerItem : selectedItems) {
            if(!tempItems.contains(spinnerItem)){
                itemsToRemove.add(spinnerItem);
            }
        }

        if(itemsToRemove.size() > 0 ){
            selectedItems.removeAll(itemsToRemove);
        }

        listener.onSettingsUpdated(new Settings((filterSortBy)));
    }

    ArrayList<SpinnerItem> createMarkerSpinnerItems(){
        ArrayList<SpinnerItem> list = new ArrayList<>();

        list.add(new SpinnerItem(mapFilterFragment.getString(R.string.form_spinner_item_1), R.drawable.ic_marker));
        list.add(new SpinnerItem(mapFilterFragment.getString(R.string.form_spinner_item_2), R.drawable.ic_marker_green));
        list.add(new SpinnerItem(mapFilterFragment.getString(R.string.form_spinner_item_3), R.drawable.ic_marker_blue));
        list.add(new SpinnerItem(mapFilterFragment.getString(R.string.form_spinner_item_4), R.drawable.ic_marker_purple));

        return list;
    }

    public void setListener(MapFilterFragment.FragmentMapFilterListener listener) {
        this.listener = listener;
    }

    public ArrayList<SpinnerItem> getSelectedItemsSaved() {
        return selectedItemsSaved;
    }
}
