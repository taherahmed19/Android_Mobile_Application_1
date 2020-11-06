package com.example.myapplication.Adapters.CustomFilterSpinnerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.Models.SpinnerItem.SpinnerItem;

import java.util.ArrayList;

public class CustomFilterSpinnerAdapter extends ArrayAdapter<SpinnerItem> {

    public CustomFilterSpinnerAdapter(@NonNull Context context, ArrayList<SpinnerItem> customList) {
        super(context, 0, customList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_categories, parent, false);
        }


        SpinnerItem item = getItem(position);
        ImageView dropdownImage = convertView.findViewById(R.id.spinnerImage);
        TextView dropdownText = convertView.findViewById(R.id.spinnerText);

        if(item != null){
            dropdownImage.setImageResource(item.getImage());
            dropdownText.setText(item.getName());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_categories, parent, false);
        }

        SpinnerItem item = getItem(position);
        ImageView image = convertView.findViewById((R.id.spinnerImage));
        TextView text = convertView.findViewById((R.id.spinnerText));

        if(item != null){
            image.setImageResource(item.getImage());
            text.setText(item.getName());
        }

        return convertView;
    }
}
