package com.example.myapplication.Fragments.ErrorFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

public class ErrorFragment extends Fragment {

    Fragment fragment;
    String titleMessage;
    String bodyMessage;

    public ErrorFragment(Fragment fragment, String titleMessage, String bodyMessage) {
        this.fragment = fragment;
        this.titleMessage = titleMessage;
        this.bodyMessage = bodyMessage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureTitle();
        configureBody();
        configureErrorButton();
    }

    void configureTitle(){
        TextView errorFragmentTitle = (TextView) getView().findViewById(R.id.errorFragmentTitle);
        errorFragmentTitle.setText(this.titleMessage);
    }

    void configureBody(){
        TextView errorFragmentBody = (TextView) getView().findViewById(R.id.errorFragmentBody);
        errorFragmentBody.setText(this.bodyMessage);
    }

    void configureErrorButton(){
        Button errorBtn = (Button) getView().findViewById(R.id.errorBtn);
        errorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }
}