package com.example.myapplication.Presenters.SearchAutocompletePresenter;

import com.example.myapplication.Interfaces.SearchAutocompleteContract.SearchAutocompleteContract;

public class SearchAutocompletePresenter implements SearchAutocompleteContract.Presenter {

    SearchAutocompleteContract.View view;

    public SearchAutocompletePresenter(SearchAutocompleteContract.View view) {
        this.view = view;
    }
}
