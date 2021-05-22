package com.example.myapplication.Views.Views.Fragments.BottomSheetFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Presenters.BottomSheetPresenter.BottomSheetPresenter;
import com.example.myapplication.Handlers.RadiusMarkerHandler.RadiusMarkerHandler;
import com.example.myapplication.Interfaces.BottomSheetContract.BottomSheetContract;
import com.example.myapplication.Interfaces.DeleteRadiusMarkerListener.DeleteRadiusMarkerListener;
import com.example.myapplication.Interfaces.SetRadiusMarkerListener.SetRadiusMarkerListener;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FragmentTransition.FragmentTransition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

/**
 * Displays bottom fragment for radius marker
 */
public class BottomSheetFragment extends Fragment implements BottomSheetContract.View {
/*
    BottomSheetPresenter bottomSheetPresenter;
    Context context;
    GoogleMap mMap;
    LatLng latLng;

    SeekBar radiusMarkerSeekBar;
    TextView radiusMarkerSeekBarProgress;
    ImageButton radiusMarkerCloseButton;
    Button radiusMarkerInAppButton;
    Button radiusMarkerVoiceButton;
    Button radiusMarkerRemoveButton;
    Button radiusMarkerSaveButton;
    Dialog dialog;

    public BottomSheetFragment(Context context, GoogleMap mMap, LatLng latLng, double radius, RadiusMarkerHandler radiusMarkerHandler,
                               DeleteRadiusMarkerListener deleteRadiusMarkerListener, SetRadiusMarkerListener setRadiusMarkerListener,
                               FragmentManager fragmentManager) {
        this.context = context;
        this.mMap = mMap;
        this.latLng = latLng;
        this.bottomSheetPresenter = new BottomSheetPresenter(this, mMap, latLng, radiusMarkerHandler,
                context, deleteRadiusMarkerListener, setRadiusMarkerListener, fragmentManager, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_marker_bottom, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bottomSheetPresenter.configure();
    }

    public void remove(){
        this.bottomSheetPresenter.remove();
    }

    private void initialiseComponents(){
        if(this.getView() != null && this.getContext() != null){
            radiusMarkerSeekBar = (SeekBar) this.getView().findViewById(R.id.radiusMarkerSeekBar);
            radiusMarkerSeekBarProgress = (TextView) this.getView().findViewById(R.id.radiusMarkerSeekBarProgress);
            radiusMarkerInAppButton = (Button) this.getView().findViewById(R.id.radiusMarkerInAppButton);
            radiusMarkerVoiceButton = (Button) this.getView().findViewById(R.id.radiusMarkerVoiceButton);
            radiusMarkerCloseButton = (ImageButton) this.getView().findViewById(R.id.radiusMarkerCloseButton);
            radiusMarkerRemoveButton = (Button) this.getView().findViewById(R.id.radiusMarkerRemoveButton);
            dialog = new Dialog(this.getContext());
        }
    }

    private void configureSeekBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            radiusMarkerSeekBar.setMin(50);
            radiusMarkerSeekBar.setMax(500);
            radiusMarkerSeekBar.setProgress((int)this.radiusMarkerHandler.getRadiusMarker().getRadius());

            String progressText =  "50m";
            radiusMarkerSeekBarProgress.setText(progressText);
        }

        radiusMarkerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bottomSheetPresenter.updateRadiusMarkerSeekBar(seekBar, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void configureInAppButton(){
        radiusMarkerInAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetPresenter.updateRadiusMarkerInAppButton();
            }
        });
    }

    private void configureVoiceButton(){
        radiusMarkerVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetPresenter.updateRadiusMarkerVoiceButton();
            }
        });
    }

    private void configureCloseButton(){
        radiusMarkerCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetPresenter.closeBottomSheetView();
            }
        });
    }

    void configureRemoveButton(){
        SharedPreferences settingsPreference = Objects.requireNonNull(bottomSheetFragment.getContext()).getSharedPreferences("Radius_Marker_Settings", 0);
        boolean stateExists = settingsPreference.getBoolean("stateExists", false);

        if(stateExists){
            radiusMarkerRemoveButton.setVisibility(View.VISIBLE);
        }else{
            radiusMarkerRemoveButton.setVisibility(View.INVISIBLE);
        }

        radiusMarkerRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetPresenter.showRemoveDialog(dialog);
                configureRemoveDialog();
            }
        });
    }

    private void configureRemoveDialog(){
        Button radiusMarkerDialogCloseButton = (Button) dialog.findViewById(R.id.radiusMarkerDialogCloseButton);
        radiusMarkerDialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetPresenter.dismissRemoveDialog(dialog);
            }
        });

        Button radiusMarkerDialogRemoveButton = (Button) dialog.findViewById(R.id.radiusMarkerDialogRemoveButton);
        radiusMarkerDialogRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radiusMarkerStorage.deleteRadiusMarkerDb();
                dialog.dismiss();
                radiusMarkerHandler.removeMarker();
                Objects.requireNonNull(context).getSharedPreferences("Radius_Marker_Settings", 0).edit().clear().apply();
                if(bottomSheetFragment.isAdded()){
                    fragmentManager.popBackStack();
                }
            }
        });
    }

    private void configureSaveButton(){
        radiusMarkerSaveButton = (Button) Objects.requireNonNull(this.bottomSheetFragment.getView()).findViewById(R.id.radiusMarkerSaveButton);

        radiusMarkerSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radiusMarkerStorage.writeRadiusMarkerDb();
                radiusMarkerStorage.saveSharedPreference(inAppButtonClicked, voiceButtonClicked, latLng);
                bottomSheetFragment.getParentFragmentManager().popBackStack();
            }
        });
    }*/
}