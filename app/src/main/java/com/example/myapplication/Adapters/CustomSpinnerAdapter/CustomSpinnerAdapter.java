package com.example.myapplication.Adapters.CustomSpinnerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.Models.SpinnerItem.SpinnerItem;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends ArrayAdapter<SpinnerItem> {

    public CustomSpinnerAdapter(@NonNull Context context, ArrayList<SpinnerItem> customList) {
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

    public static ArrayList<SpinnerItem> CreateMarkerSpinnerItems(Context context){
        ArrayList<SpinnerItem> list = new ArrayList<>();

        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_1), R.drawable.ic_marker));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_2), R.drawable.ic_marker_green));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_3), R.drawable.ic_marker_blue));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_4), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_5), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_6), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_7), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_8), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_9), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_10), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_11), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_12), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_13), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_14), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_15), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_16), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_17), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_18), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_19), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_20), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_21), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_22), R.drawable.ic_marker_purple));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_23), R.drawable.ic_marker_purple));

        return list;
    }
}