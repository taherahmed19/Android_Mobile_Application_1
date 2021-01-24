package com.example.myapplication.Models.SpinnerItem;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplication.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class SpinnerItem  {

    private String name;
    private int confirmation;

    public SpinnerItem(String name){
        this.name = name;
        this.confirmation = R.drawable.empty;
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
