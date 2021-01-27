package com.example.myapplication.Handlers.FormFragmentHandler;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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

import com.example.myapplication.Activities.LocationSelectorActivity.LocationSelectorActivity;
import com.example.myapplication.Adapters.CustomSpinnerAdapter.CustomSpinnerAdapter;
import com.example.myapplication.Fragments.FormFragment.FormFragment;
import com.example.myapplication.HttpRequest.HttpMarker.HttpMarker;
import com.example.myapplication.Interfaces.FeedSubmitListener.FeedSubmitListener;
import com.example.myapplication.Models.FormFragmentSpinner.FormFragmentSpinner;
import com.example.myapplication.HttpRequest.FormStaticMap.FormStaticMap;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Models.SpinnerItem.SpinnerItem;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Utils.Tools.Tools;
import com.example.myapplication.Validators.FormFragmentValidator.FormFragmentValidator;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

public class FormFragmentHandler {

    public static final int GALLERY_REQUEST = 1000;
    public static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    ImageView formLocationImage;
    FormFragmentSpinner formFragmentSpinner;
    FormFragment formFragment;
    FeedSubmitListener feedSubmitListener;
    FormFragmentValidator formFragmentValidator;
    boolean isConfiguredStaticMap;
    String encodedImage;

    public FormFragmentHandler(FormFragment formFragment, FeedSubmitListener feedSubmitListener) {
        this.formFragmentSpinner = new FormFragmentSpinner(formFragment);
        this.formFragment = formFragment;
        this.feedSubmitListener = feedSubmitListener;
        this.isConfiguredStaticMap = false;
        this.formFragmentValidator = new FormFragmentValidator(this);
    }

    public void configure(){
        configureMarkerSpinner();
        configureDescriptionText();
        configureGeolocationButton();
        configureLocationButton();
        configureMedia();
        configureFormCloseButton();
        configureSubmitButton();
        configureSpinner();
    }

    public void submit(){
        Spinner spinner = (Spinner) formFragment.getView().findViewById(R.id.formSpinner);
        SpinnerItem item = (SpinnerItem) spinner.getSelectedItem();
        int category = Marker.CategorySwitchCase(item.getName().toLowerCase());
        EditText mapFeedDescription = (EditText) formFragment.getView().findViewById(R.id.mapFeedDescription);
        String description = Tools.encodeString(mapFeedDescription.getText().toString());
        Button geolocationButton = (Button) formFragment.getView().findViewById(R.id.geolocationButton);
        int userID = LoginPreferenceData.getUserId(formFragment.getActivity().getApplicationContext());

        LatLng chosenLocation = (LatLng)geolocationButton.getTag();

        HttpMarker httpMarker = new HttpMarker(this.formFragment.getContext(), userID, category, description, chosenLocation ,feedSubmitListener, this.encodedImage);
        httpMarker.execute("");
    }

