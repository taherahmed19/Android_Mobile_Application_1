package com.example.myapplication.Models.FilterButtons;

import android.widget.Button;

public class FilterButton {

    Button button;
    Boolean clicked;
    int id;

    public FilterButton(Button button){
        this.button = button;
        this.clicked = false;
        this.id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Boolean getClicked() {
        return clicked;
    }

    public void setClicked(Boolean clicked) {
        this.clicked = clicked;
    }
}
