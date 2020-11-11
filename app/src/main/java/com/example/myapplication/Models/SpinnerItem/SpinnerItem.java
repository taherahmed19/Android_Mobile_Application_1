package com.example.myapplication.Models.SpinnerItem;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplication.R;

public class SpinnerItem  {

    private String name;
    private int image;
    private int confirmation;

    public SpinnerItem(String name, int image){
        this.name = name;
        this.image = image;
        this.confirmation = R.drawable.empty;
    }

    public SpinnerItem(String name, int image, int confirmation){
        this.name = name;
        this.image = image;
        this.confirmation = confirmation;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(int confirmation) {
        this.confirmation = confirmation;
    }

}