    public void onActivityResultCamera(int requestCode, int resultCode, Intent data){
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            ImageView postImageView = (ImageView) this.formFragment.getView().findViewById(R.id.postImageView);
            postImageView.setImageBitmap(photo);

            if (photo != null) {
                encodeImage(photo);
            }
        }
    }

    public void onActivityResultGallery(int requestCode, int resultCode, Intent data){
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK)
        {
            if(data != null){
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(this.formFragment.getActivity().getContentResolver(), data.getData());

                    ImageView postImageView = (ImageView) this.formFragment.getView().findViewById(R.id.postImageView);
                    postImageView.setImageBitmap(photo);

                    if(photo != null){
                        encodeImage(photo);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if (resultCode == Activity.RESULT_CANCELED)  {
            Toast.makeText(formFragment.getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    void encodeImage(Bitmap photo){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        this.encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void onActivityResultConfigure(double lat, double lng){
        setGeolocationButtonClickable(true);
        updateGeolocationText(true);
        configureStaticMap(new LatLng(lat,lng));
    }

    public void showDescriptionErrorMessage(){
        TextView descriptionError = (TextView) formFragment.getView().findViewById(R.id.descriptionError);
        descriptionError.setVisibility(View.VISIBLE);
    }

    public void showSpinnerErrorMessage(){
        TextView spinnerError = (TextView) formFragment.getView().findViewById(R.id.spinnerError);
        spinnerError.setVisibility(View.VISIBLE);
    }

    public void removeDescriptionErrorMessage(){
        TextView descriptionError = (TextView) formFragment.getView().findViewById(R.id.descriptionError);
        descriptionError.setVisibility(View.INVISIBLE);
    }

    public void removeSpinnerErrorMessage(){
        TextView spinnerError = (TextView) formFragment.getView().findViewById(R.id.spinnerError);
        spinnerError.setVisibility(View.INVISIBLE);
    }

    public boolean isConfiguredStaticMap() {
        return isConfiguredStaticMap;
    }

    void configureSpinner(){
        Spinner spinner = formFragment.getView().findViewById(R.id.formSpinner);
        int initialPosition = spinner.getSelectedItemPosition();
        spinner.setSelection(initialPosition, false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                formFragmentValidator.spinnerFocusChange(spinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void configureDescriptionText(){
        EditText mapFeedDescription = (EditText) formFragment.getView().findViewById(R.id.mapFeedDescription);

        mapFeedDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                formFragmentValidator.descriptionTextChange(mapFeedDescription);
            }
        });
    }

    void configureLocationButton(){
        final Button locationButton = (Button) formFragment.getView().findViewById(R.id.locationButton);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner spinner = formFragment.getView().findViewById(R.id.formSpinner);
                int marker = -1;

                if(spinner.getSelectedItemPosition() == 0){
                }else{
                    SpinnerItem spinnerItem = (SpinnerItem)spinner.getSelectedItem();
                    marker = (int)(BitmapDescriptorFactory.HUE_RED);
                }

                Intent intent = new Intent(formFragment.getActivity(), LocationSelectorActivity.class);
                intent.putExtra("chosenMarker", marker);

                formFragment.startActivityForResult(intent, 1);
            }
        });
    }

    void configureGeolocationButton(){
        Button geolocationButton = (Button) formFragment.getView().findViewById(R.id.geolocationButton);

        geolocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGeolocationButtonClickable(false);
                updateGeolocationText(false);

                CurrentLocation currentLocation = new CurrentLocation(formFragment.getActivity());
                LatLng location = currentLocation.accessGeolocation();

                configureStaticMap(new LatLng(location.latitude, location.longitude));
            }
        });

        setGeolocationButtonClickable(false);
        updateGeolocationText(false);
    }

    void setGeolocationButtonClickable(boolean clickable){
        Button geolocationButton = (Button) formFragment.getView().findViewById(R.id.geolocationButton);
        geolocationButton.setClickable(clickable);
    }

    void setFormGeolocationTag(double lat, double lng){
        Button geolocationButton = (Button) formFragment.getView().findViewById(R.id.geolocationButton);
        geolocationButton.setTag(new LatLng(lat, lng));
    }

    void updateGeolocationText(boolean locationButtonSelected){
        Button geolocationButton = (Button) formFragment.getView().findViewById(R.id.geolocationButton);

        if(locationButtonSelected){
            geolocationButton.setText(formFragment.getString(R.string.form_use_current_location));
        }else{
            geolocationButton.setText(formFragment.getString(R.string.form_current_location));
        }
    }

    public void configureStaticMap(LatLng latLng){
        String imageUrl = MessageFormat.format(formFragment.getString(R.string.form_static_map),
                latLng.latitude, latLng.longitude, 15, latLng.latitude, latLng.longitude, formFragment.getString(R.string.GOOGLE_API_KEY));
        formLocationImage = (ImageView) formFragment.getView().findViewById(R.id.formLocationImage);

        setFormGeolocationTag(latLng.latitude, latLng.longitude);

        FormStaticMap formStaticMap = new FormStaticMap(formLocationImage);
        formStaticMap.execute(imageUrl);

        isConfiguredStaticMap = true;
    }

    void configureMedia(){
        RelativeLayout imageButton = (RelativeLayout) this.formFragment.getView().findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraDialog();

            }
        });
    }

    void openCameraDialog(){
        Dialog dialog = new Dialog(this.formFragment.getContext());
        dialog.setContentView(R.layout.camera_dialog);
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        Button openCameraBtn = (Button) dialog.findViewById(R.id.openCameraBtn);
        Button openGalleryBtn = (Button) dialog.findViewById(R.id.openGalleryBtn);

        openCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        openGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    void openCamera(){
        if (formFragment.getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            formFragment.requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            formFragment.startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.formFragment.startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                this.formFragment.startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }

    void configureFormCloseButton(){
        final ImageButton formClose = (ImageButton) formFragment.getView().findViewById(R.id.formClose);
        formClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(formFragment.getActivity() != null){
                    Tools.HideKeyboard(formFragment.getActivity());
                }
                formFragment.getParentFragmentManager().popBackStack();
            }
        });
    }

    void configureSubmitButton(){
        ImageButton mapFeedSubmit = (ImageButton) formFragment.getView().findViewById(R.id.mapFeedSubmit);
        mapFeedSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validPost = validate();
                if(validPost){
                    if(formFragment.getActivity() != null){
                        Tools.HideKeyboard(formFragment.getActivity());
                    }
                    submit();
                }
            }
        });
    }

    ArrayList<SpinnerItem> configureMarkerSpinner(){
        Spinner spinner = formFragment.getView().findViewById(R.id.formSpinner);
        ArrayList<SpinnerItem> customList = CustomSpinnerAdapter.CreateMarkerSpinnerItems(formFragment.getContext());

        if(formFragment.getActivity() != null){
            CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(formFragment.getActivity(), customList);

            if(spinner != null){
                spinner.setAdapter(adapter);
            }
        }

        return customList;
    }

    boolean validate(){
        EditText mapFeedDescription = (EditText) formFragment.getView().findViewById(R.id.mapFeedDescription);
        Spinner spinner = formFragment.getView().findViewById(R.id.formSpinner);
        boolean validSpinnerItem = formFragmentValidator.spinnerFocusChange(spinner);
        boolean validDescription = formFragmentValidator.descriptionFocusChange(mapFeedDescription);

        return validSpinnerItem && validDescription;
    }

}
