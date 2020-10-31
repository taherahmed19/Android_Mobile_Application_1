package com.example.myapplication.Fragments.ConfirmFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;

public class ConfirmFragment extends Fragment {

    Fragment userFeedForm;

    public ConfirmFragment(Fragment userFeedForm){
        this.userFeedForm = userFeedForm;
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
    }

    void configureConfirmButton(){
        Button confirmBtn = (Button) getView().findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(ConfirmFragment.this).commit();
                getFragmentManager().beginTransaction().remove(userFeedForm).commit();
            }
        });
    }
}