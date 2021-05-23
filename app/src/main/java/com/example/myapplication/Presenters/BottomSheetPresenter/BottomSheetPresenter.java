package com.example.myapplication.Presenters.BottomSheetPresenter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Handlers.RadiusMarkerHandler.RadiusMarkerHandler;
import com.example.myapplication.Handlers.RadiusMarkerHandler.RadiusMarkerStorage;
import com.example.myapplication.Interfaces.BottomSheetContract.BottomSheetContract;
import com.example.myapplication.Interfaces.DeleteRadiusMarkerListener.DeleteRadiusMarkerListener;
import com.example.myapplication.Interfaces.SetRadiusMarkerListener.SetRadiusMarkerListener;
import com.example.myapplication.R;
import com.example.myapplication.BottomSheetFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

/**
 * Responsible for handling actions from the View and updating the UI as required.
 */
public class BottomSheetPresenter implements BottomSheetContract.Presenter {

    BottomSheetContract.View view;

    BottomSheetFragment bottomSheetFragment;
    GoogleMap mMap;
    LatLng latLng;
    RadiusMarkerHandler radiusMarkerHandler;

    SeekBar radiusMarkerSeekBar;
    ImageButton radiusMarkerCloseButton;
    TextView radiusMarkerSeekBarProgress;
    Button radiusMarkerInAppButton;
    Button radiusMarkerVoiceButton;
    Button radiusMarkerSaveButton;

    boolean inAppButtonClicked;
    boolean voiceButtonClicked;

    Context context;

    RadiusMarkerStorage radiusMarkerStorage;
    DeleteRadiusMarkerListener deleteRadiusMarkerListener;
    SetRadiusMarkerListener setRadiusMarkerListener;
    FragmentManager fragmentManager;

    public BottomSheetPresenter(BottomSheetFragment bottomSheetFragment, GoogleMap mMap, LatLng latLng,
                                RadiusMarkerHandler radiusMarkerHandler, Context context,
                                DeleteRadiusMarkerListener deleteRadiusMarkerListener, SetRadiusMarkerListener setRadiusMarkerListener,
                                FragmentManager fragmentManager, BottomSheetContract.View view) {
        this.view = view;
        this.bottomSheetFragment = bottomSheetFragment;
        this.mMap = mMap;
        this.latLng = latLng;
        this.radiusMarkerHandler = radiusMarkerHandler;
        this.context = context;
        this.deleteRadiusMarkerListener = deleteRadiusMarkerListener;
        this.setRadiusMarkerListener = setRadiusMarkerListener;
        this.fragmentManager = fragmentManager;
        this.radiusMarkerStorage = new RadiusMarkerStorage(radiusMarkerHandler, deleteRadiusMarkerListener, setRadiusMarkerListener, context);
        setNotificationsState();
    }

    public void updateRadiusMarkerSeekBar(SeekBar seekBar, int progress){
        seekBar.setProgress(progress);
        String progressText = progress + "m";
        radiusMarkerSeekBarProgress.setText(progressText);
        radiusMarkerHandler.updateMarkerRadius(progress);
    }

    /**
     * Break out into model
     */
    public void updateRadiusMarkerInAppButton(){
        if(!inAppButtonClicked){
            inAppButtonClicked = true;
            voiceButtonClicked = false;
            radiusMarkerInAppButton.setBackground(bottomSheetFragment.getResources().getDrawable(R.drawable.custom_marker_bottom_background_clicked));
            radiusMarkerVoiceButton.setBackgroundResource(0);
        }else{
            inAppButtonClicked = false;
            radiusMarkerInAppButton.setBackgroundResource(0);
        }
    }

    public void updateRadiusMarkerVoiceButton(){
        if(!voiceButtonClicked){
            voiceButtonClicked = true;
            inAppButtonClicked = false;
            radiusMarkerVoiceButton.setBackground(bottomSheetFragment.getResources().getDrawable(R.drawable.custom_marker_bottom_background_clicked));
            radiusMarkerInAppButton.setBackgroundResource(0);
        }else{
            voiceButtonClicked = false;
            radiusMarkerVoiceButton.setBackgroundResource(0);
        }
    }

