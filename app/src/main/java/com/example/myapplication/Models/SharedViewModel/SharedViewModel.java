package com.example.myapplication.Models.SharedViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<String> text = new MutableLiveData<>();

    public void setText(String input) {
        text.setValue(input);
    }

    public LiveData<String> getText() {
        return text;
    }

    public void clear(){
        onCleared();
    }
}
