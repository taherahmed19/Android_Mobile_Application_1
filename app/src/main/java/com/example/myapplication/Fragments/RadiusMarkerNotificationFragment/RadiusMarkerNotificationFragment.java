package com.example.myapplication.Fragments.RadiusMarkerNotificationFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class RadiusMarkerNotificationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_radius_marker_notification, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayout radiusMarkerNotifCloseButton = (LinearLayout) this.getView().findViewById(R.id.radiusMarkerNotifCloseButton);
        radiusMarkerNotifCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
    }

}