    /**
     * Remove shared preference
     */
    public void closeBottomSheetView(){
        SharedPreferences settingsPreference = Objects.requireNonNull(bottomSheetFragment.getContext()).getSharedPreferences("Radius_Marker_Settings", 0);
        boolean stateExists = settingsPreference.getBoolean("stateExists", false);
        double radius = (double)settingsPreference.getFloat("radius", 0.0f);
        double centerLat = (double)settingsPreference.getFloat("centerLat", 0.0f);
        double centerLon = (double)settingsPreference.getFloat("centerLon", 0.0f);

        if (!stateExists){
            radiusMarkerHandler.removeMarker();
        }else{
            radiusMarkerHandler.getRadiusMarker().setRadius(radius);
            radiusMarkerSeekBar.setProgress((int)radius);

            String progressText = (int)radius + "m";
            radiusMarkerSeekBarProgress.setText(progressText);

            setNotificationsState();
            resetNotificationsBackgroundState();
        }
        fragmentManager.popBackStack();
    }

    public void showRemoveDialog(Dialog dialog){
        dialog.setContentView(R.layout.radius_marker_dialog);
        dialog.show();

        Window window = dialog.getWindow();
        if(window != null){
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    public void dismissRemoveDialog(Dialog dialog){
        dialog.dismiss();
    }

    public void handleRadiusMarkerRemoval(boolean valid){
        if(!valid){
            Toast.makeText(context, context.getString(R.string.radius_marker_delete_body), Toast.LENGTH_LONG).show();
        }
    }

    public void handleRadiusMarker(boolean valid){
        if(valid){
            radiusMarkerStorage.saveSharedPreference(inAppButtonClicked, voiceButtonClicked, latLng);
            fragmentManager.popBackStack();
        }else{
            radiusMarkerHandler.removeMarker();
            Toast.makeText(context, context.getString(R.string.radius_marker_set_body), Toast.LENGTH_LONG).show();
        }
    }

    public void remove(){
        SharedPreferences settingsPreference = Objects.requireNonNull(context).getSharedPreferences("Radius_Marker_Settings", 0);
        boolean stateExists = settingsPreference.getBoolean("stateExists", false);

        if(stateExists){
            radiusMarkerStorage.deleteRadiusMarkerDb();
            radiusMarkerStorage.clearSharedPreference();
            Toast.makeText(context, context.getString(R.string.form_confirm_radius_marker_removed), Toast.LENGTH_LONG).show();
        }

        radiusMarkerHandler.removeMarker();
        if(bottomSheetFragment.isAdded()){
            fragmentManager.popBackStack();
        }
    }

    void configureSeekBar(){

    }

    void configureInAppButton(){

    }

    void configureVoiceButton(){

    }

    void configureCloseButton(){

    }

    void configureRemoveButton(){

    }

    void configureRemoveDialog(){

    }

    void configureSaveButton(){

    }

    void setNotificationsState(){
        SharedPreferences settingsPreference = Objects.requireNonNull(context).getSharedPreferences("Radius_Marker_Settings", 0);
        boolean inAppButtonState = settingsPreference.getBoolean("inAppNotifications", false);
        boolean voiceButtonState = settingsPreference.getBoolean("voiceNotifications", false);

        inAppButtonClicked = inAppButtonState;
        voiceButtonClicked = voiceButtonState;
    }

    void resetNotificationsBackgroundState(){
        if(inAppButtonClicked){
            radiusMarkerInAppButton.setBackground(bottomSheetFragment.getResources().getDrawable(R.drawable.custom_marker_bottom_background_clicked));
        }else if(voiceButtonClicked){
            radiusMarkerVoiceButton.setBackground(bottomSheetFragment.getResources().getDrawable(R.drawable.custom_marker_bottom_background_clicked));
        }else{
            inAppButtonClicked = true;
            radiusMarkerInAppButton.setBackground(bottomSheetFragment.getResources().getDrawable(R.drawable.custom_marker_bottom_background_clicked));
        }
    }




}
