package com.example.myapplication.Refactor.searchAutocomplete;

import java.util.ArrayList;

public class Place{

    String description;
    String mainText;
    String secondText;
    ArrayList types;

    public Place(String description, ArrayList types, String mainText, String secondText) {
        this.description = description;
        this.types = types;
        this.mainText = mainText;
        this.secondText = secondText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList getTypes() {
        return types;
    }

    public void setTypes(ArrayList types) {
        this.types = types;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getSecondText() {
        return secondText;
    }

    public void setSecondText(String secondText) {
        this.secondText = secondText;
    }
}
