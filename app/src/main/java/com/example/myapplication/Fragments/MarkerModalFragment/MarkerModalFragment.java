package com.example.myapplication.Fragments.MarkerModalFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Interfaces.MarkerModalContract.MarkerModalContract;
import com.example.myapplication.Models.Marker.Marker;
import com.example.myapplication.Presenters.MarkerModalPresenter.MarkerModalPresenter;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.JWTToken.JWTToken;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData.LoginPreferenceData;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Objects;

public class MarkerModalFragment extends Fragment implements MarkerModalContract.View {

    ViewPager viewPager;
    MarkerModalPresenter markerModalPresenter;

    public MarkerModalFragment(Marker marker, ViewPager viewPager) {
        this.viewPager = viewPager;
        this.markerModalPresenter = new MarkerModalPresenter(this, marker);
    }

    /**
     * lifecyle
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * lifecycle
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_marker_modal, container, false);
    }

    /**
     * lifecycle - render elements here
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        configureDescription();
        configureCategory();
        configureCloseButton();
        configureDeleteButton();
        configureMedia();
        configureName();
    }

    /**
     * To be executed if response from web service is null
     */
    @Override
    public void handleTokenExpiration(){
        JWTToken.removeTokenSharedPref(Objects.requireNonNull(this.getActivity()));
    }

    /**
     * refresh map after successful response from web service
     * @param response
     */
    @Override
    public void closeUserPost(boolean response) {
        if (response) {
            Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
            getParentFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.post_delete_body), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Seperately render image from other data
     * Utilises once opening marker from notification
     * @param encodedString base 64 string of image
     */
    @Override
    public void addImageToModal(String encodedString) {
        if (TextUtils.isEmpty(encodedString)) {
            Toast.makeText(this.getContext(), "Refresh map data", Toast.LENGTH_LONG).show();
        } else {
            this.markerModalPresenter.updateModalEncodedImage(encodedString);
            this.renderImage();
        }
    }

    /**
     * return from modal to main activity
     */
    @Override
    public void handleCloseButtonClick() {
        getParentFragmentManager().popBackStack();
    }

    /**
     * close image back to marker modal
     * @param dialog dialog instance
     */
    @Override
    public void handleImageDialogClose(Dialog dialog) {
        dialog.dismiss();
    }

    /**
     * Open full screen of image
     * @param dialog dialog instance
     */
    @Override
    public void handleImageClick(Dialog dialog) {
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        dialog.show();
    }

    /**
     * Render dialog to remove marker
     * Colors rendered from colors.xml
     */
    @Override
    public void createMarkerDeletionDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Marker")
                .setMessage("Are you sure you want to delete this post?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        markerModalPresenter.makeApiCallDeleteMarker();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show()
                .getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.error_red));
    }

    /**
     * Fragment context
     * @return
     */
    @Override
    public Context getApplicationContext() {
        return this.getContext();
    }

    /**
     * Configure XMl
     * Image handling from XML to object
     */
    void renderImage() {
        this.markerModalPresenter.decodeModalImage();

        ImageView markerImage = (ImageView) Objects.requireNonNull(this.getView()).findViewById(R.id.markerImage);
        markerImage.setImageBitmap(markerModalPresenter.getModal());

        Dialog dialog = new Dialog(Objects.requireNonNull(this.getContext()));
        dialog.setContentView(R.layout.image_dialog);

        ImageButton dialogClose = (ImageButton) dialog.findViewById(R.id.dialogClose);
        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerModalPresenter.handleImageDialogClose(dialog);
            }
        });

        PhotoView dialogImage = (PhotoView) dialog.findViewById(R.id.dialogImage);
        dialogImage.setImageBitmap(markerModalPresenter.getModal());

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        }

        markerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerModalPresenter.handleImageClick(dialog);
            }
        });
    }

    /**
     * Configure XML
     */
    void configureCloseButton() {
        ImageButton modalCloseButton = (ImageButton) Objects.requireNonNull(this.getView()).findViewById(R.id.modalCloseButton);
        modalCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerModalPresenter.handleCloseButtonClick();
            }
        });
    }

    /**
     * Configure XML
     */
    void configureCategory() {
        TextView modalCategory = Objects.requireNonNull(this.getView()).findViewById(R.id.modalCategory);
        String category = this.markerModalPresenter.getMarker().getCategory();
        category = category.substring(0, 1).toUpperCase() + category.substring(1);

        modalCategory.setText(category);
    }

    /**
     * Configure XML
     */
    void configureDescription() {
        String description = this.markerModalPresenter.getMarker().getDescription();
        TextView modalDescription = (TextView) Objects.requireNonNull(this.getView()).findViewById(R.id.modalDescription);

        modalDescription.setText(description);
    }

    /**
     * Configure XML
     */
    void configureName() {
        this.markerModalPresenter.updateModalPostName();
        TextView text = Objects.requireNonNull(this.getView()).findViewById(R.id.marker_user_name);

        if (this.markerModalPresenter.getMarker().getUserId() == LoginPreferenceData.getUserId(this.getContext())) {
            text.setText(markerModalPresenter.getUserPost());
        } else {
            text.setText(markerModalPresenter.getAltName());
        }
    }

    /**
     * Configure XML
     */
    void configureMedia() {
        if (this.markerModalPresenter.getMarker().getEncodedImage() == null) {
            markerModalPresenter.makeApiCallCreateGetMarkerImage();
        } else {
            renderImage();
        }
    }

    /**
     * Configure XML
     */
    void configureDeleteButton() {
        Button markerDeleteButton = (Button) Objects.requireNonNull(this.getView()).findViewById(R.id.markerDeleteButton);

        if (this.markerModalPresenter.getMarker().getUserId() == LoginPreferenceData.getUserId(this.getContext())) {
            markerDeleteButton.setVisibility(View.VISIBLE);
            configureMarkerDeleteDialog(markerDeleteButton);
        }
    }

    /**
     * Configure XML
     * @param markerDeleteButton xml
     */
    void configureMarkerDeleteDialog(Button markerDeleteButton) {
        markerDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerModalPresenter.createMarkerDeletionDialog();
            }
        });
    }
}