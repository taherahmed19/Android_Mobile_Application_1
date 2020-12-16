package com.example.myapplication.Models.FormFragmentSpinner;

import android.view.View;
import android.widget.AdapterView;

import com.example.myapplication.Fragments.FormFragment.FormFragment;

public class FormFragmentSpinner {

    FormFragment formFragment;

    public FormFragmentSpinner(FormFragment formFragment) {
        this.formFragment = formFragment;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public void onNothingSelected(AdapterView<?> adapterView) { }
}
