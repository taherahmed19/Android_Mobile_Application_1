package com.example.myapplication.Refactor;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.Refactor.searchAutocomplete.PlacesFinder;

import static android.app.Activity.RESULT_OK;

public class RoutingSearchFragment extends Fragment {

    ImageButton routingSearchClose;
    EditText routingSearchFrom;
    EditText routingSearchWhere;

    RoutingSearchFragmentElements routingSearchFragmentElements;
    PlacesFinder places;
    RoutingSearchAutocompleteFragment routingSearchAutocompleteFragment;

    public RoutingSearchFragment(RoutingSearchAutocompleteFragment routingSearchAutocompleteFragment) {
        this.routingSearchAutocompleteFragment = routingSearchAutocompleteFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_routing_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.routingSearchFragmentElements = new RoutingSearchFragmentElements();

        this.routingSearchClose = (ImageButton) getView().findViewById(R.id.routingSearchClose);
        this.routingSearchFrom = (EditText) getView().findViewById(R.id.routingSearchFrom);
        this.routingSearchWhere = (EditText) getView().findViewById(R.id.routingSearchWhere);

        this.routingSearchFragmentElements.configureRoutingSearchSubmit();
        this.routingSearchFragmentElements.configureRoutingSearchClose();
        this.routingSearchFragmentElements.configureEditText();
    }

    public void updateEditText(String input){
        if(routingSearchWhere.hasFocus()){
            routingSearchWhere.setText(input, TextView.BufferType.EDITABLE);
        }else if(routingSearchFrom.hasFocus()){
            routingSearchFrom.setText(input, TextView.BufferType.EDITABLE);

        }
    }

    private class RoutingSearchFragmentElements{

        void configureRoutingSearchSubmit(){
            ImageButton routingSearchSubmit = (ImageButton) getView().findViewById(R.id.routingSearchSubmit);
            routingSearchSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!TextUtils.isEmpty(routingSearchFrom.getText()) && !TextUtils.isEmpty(routingSearchWhere.getText())){
                        String fromText = routingSearchFrom.getText().toString();
                        String whereText = routingSearchWhere.getText().toString();

                        Log.d("Print", "From text = " + fromText);
                        Log.d("Print", "Where text = " + whereText);

                        Intent intent = new Intent(getContext(), RoutingSearchFragment.class);
                        intent.putExtra("from", fromText);
                        intent.putExtra("where", whereText);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
                        getFragmentManager().popBackStack();
                        getFragmentManager().popBackStack();
                    }

                }
            });
        }

        void configureRoutingSearchClose(){
            routingSearchClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                    getActivity().onBackPressed();
                }
            });
        }

        void configureEditText(){
            routingSearchFrom.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            routingSearchWhere.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            routingSearchFrom.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                    routingSearchFrom.clearComposingText();
                    routingSearchAutocompleteFragment.clearAutocomplete();
                    places = new PlacesFinder(getContext(), routingSearchAutocompleteFragment);
                    places.execute(s.toString());
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });

            routingSearchWhere.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                    routingSearchWhere.clearComposingText();
                    routingSearchAutocompleteFragment.clearAutocomplete();
                    places = new PlacesFinder(getContext(), routingSearchAutocompleteFragment);
                    places.execute(s.toString());
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
        }
    }
}
