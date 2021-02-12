package com.example.myapplication.Handlers.MarkerModalFragmentHandler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Adapters.MediaPageAdapter.MediaPageAdapter;
import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.HttpRequest.HttpMarkerDelete.HttpMarkerDelete;
import com.example.myapplication.HttpRequest.HttpMarkerImage.HttpMarkerImage;
import com.example.myapplication.Models.ImageItem.ImageItem;
import com.example.myapplication.Models.MediaItem.MediaItem;
import com.example.myapplication.Models.VideoItem.VideoItem;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Utils.Tools.Tools;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MarkerModalFragmentHandler {

    MarkerModalFragment markerModalFragment;

    public MarkerModalFragmentHandler(MarkerModalFragment markerModalFragment) {
        this.markerModalFragment = markerModalFragment;
    }

    public void configure(){
        configureRating();
        configureMedia();
        configureCategory();
        configureDescription();
        configureName();
        configureCloseButton();
        configureDeleteButton();
    }

    public void updateRatingValue(int rating){
        TextView markerRating = (TextView) this.markerModalFragment.getView().findViewById(R.id.markerRating);
        markerRating.setText(String.valueOf(rating));
    }

    public void handleMarkerImage(String encodedString){
        if(TextUtils.isEmpty(encodedString)){
            Toast.makeText(this.markerModalFragment.getContext(), "Refresh map data", Toast.LENGTH_LONG).show();
        }else{
            this.renderImage(encodedString);
            this.markerModalFragment.getMarker().setEncodedImage(encodedString);
        }
    }

    void configureCloseButton(){
        ImageButton modalCloseButton = (ImageButton) this.markerModalFragment.getView().findViewById(R.id.modalCloseButton);
        modalCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerModalFragment.getFragmentManager().popBackStack();
            }
        });
    }

    void configureRating(){
        TextView markerRating = (TextView) this.markerModalFragment.getView().findViewById(R.id.markerRating);
        String rating = String.valueOf(this.markerModalFragment.getMarker().getRating());
        markerRating.setText(rating);
    }

    void configureCategory() {
        TextView modalCategory = this.markerModalFragment.getView().findViewById(R.id.modalCategory);
        String category = this.markerModalFragment.getMarker().getCategory();
        category = category.substring(0, 1).toUpperCase() + category.substring(1);

        modalCategory.setText(category);
    }

    void configureDescription(){
        String description = this.markerModalFragment.getMarker().getDescription();
        TextView modalDescription = (TextView) this.markerModalFragment.getView().findViewById(R.id.modalDescription);

        //modalDescription.setText(description);
    }

    void configureName(){
        String firstName = this.markerModalFragment.getMarker().getFirstName();
        String lastName = this.markerModalFragment.getMarker().getLastName();

        String concatName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1) + " " + lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
        String userPost = "Your post";

        TextView text = this.markerModalFragment.getView().findViewById(R.id.marker_user_name);

        if(this.markerModalFragment.getMarker().getUserId() == LoginPreferenceData.getUserId(this.markerModalFragment.getContext())){
            text.setText(userPost);
        }else{
            text.setText(concatName);
        }

    }
    void configureMedia() {
        if (this.markerModalFragment.getMarker().getEncodedImage() == null) {
            HttpMarkerImage httpMarkerImage = new HttpMarkerImage(this.markerModalFragment.getContext(), this.markerModalFragment.getMarker().getId(), this.markerModalFragment);
            httpMarkerImage.execute();
        } else {
            renderImage(this.markerModalFragment.getMarker().getEncodedImage());
        }
    }

    void renderImage(String encodeImage){
        byte[] decodedString = Base64.decode(encodeImage, Base64.DEFAULT);
        Bitmap image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ImageView markerImage = (ImageView) this.markerModalFragment.getView().findViewById(R.id.markerImage);
        markerImage.setImageBitmap(image);

        Dialog dialog = new Dialog(Objects.requireNonNull(this.markerModalFragment.getContext()));
        dialog.setContentView(R.layout.image_dialog);

        ImageButton dialogClose = (ImageButton) dialog.findViewById(R.id.dialogClose);
        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        PhotoView dialogImage = (PhotoView) dialog.findViewById(R.id.dialogImage);
        dialogImage.setImageBitmap(image);

        Window window = dialog.getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        markerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                dialog.show();
            }
        });
    }

    void configureDeleteButton(){
        Button markerDeleteButton = (Button) this.markerModalFragment.getView().findViewById(R.id.markerDeleteButton);

        if(this.markerModalFragment.getMarker().getUserId() == LoginPreferenceData.getUserId(this.markerModalFragment.getContext())){
            markerDeleteButton.setVisibility(View.VISIBLE);
            configureMarkerDeleteDialog(markerDeleteButton);
        }
    }

    void configureMarkerDeleteDialog(Button markerDeleteButton){
        markerDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(markerModalFragment.getContext())
                        .setTitle("Delete Marker")
                        .setMessage("Are you sure you want to delete this post?")
                        .setCancelable(false)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                HttpMarkerDelete httpMarkerDelete = new HttpMarkerDelete(markerModalFragment.getContext(), markerModalFragment.getMarker().getId(), markerModalFragment);
                                httpMarkerDelete.execute();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show()
                        .getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(markerModalFragment.getResources().getColor(R.color.error_red));
            }
        });
    }
}
