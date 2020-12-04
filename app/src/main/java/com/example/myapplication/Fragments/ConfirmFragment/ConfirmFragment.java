package com.example.myapplication.Fragments.ConfirmFragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

public class ConfirmFragment extends Fragment {

    Fragment fragment;
    Activity activity;
    String titleMessage;
    String bodyMessage;

    public ConfirmFragment(Activity activity, String titleMessage, String bodyMessage) {
        this.activity = activity;
        this.titleMessage = titleMessage;
        this.bodyMessage = bodyMessage;
    }

    public ConfirmFragment(Fragment fragment, String titleMessage, String bodyMessage) {
        this.fragment = fragment;
        this.titleMessage = titleMessage;
        this.bodyMessage = bodyMessage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureConfirmButton();
        configureTitle();
        configureBody();
    }

    void configureTitle(){
        TextView confirmFragmentTitle = (TextView) getView().findViewById(R.id.confirmFragmentTitle);
        confirmFragmentTitle.setText(this.titleMessage);
    }

    void configureBody(){
        TextView confirmFragmentBody = (TextView) getView().findViewById(R.id.confirmFragmentBody);
        confirmFragmentBody.setText(this.bodyMessage);
    }

    void configureConfirmButton(){
        Button confirmBtn = (Button) getView().findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(ConfirmFragment.this).commit();

                if(fragment != null){
                    getFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
        });
    }
}