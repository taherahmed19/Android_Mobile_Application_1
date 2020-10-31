package com.example.myapplication.Models.FilterSettings;

import com.example.myapplication.Models.FilteredRegion.FilteredRegion;

import java.util.ArrayList;

public class FilterSettings {

    FilteredRegion filteredRegion;
    ArrayList<Integer> categories;

    public FilterSettings(FilteredRegion filteredRegion, ArrayList<Integer> categories) {
        this.filteredRegion = filteredRegion;
        this.categories = categories;
    }

    public FilteredRegion getFilteredRegion() {
        return filteredRegion;
    }

    public void setFilteredRegion(FilteredRegion filteredRegion) {
        this.filteredRegion = filteredRegion;
    }

    public ArrayList<Integer> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Integer> categories) {
        this.categories = categories;
    }
}
