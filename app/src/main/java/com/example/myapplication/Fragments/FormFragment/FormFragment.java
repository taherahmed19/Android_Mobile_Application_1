package com.example.myapplication.Fragments.FormFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Activities.LocationSelectorActivity.LocationSelectorActivity;
import com.example.myapplication.Adapters.CustomSpinnerAdapter.CustomSpinnerAdapter;
import com.example.myapplication.Interfaces.CurrentLocationListener.CurrentLocationListener;
import com.example.myapplication.Interfaces.FeedSubmitListener.FeedSubmitListener;
import com.example.myapplication.Interfaces.FormContract.FormContract;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Models.SpinnerItem.SpinnerItem;
import com.example.myapplication.Presenters.FormPresenter.FormPresenter;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.JWTToken.JWTToken;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Utils.StringConstants.StringConstants;
import com.example.myapplication.Utils.Tools.Tools;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class FormFragment extends Fragment implements FeedSubmitListener, CurrentLocationListener, FormContract.View {

    public static final int GALLERY_REQUEST = 1000;
    public static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    FragmentManager fragmentSupport;
    CurrentLocation currentLocation;
    ViewPager viewPager;

    FormPresenter formPresenter;

    Dialog dialog;
    boolean isConfiguredStaticMap;

    public FormFragment(FragmentManager fragmentSupport, ViewPager viewPager){
        this.fragmentSupport = fragmentSupport;
        this.viewPager = viewPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        currentLocation = new CurrentLocation(this.getActivity(),  this);
        formPresenter = new FormPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_feed_form, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        configureDescriptionText();
        configureFormCloseButton();
        configureLocationButton();
        configureMarkerSpinner();
        configureMedia();
        configureSpinner();
        configureSubmitButton();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            double lat = data.getDoubleExtra(StringConstants.INTENT_LOCATION_SELECTOR_LAT, 0);
            double lng = data.getDoubleExtra(StringConstants.INTENT_LOCATION_SELECTOR_LNG, 0);

            this.onActivityResultConfigure(lat, lng);
        }

        if(requestCode == CAMERA_REQUEST){
            this.onActivityResultCamera(requestCode, resultCode, data);
        }

        if(requestCode == GALLERY_REQUEST){
            this.onActivityResultGallery(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                this.startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }

    @Override
    public void handleTokenExpiration(){
        JWTToken.removeTokenSharedPref(this.getActivity());
    }

    @Override
    public void handleSubmitStatusMessage(boolean validPost) {
        if(validPost){
            Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
            getParentFragmentManager().popBackStack();
        }else{
            Toast.makeText(getContext(), getContext().getString(R.string.form_error_body), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        currentLocation.startLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        currentLocation.stopLocationUpdates();
    }

    @Override
    public void updateUserLocation(Location location) {
        if(!this.isConfiguredStaticMap()){
            formPresenter.updateCurrentLatLng(location);
        }
    }

    @Override
    public void handleLocationButtonClick(){
        Spinner spinner = getView().findViewById(R.id.formSpinner);
        int marker = -1;

        if(spinner.getSelectedItemPosition() == 0){
        }else{
            SpinnerItem spinnerItem = (SpinnerItem)spinner.getSelectedItem();
            marker = (int)(BitmapDescriptorFactory.HUE_RED);
        }

        Intent intent = new Intent(getActivity(), LocationSelectorActivity.class);
        intent.putExtra("chosenMarker", marker);

        startActivityForResult(intent, 1);
    }

    @Override
    public boolean handleDescriptionScroll(EditText mapFeedDescription, View v, MotionEvent event){
        if (mapFeedDescription.hasFocus()) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_SCROLL) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
                return true;
            }
        }
        return false;
    }

    @Override
    public void handleSubmitButtonClick(){
        boolean validPost = validate();
        if(validPost){
            if(getActivity() != null){
                Tools.HideKeyboard(getActivity());
            }
            submitForm();
        }
    }

    @Override
    public void handleCloseButtonClick(){
        if(getActivity() != null){
            Tools.HideKeyboard(getActivity());
        }
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void handleCameraDialog(){
        openCameraDialog();
    }

    @Override
    public void openCamera(){
        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    public void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
    }

    @Override
    public Context getApplicationContext(){
        return this.getContext();
    }

    public void onActivityResultCamera(int requestCode, int resultCode, Intent data){
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            if(data != null) {
                try {
                    dialog.dismiss();
                    Bitmap photo = (Bitmap) data.getExtras().get("data");

                    ImageView postImageView = (ImageView) this.getView().findViewById(R.id.postImageView);
                    postImageView.setImageBitmap(photo);

                    if (photo != null) {
                        encodeImage(photo);
                    }

                    removeImageErrorMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onActivityResultGallery(int requestCode, int resultCode, Intent data){
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK)
        {
            if(data != null){
                try {
                    dialog.dismiss();
                    Bitmap photo = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), data.getData());

                    ImageView postImageView = (ImageView) this.getView().findViewById(R.id.postImageView);
                    postImageView.setImageBitmap(photo);

                    if(photo != null){
                        encodeImage(photo);
                    }

                    removeImageErrorMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onActivityResultConfigure(double lat, double lng){
        formPresenter.updateChosenLocation(lat, lng);
        this.removeLocationErrorMessage();
    }

    public void showDescriptionErrorMessage(){
        TextView descriptionError = (TextView) getView().findViewById(R.id.descriptionError);
        descriptionError.setVisibility(View.VISIBLE);
    }

    public void showSpinnerErrorMessage(){
        TextView spinnerError = (TextView) getView().findViewById(R.id.spinnerError);
        spinnerError.setVisibility(View.VISIBLE);
    }

    public void showImageErrorMessage(){
        TextView imageError = (TextView) getView().findViewById(R.id.imageError);
        imageError.setVisibility(View.VISIBLE);
    }

    public void showLocationErrorMessage(){
        TextView imageError = (TextView) getView().findViewById(R.id.locationError);
        imageError.setVisibility(View.VISIBLE);
    }

    public void removeLocationErrorMessage(){
        TextView descriptionError = (TextView) getView().findViewById(R.id.locationError);
        descriptionError.setVisibility(View.INVISIBLE);
    }

    public void removeDescriptionErrorMessage(){
        TextView descriptionError = (TextView) getView().findViewById(R.id.descriptionError);
        descriptionError.setVisibility(View.INVISIBLE);
    }

    public void removeSpinnerErrorMessage(){
        TextView spinnerError = (TextView) getView().findViewById(R.id.spinnerError);
        spinnerError.setVisibility(View.INVISIBLE);
    }

    public void removeImageErrorMessage(){
        TextView imageError = (TextView) getView().findViewById(R.id.imageError);
        imageError.setVisibility(View.INVISIBLE);
    }

    public boolean isConfiguredStaticMap() {
        return isConfiguredStaticMap;
    }

    void submitForm(){
        Spinner spinner = (Spinner) getView().findViewById(R.id.formSpinner);
        SpinnerItem item = (SpinnerItem) spinner.getSelectedItem();
        String category = item.getName().toLowerCase();
        EditText mapFeedDescription = (EditText) getView().findViewById(R.id.mapFeedDescription);
        String description = mapFeedDescription.getText().toString();
        int userID = LoginPreferenceData.getUserId(getActivity().getApplicationContext());

        formPresenter.makeApiCall(userID, category, description, this);
    }

    void encodeImage(Bitmap photo){
        formPresenter.updateEncodedImage(photo);
    }

    void configureSpinner(){
        Spinner spinner = getView().findViewById(R.id.formSpinner);
        int initialPosition = spinner.getSelectedItemPosition();
        spinner.setSelection(initialPosition, false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerFocusChange(spinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    void configureDescriptionText(){
        EditText mapFeedDescription = (EditText) getView().findViewById(R.id.mapFeedDescription);

        mapFeedDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                descriptionTextChange(mapFeedDescription);
            }
        });

        mapFeedDescription.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                return formPresenter.handleDescriptionScroll(mapFeedDescription, v, event);
            }
        });
    }

    void removeErrorDrawable(View view){
        if(view instanceof Spinner){
            view.setBackgroundResource(R.drawable.ic_custom_spinner);
        }else{
            view.setBackgroundResource(R.drawable.input_border);
        }
    }

    void addErrorDrawable(View view){
        if(view instanceof Spinner){
            view.setBackgroundResource(R.drawable.ic_custom_spinner_error);
        }else{
            view.setBackgroundResource(R.drawable.input_border_error);
        }
    }

    boolean spinnerFocusChange(Spinner spinner){
        SpinnerItem item = (SpinnerItem) spinner.getSelectedItem();

        String category = item.getName().toLowerCase();

        if(spinner.getSelectedItemPosition() == 0){
            addErrorDrawable(spinner);
            showSpinnerErrorMessage();

            return false;
        }else{
            removeErrorDrawable(spinner);
            removeSpinnerErrorMessage();
        }


        return true;
    }

    boolean descriptionFocusChange(EditText mapFeedDescription){
        String description = mapFeedDescription.getText().toString();

        if(TextUtils.isEmpty(description)){
            addErrorDrawable(mapFeedDescription);
            showDescriptionErrorMessage();
            return false;
        }else{
            removeErrorDrawable(mapFeedDescription);
            removeDescriptionErrorMessage();
        }

        return true;
    }

    boolean descriptionTextChange(EditText mapFeedDescription){
        String description = mapFeedDescription.getText().toString();

        if(!TextUtils.isEmpty(description) && description.length() > 0){
            removeErrorDrawable(mapFeedDescription);
            removeDescriptionErrorMessage();
            return true;
        }else{
            addErrorDrawable(mapFeedDescription);
            showDescriptionErrorMessage();
        }

        return false;
    }

    boolean imageSelected(ImageButton imageButton, String encodedImage){
        if(encodedImage == null){
            addErrorDrawable(imageButton);
            showImageErrorMessage();
            return false;
        }else{
            if(encodedImage.length() > 0){
                removeErrorDrawable(imageButton);
                removeImageErrorMessage();
            }
            return encodedImage.length() > 0;
        }
    }

    boolean locationSelected(LatLng chosenLocation){
        if(chosenLocation != null){
            removeLocationErrorMessage();
        }else{
            showLocationErrorMessage();
        }

        return chosenLocation != null;
    }

    void configureLocationButton(){
        final Button locationButton = (Button) getView().findViewById(R.id.locationButton);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formPresenter.handleLocationButtonClick();
            }
        });
    }

    void configureMedia(){
        ImageButton imageButton = (ImageButton) this.getView().findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formPresenter.handleCameraDialog();
            }
        });
    }

    void configureFormCloseButton(){
        final ImageButton formClose = (ImageButton) getView().findViewById(R.id.formClose);
        formClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formPresenter.handleCloseButtonClick();
            }
        });
    }

    void configureSubmitButton(){
        ImageButton mapFeedSubmit = (ImageButton) getView().findViewById(R.id.mapFeedSubmit);
        mapFeedSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formPresenter.handleSubmitButtonClick();
            }
        });
    }

    void openCameraDialog(){
        try{
            dialog = new Dialog(Objects.requireNonNull(this.getContext()));
            dialog.setContentView(R.layout.camera_dialog);
            dialog.show();
        }catch (Exception e){
            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
        }

        if(dialog != null){
            Window window = dialog.getWindow();
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            Button openCameraBtn = (Button) dialog.findViewById(R.id.openCameraBtn);
            Button openGalleryBtn = (Button) dialog.findViewById(R.id.openGalleryBtn);

            openCameraBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    formPresenter.openCamera();
                }
            });

            openGalleryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    formPresenter.openGallery();
                }
            });
        }
    }

    ArrayList<SpinnerItem> configureMarkerSpinner(){
        Spinner spinner = getView().findViewById(R.id.formSpinner);
        ArrayList<SpinnerItem> customList = CustomSpinnerAdapter.CreateMarkerSpinnerItems(getContext());

        if(getActivity() != null){
            CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), customList);

            if(spinner != null){
                spinner.setAdapter(adapter);
            }
        }

        return customList;
    }

    boolean validate(){
        EditText mapFeedDescription = (EditText) getView().findViewById(R.id.mapFeedDescription);
        Spinner spinner = getView().findViewById(R.id.formSpinner);
        ImageButton imageButton = (ImageButton) getView().findViewById(R.id.imageButton);

        boolean validSpinnerItem = spinnerFocusChange(spinner);
        boolean validDescription = descriptionFocusChange(mapFeedDescription);
        boolean validImage = imageSelected(imageButton, formPresenter.getEncodedImage());
        boolean validLocation = locationSelected(formPresenter.getChosenLocation());

        return validSpinnerItem && validDescription && validImage && validLocation;
    }
}