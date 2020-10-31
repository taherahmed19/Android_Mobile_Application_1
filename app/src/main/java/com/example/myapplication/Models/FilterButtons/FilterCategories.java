package com.example.myapplication.Models.FilterButtons;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

public class FilterCategories {

    ArrayList<FilterButton> filterButtons;
    FragmentActivity fragmentActivity;

    public FilterCategories(FragmentActivity fragmentActivity){
        this.filterButtons = new ArrayList<FilterButton>();
        this.fragmentActivity = fragmentActivity;
    }

    public void addButton(FilterButton filterButton){
        this.filterButtons.add(filterButton);
    }

    public void removeButton(FilterButton filterButton){
        this.filterButtons.remove(filterButton);
    }

    public ArrayList<FilterButton> getFilterButtons() {
        return filterButtons;
    }

    public void setFilterButtons(ArrayList<FilterButton> filterButtons) {
        this.filterButtons = filterButtons;
    }
}
