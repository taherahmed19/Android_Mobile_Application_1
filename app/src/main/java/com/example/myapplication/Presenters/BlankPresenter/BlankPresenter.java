package com.example.myapplication.Presenters.BlankPresenter;

import com.example.myapplication.Interfaces.BlankContract.BlankContract;

public class BlankPresenter implements BlankContract.Presenter {

    BlankContract.View view;

    public BlankPresenter(BlankContract.View view) {
        this.view = view;
    }

    public void loadStartActivity(){
        view.loadStartActivity();
    }
}
