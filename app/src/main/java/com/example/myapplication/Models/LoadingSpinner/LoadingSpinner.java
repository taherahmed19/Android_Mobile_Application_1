package com.example.myapplication.Models.LoadingSpinner;

import android.view.View;
import android.widget.ProgressBar;

public class LoadingSpinner {

    ProgressBar progressBar;

    public LoadingSpinner(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void hide(){
        this.progressBar.setVisibility(View.GONE);
    }

    public void show(){
        this.progressBar.setVisibility(View.VISIBLE);
    }
}
