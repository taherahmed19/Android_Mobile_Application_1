package com.example.myapplication.Models.SpinnerItem;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplication.R;

public class SpinnerItem implements Parcelable {

    private String name;
    private int image;
    private int confirmation;

    public SpinnerItem(String name, int image){
        this.name = name;
        this.image = image;
        this.confirmation = R.drawable.empty;
    }

    protected SpinnerItem(Parcel in) {
        name = in.readString();
        image = in.readInt();
        confirmation = in.readInt();
    }

    public static final Creator<SpinnerItem> CREATOR = new Creator<SpinnerItem>() {
        @Override
        public SpinnerItem createFromParcel(Parcel in) {
            return new SpinnerItem(in);
        }

        @Override
        public SpinnerItem[] newArray(int size) {
            return new SpinnerItem[size];
        }
    };

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



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(image);
        parcel.writeInt(confirmation);
    }
}
