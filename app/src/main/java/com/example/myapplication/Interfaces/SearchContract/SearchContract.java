package com.example.myapplication.Interfaces.SearchContract;

import android.text.Editable;
import android.widget.EditText;

public interface SearchContract {

    interface View{
        void handleTextSearch(EditText mapFeedSearch, Editable s);
        void handleSearchClose();
        void handleCurrentLocationButton();
    }

    interface Presenter{
    }

    interface Model{
    }

}
