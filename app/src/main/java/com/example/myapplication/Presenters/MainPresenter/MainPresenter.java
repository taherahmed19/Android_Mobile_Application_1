package com.example.myapplication.Presenters.MainPresenter;

import com.example.myapplication.Interfaces.MainContract.MainContract;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;

    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

//    public void handleBackPressed(){
//        view.handleBackPressed();
//    }

//    public void handleBackPressMapFragments(Fragment fragment){
//        view.handleBackPressMapFragments(fragment);
//    }

}
