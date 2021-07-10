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
    ImageButton upVoteButton;
    ImageButton downVoteButton;

    public MarkerModalFragment(Marker marker, ViewPager viewPager) {
        this.viewPager = viewPager;
        this.markerModalPresenter = new MarkerModalPresenter(this, marker);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_marker_modal, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        configureDescription();
        configureCategory();
        configureCloseButton();
        configureDeleteButton();
        configureMedia();
        configureName();
        configureRating();
        configureDownVoteButton();
        configureUpVoteButton();
        configureRatingButtons();
        configureUserRatingState();
    }

    @Override
    public void handleTokenExpiration(){
        JWTToken.removeTokenSharedPref(Objects.requireNonNull(this.getActivity()));
    }

    @Override
    public void updateModalRating(boolean response) {
        if (response) {
            this.markerModalPresenter.updateRating();
            this.markerModalPresenter.saveSettingsSharedPreference();
            this.updateRatingValue(this.markerModalPresenter.getRating());
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.post_rating_body), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void saveModalRatingState(int rating) {
        this.updateRatingValue(rating);
    }

    @Override
    public void closeUserPost(boolean response) {
        if (response) {
            Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
            getParentFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.post_delete_body), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void addImageToModal(String encodedString) {
        if (TextUtils.isEmpty(encodedString)) {
            Toast.makeText(this.getContext(), "Refresh map data", Toast.LENGTH_LONG).show();
        } else {
            this.markerModalPresenter.updateModalEncodedImage(encodedString);
            this.renderImage();
        }
    }

    @Override
    public void handleCloseButtonClick() {
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void handleImageDialogClose(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void handleImageClick(Dialog dialog) {
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        dialog.show();
    }

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

    @Override
    public Context getApplicationContext() {
        return this.getContext();
    }

    @Override
    public void handleUpVoteButtonClick() {
        markerModalPresenter.handleUpVoteButtonClick();
    }

    @Override
    public void handleDownVoteButtonClick() {
        markerModalPresenter.handleDownVoteButtonClick();
    }

    @Override
    public void setUpVote() {
        upVoteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_upvote_arrow_clicked));
        downVoteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_downvote_arrow));
    }

    @Override
    public void setDownVote() {
        downVoteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_downvote_arrow_clicked));
        upVoteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_upvote_arrow));
    }

    @Override
    public void removeVote() {
        upVoteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_upvote_arrow));
        downVoteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_downvote_arrow));
    }

    void updateRatingValue(int rating) {
        TextView markerRating = (TextView) Objects.requireNonNull(this.getView()).findViewById(R.id.markerRating);
        markerRating.setText(String.valueOf(rating));
    }

    void configureCloseButton() {
        ImageButton modalCloseButton = (ImageButton) Objects.requireNonNull(this.getView()).findViewById(R.id.modalCloseButton);
        modalCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerModalPresenter.handleCloseButtonClick();
            }
        });
    }

    void configureUpVoteButton() {
        this.upVoteButton = (ImageButton) Objects.requireNonNull(this.getView()).findViewById(R.id.upVoteButton);
    }

    void configureDownVoteButton() {
        this.downVoteButton = (ImageButton) Objects.requireNonNull(this.getView()).findViewById(R.id.downVoteButton);
    }

    void configureRatingButtons() {
        upVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerModalPresenter.handleUpVoteButtonClick();
            }
        });

        downVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerModalPresenter.handleDownVoteButtonClick();
            }
        });
    }

    void configureUserRatingState() {
        markerModalPresenter.configureUserRatingState();
    }

    void configureRating() {
        TextView markerRating = (TextView) Objects.requireNonNull(this.getView()).findViewById(R.id.markerRating);
        String rating = String.valueOf(this.markerModalPresenter.getMarker().getRating());
        markerRating.setText(rating);
    }

    void configureCategory() {
        TextView modalCategory = Objects.requireNonNull(this.getView()).findViewById(R.id.modalCategory);
        String category = this.markerModalPresenter.getMarker().getCategory();
        category = category.substring(0, 1).toUpperCase() + category.substring(1);

        modalCategory.setText(category);
    }

    void configureDescription() {
        String description = this.markerModalPresenter.getMarker().getDescription();
        TextView modalDescription = (TextView) Objects.requireNonNull(this.getView()).findViewById(R.id.modalDescription);

        modalDescription.setText(description);
    }

    void configureName() {
        this.markerModalPresenter.updateModalPostName();
        TextView text = Objects.requireNonNull(this.getView()).findViewById(R.id.marker_user_name);

        if (this.markerModalPresenter.getMarker().getUserId() == LoginPreferenceData.getUserId(this.getContext())) {
            text.setText(markerModalPresenter.getUserPost());
        } else {
            text.setText(markerModalPresenter.getAltName());
        }
    }

    void configureMedia() {
        if (this.markerModalPresenter.getMarker().getEncodedImage() == null) {
            markerModalPresenter.makeApiCallCreateGetMarkerImage();
        } else {
            renderImage();
        }
    }

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

    void configureDeleteButton() {
        Button markerDeleteButton = (Button) Objects.requireNonNull(this.getView()).findViewById(R.id.markerDeleteButton);

        if (this.markerModalPresenter.getMarker().getUserId() == LoginPreferenceData.getUserId(this.getContext())) {
            markerDeleteButton.setVisibility(View.VISIBLE);
            configureMarkerDeleteDialog(markerDeleteButton);
        }
    }

    void configureMarkerDeleteDialog(Button markerDeleteButton) {
        markerDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerModalPresenter.createMarkerDeletionDialog();
            }
        });
    }
}