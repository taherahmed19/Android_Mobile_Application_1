package com.example.myapplication.Models.Filter.FilterSortBy;

import android.widget.Button;

import com.example.myapplication.Models.SpinnerItem.SpinnerItem;

import java.util.ArrayList;

public class FilterSortBy {

    ArrayList<String> tempItems;
    ArrayList<String> selectedItems;

    public FilterSortBy() {
        this.tempItems = new ArrayList<>();
        this.selectedItems = new ArrayList<>();
    }

    public void addTempItem(String spinnerItem){
        this.tempItems.add(spinnerItem);
    }

    public void removeTempItem(String spinnerItem){
        this.tempItems.remove(spinnerItem);
    }

    public ArrayList<String> getTempItems() {
        return tempItems;
    }

    public void addItemSelected(String spinnerItem){
        this.selectedItems.add(spinnerItem);
    }

    public void removeItemSelected(String spinnerItem){
        this.selectedItems.remove(spinnerItem);
    }

    public ArrayList<String> getSelectedItems() {
        return selectedItems;
    }
}
