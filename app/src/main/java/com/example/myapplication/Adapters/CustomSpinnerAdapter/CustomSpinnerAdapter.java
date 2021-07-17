package com.example.myapplication.Adapters.CustomSpinnerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Models.SpinnerItem.SpinnerItem;
import com.example.myapplication.R;

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
        TextView dropdownText = convertView.findViewById(R.id.spinnerText);

        if(item != null){
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
        TextView text = convertView.findViewById((R.id.spinnerText));
        LinearLayout customSpinnerItem = convertView.findViewById(R.id.customSpinnerItem);

        if(item != null){
            text.setText(item.getName());
            customSpinnerItem.setBackgroundResource(R.drawable.bottom_border);
        }

        return convertView;
    }

    public static ArrayList<SpinnerItem> CreateMarkerSpinnerItems(Context context){
        ArrayList<SpinnerItem> list = new ArrayList<>();

        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_1)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_2)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_3)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_4)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_5)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_6)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_7)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_8)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_9)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_10)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_11)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_12)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_13)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_14)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_15)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_16)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_17)));
        list.add(new SpinnerItem(context.getString(R.string.form_spinner_item_18)));

        return list;
    }
}