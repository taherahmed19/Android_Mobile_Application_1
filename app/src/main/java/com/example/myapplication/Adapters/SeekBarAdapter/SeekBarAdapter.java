package com.example.myapplication.Adapters.SeekBarAdapter;

import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarAdapter {

    final int MAX_VALUE = 100;
    final int MIN_VALUE = 0;

    SeekBar seekBar;
    int progress;

    TextView maxValueText;

    public SeekBarAdapter(SeekBar seekBar, TextView maxValueText){
        this.seekBar = seekBar;
        this.progress = MIN_VALUE;
        this.maxValueText = maxValueText;
        this.setup();
    }

    void setup(){
        this.seekBar.setMax(MAX_VALUE);
        listener();
    }

    void listener(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressChanged, boolean b) {
                progress = progressChanged;
                maxValueText.setText(progress + " mi");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
