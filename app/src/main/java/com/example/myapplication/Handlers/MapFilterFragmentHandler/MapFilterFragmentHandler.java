package com.example.myapplication.Handlers.MapFilterFragmentHandler;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Adapters.CustomFilterSpinnerAdapter.CustomFilterSpinnerAdapter;
import com.example.myapplication.Adapters.CustomSpinnerAdapter.CustomSpinnerAdapter;
import com.example.myapplication.Adapters.SeekBarAdapter.SeekBarAdapter;
import com.example.myapplication.Fragments.MapFilterFragment.MapFilterFragment;
import com.example.myapplication.Models.Filter.FilterSortBy.FilterSortBy;
import com.example.myapplication.Models.FilteredRegion.FilteredRegion;
import com.example.myapplication.Models.Settings.Settings;
import com.example.myapplication.Models.SpinnerItem.SpinnerItem;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Tools.Tools;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class MapFilterFragmentHandler {

    MapFilterFragment mapFilterFragment;

    ViewPager viewPager;
    FilterSortBy filterSortBy;
    MapFilterFragment.FragmentMapFilterListener listener;

    public MapFilterFragmentHandler(MapFilterFragment mapFilterFragment, ViewPager viewPager) {
        this.mapFilterFragment = mapFilterFragment;
        this.viewPager = viewPager;
        this.filterSortBy = new FilterSortBy();
    }

    public void configure(){
        getSettingsItems();
        configureSubmitButton();
        configureFormCloseButton();
        configureSeekBar();
        configureCategorySpinner();
    }

    public void configureCategorySpinner(){
        Spinner spinner = mapFilterFragment.getView().findViewById(R.id.filterSpinner);
        ArrayList<SpinnerItem> customList = CustomSpinnerAdapter.CreateMarkerSpinnerItems(mapFilterFragment.getContext());
        CustomFilterSpinnerAdapter adapter = new CustomFilterSpinnerAdapter(Objects.requireNonNull(mapFilterFragment.getActivity()), customList);

        spinner.setAdapter(adapter);
        spinner.setDropDownVerticalOffset(125);
        spinnerListener(spinner);
        updateDrawableSelectedSpinnerItems(adapter);
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

                ArrayList<String> itemsToRemove = new ArrayList<>();

                for (String spinnerItem : tempItems) {
                    if(!selectedItems.contains(spinnerItem)){
                        itemsToRemove.add(spinnerItem);
                    }
                }

                for (String itemToRemove : itemsToRemove) {
                    tempItems.remove(itemToRemove);
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

        saveSettingsSharedPreference();

        listener.onSettingsUpdated(new Settings((filterSortBy)));
    }

    public void setListener(MapFilterFragment.FragmentMapFilterListener listener) {
        this.listener = listener;
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
                    } else {
                        item.setConfirmation(R.drawable.empty);
                        filterSortBy.removeTempItem(item.getName());
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }

        });
    }

    void updateDrawableSelectedSpinnerItems(CustomFilterSpinnerAdapter adapter){
        ArrayList<String> selectedItems = filterSortBy.getSelectedItems();
        int spinnerItemsCount = adapter.getCount();
        int selectedItemsCount = selectedItems.size();

        for(int i = 0; i < spinnerItemsCount; i++){
            if(i != 0){
                SpinnerItem item = adapter.getItem(i);
                for(int j = 0; j < selectedItemsCount; j++){
                    if(item.getName().equals(selectedItems.get(j))){
                        item.setConfirmation(R.drawable.ic_map_filter_confirm);
                    }
                }
            }
        }
    }

    void saveSettingsSharedPreference(){
        ArrayList<String> selectedItems = filterSortBy.getSelectedItems();
        ArrayList<String> tempItems = filterSortBy.getTempItems();

        String json = Tools.ArrayListToJson(selectedItems);

        SharedPreferences settingsPreference = this.mapFilterFragment.getContext().getSharedPreferences("Map_Filter_Fragment_Settings", 0);
        SharedPreferences.Editor preferenceEditor = settingsPreference.edit();
        preferenceEditor.putString("settingsJson", json);
        preferenceEditor.apply();

        Log.d("Print", "Settings Shared Preference " + settingsPreference.getString("settingsJson", "default"));
    }

    void getSettingsItems(){
        SharedPreferences settingsPreference = this.mapFilterFragment.getContext().getSharedPreferences("Map_Filter_Fragment_Settings", 0);
        String json = settingsPreference.getString("settingsJson", "empty");

        Log.d("Print", "Get settings saved " + json);

        ArrayList<String> selectedItems = filterSortBy.getSelectedItems();
        ArrayList<String> tempItems = filterSortBy.getTempItems();

        if(!json.equals("empty")){
            try {
                ArrayList<String> spinnerItems = Tools.JsonToArrayList(json);

                for(String item : spinnerItems){
                    selectedItems.add(item);
                    tempItems.add(item);
                    Log.d("Print", "Added items " + item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
