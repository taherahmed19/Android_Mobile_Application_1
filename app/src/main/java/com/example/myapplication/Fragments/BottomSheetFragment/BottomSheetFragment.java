package com.example.myapplication.Fragments.BottomSheetFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Interfaces.BottomSheetContract.BottomSheetContract;
import com.example.myapplication.Interfaces.DeleteRadiusMarkerListener.DeleteRadiusMarkerListener;
import com.example.myapplication.Interfaces.SetRadiusMarkerListener.SetRadiusMarkerListener;
import com.example.myapplication.Models.RadiusMarker.RadiusMarker;
import com.example.myapplication.Presenters.BottomSheetPresenter.BottomSheetPresenter;
import com.example.myapplication.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

/**
 * Displays bottom fragment for radius marker
 */
public class BottomSheetFragment extends Fragment implements BottomSheetContract.View {

    BottomSheetPresenter bottomSheetPresenter;

    SeekBar radiusMarkerSeekBar;
    TextView radiusMarkerSeekBarProgress;
    ImageButton radiusMarkerCloseButton;
    Button radiusMarkerInAppButton;
    Button radiusMarkerVoiceButton;
    Button radiusMarkerRemoveButton;
    Button radiusMarkerSaveButton;
    Dialog dialog;

    GoogleMap mMap; LatLng latLng; RadiusMarker radiusMarker;

    public BottomSheetFragment(GoogleMap mMap, LatLng latLng, RadiusMarker radiusMarker, FragmentManager fragmentManager) {
        this.mMap = mMap;
        this.latLng = latLng;
        this.radiusMarker = radiusMarker;
        this.bottomSheetPresenter = new BottomSheetPresenter(mMap, latLng, radiusMarker, fragmentManager, this);

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


        initialiseComponents();
        configureCloseButton();
        configureInAppButton();
        configureRemoveButton();
        configureSaveButton();
        configureSeekBar();
        configureVoiceButton();
    }

    @Override
    public void updateRadiusMarkerSeekBar(SeekBar seekBar, int progress){
        seekBar.setProgress(progress);
        String progressText = progress + "m";
        radiusMarkerSeekBarProgress.setText(progressText);
        bottomSheetPresenter.updateRadius(progress);
    }

    @Override
    public void setInAppButtonClicked(){
        radiusMarkerInAppButton.setBackground(getResources().getDrawable(R.drawable.custom_marker_bottom_background_clicked));
        radiusMarkerVoiceButton.setBackgroundResource(0);
    }

    @Override
    public void removeInAppButtonBackground(){
        radiusMarkerInAppButton.setBackgroundResource(0);
    }

    @Override
    public void setVoiceButtonClicked(){
        radiusMarkerVoiceButton.setBackground(getResources().getDrawable(R.drawable.custom_marker_bottom_background_clicked));
        radiusMarkerInAppButton.setBackgroundResource(0);
    }

    @Override
    public void removeVoiceButtonBackground(){
        radiusMarkerVoiceButton.setBackgroundResource(0);
    }

    @Override
    public void closeBottomSheetView(RadiusMarker radiusMarker){
        SharedPreferences settingsPreference = Objects.requireNonNull(getContext()).getSharedPreferences("Radius_Marker_Settings", 0);
        boolean stateExists = settingsPreference.getBoolean("stateExists", false);

        if (!stateExists){
            bottomSheetPresenter.removeMarker();
        }else{
            bottomSheetPresenter.updateRadius(radiusMarker.getRadius());
            radiusMarkerSeekBar.setProgress((int)radiusMarker.getRadius());

            String progressText = (int)radiusMarker.getRadius() + "m";
            radiusMarkerSeekBarProgress.setText(progressText);
//
//            bottomSheetPresenter.setNotificationsState();
//            bottomSheetPresenter.resetNotificationsBackgroundState();
        }
        this.getFragmentManager().popBackStack();
    }

    @Override
    public void showRemoveDialog(){
        dialog.setContentView(R.layout.radius_marker_dialog);
        dialog.show();

        Window window = dialog.getWindow();
        if(window != null){
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void dismissRemoveDialog(){
        dialog.dismiss();
    }

    @Override
    public void handleSaveButtonClick(){
        bottomSheetPresenter.writeRadiusMarkerDb();
        //radiusMarkerStorage.saveSharedPreference(inAppButtonClicked, voiceButtonClicked, latLng);

        SharedPreferences settingsPreference = Objects.requireNonNull(this.getApplicationContext()).getSharedPreferences("Radius_Marker_Settings", 0);
        SharedPreferences.Editor mapStateEditor = settingsPreference.edit();
        mapStateEditor.putBoolean("stateExists", true);
        mapStateEditor.apply();

        getParentFragmentManager().popBackStack();
    }

    @Override
    public Context getApplicationContext(){
        return this.getContext();
    }

    public void removeExistingRadiusMarker(Context context){
        bottomSheetPresenter.deleteRadiusMarkerDb();

        bottomSheetPresenter.removeMarker();
        if(isAdded()){
            getFragmentManager().popBackStack();
        }
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
            String progressText =  "";

            radiusMarkerSeekBar.setMin(50);
            radiusMarkerSeekBar.setMax(500);
            radiusMarkerSeekBar.setProgress((int)this.bottomSheetPresenter.getRadiusMarkerRadius());

            if(this.bottomSheetPresenter.getRadiusMarkerRadius() > 0){
                progressText =  String.valueOf(this.bottomSheetPresenter.getRadiusMarkerRadius());
            }else{
                progressText =  "50m";
            }
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
        SharedPreferences settingsPreference = Objects.requireNonNull(this.getContext()).getSharedPreferences("Radius_Marker_Settings", 0);
        boolean stateExists = settingsPreference.getBoolean("stateExists", false);

        if(stateExists){
            radiusMarkerRemoveButton.setVisibility(View.VISIBLE);
        }else{
            radiusMarkerRemoveButton.setVisibility(View.INVISIBLE);
        }

        radiusMarkerRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetPresenter.showRemoveDialog();
                configureRemoveDialog();
            }
        });
    }

    private void configureRemoveDialog(){
        Button radiusMarkerDialogCloseButton = (Button) dialog.findViewById(R.id.radiusMarkerDialogCloseButton);
        radiusMarkerDialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetPresenter.dismissRemoveDialog();
            }
        });

        Button radiusMarkerDialogRemoveButton = (Button) dialog.findViewById(R.id.radiusMarkerDialogRemoveButton);
        radiusMarkerDialogRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetPresenter.deleteRadiusMarkerDb();

            }
        });
    }

    @Override
    public void handleRadiusMarkerRemoval(Boolean valid){
        if(!valid){
            Toast.makeText(this.getApplicationContext(), this.getApplicationContext().getString(R.string.radius_marker_delete_body), Toast.LENGTH_LONG).show();
        }else{
            dialog.dismiss();
            bottomSheetPresenter.removeMarker();
            Objects.requireNonNull(getApplicationContext()).getSharedPreferences("Radius_Marker_Settings", 0).edit().clear().apply();
            if(isAdded()){
                getFragmentManager().popBackStack();
            }
        }
    }

    private void configureSaveButton(){
        radiusMarkerSaveButton = (Button) Objects.requireNonNull(this.getView()).findViewById(R.id.radiusMarkerSaveButton);

        radiusMarkerSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetPresenter.handleSaveButtonClick();
            }
        });
    }
}