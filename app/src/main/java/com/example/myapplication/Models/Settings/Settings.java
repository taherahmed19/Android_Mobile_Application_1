package com.example.myapplication.Models.Settings;

import com.example.myapplication.Models.Filter.FilterSortBy.FilterSortBy;

public class Settings {

    FilterSortBy filterSortBy;

    public Settings(FilterSortBy filterSortBy) {
        this.filterSortBy = filterSortBy;
    }

    public FilterSortBy getFilterSortBy() {
        return filterSortBy;
    }
}
