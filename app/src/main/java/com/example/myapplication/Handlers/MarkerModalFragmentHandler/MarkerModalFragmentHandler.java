package com.example.myapplication.Handlers.MarkerModalFragmentHandler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Adapters.MediaPageAdapter.MediaPageAdapter;
import com.example.myapplication.Fragments.MarkerModalFragment.MarkerModalFragment;
import com.example.myapplication.HttpRequest.HttpMarkerDelete.HttpMarkerDelete;
import com.example.myapplication.Models.ImageItem.ImageItem;
import com.example.myapplication.Models.MediaItem.MediaItem;
import com.example.myapplication.Models.VideoItem.VideoItem;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.example.myapplication.Utils.Tools.Tools;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

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
        String category = "";
        int markerType = this.markerModalFragment.getMarker().getMarker();

        switch (markerType) {
            case 1:
                category = "Environment";
                break;
            case 2:
                category = "Weather";
                break;
            case 3:
                category = "People";
                break;
            default:
                category = "None";
                break;
        }

        modalCategory.setText(category);
    }

    void configureDescription(){
        String description = this.markerModalFragment.getMarker().getDescription();
        TextView modalDescription = (TextView) this.markerModalFragment.getView().findViewById(R.id.modalDescription);

        //modalDescription.setText(description);
    }

    void configureName(){
        String firstName = LoginPreferenceData.getUserFirstName(this.markerModalFragment.getContext());
        String lastName = LoginPreferenceData.getUserLastName(this.markerModalFragment.getContext());

        String concatName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1) + " " + lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
        String userPost = "Your post";

        TextView text = this.markerModalFragment.getView().findViewById(R.id.marker_user_name);

        if(this.markerModalFragment.getMarker().getUserId() == LoginPreferenceData.getUserId(this.markerModalFragment.getContext())){
            text.setText(userPost);
        }else{
            text.setText(concatName);
        }

    }
    void configureMedia(){
        String encodedImage = this.markerModalFragment.getMarker().getEncodedImage();
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ImageView markerImage = (ImageView) this.markerModalFragment.getView().findViewById(R.id.markerImage);
        markerImage.setImageBitmap(image);

        Dialog dialog = new Dialog(this.markerModalFragment.getContext());
        dialog.setContentView(R.layout.image_dialog);

        PhotoView dialogImage = (PhotoView) dialog.findViewById(R.id.dialogImage);
        dialogImage.setImageBitmap(image);

        markerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Print", "bitmap height " + image.getHeight() + " image view height " + dialogImage.getHeight());

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

    void configureMediaTest(){
        MediaItem[] mediaUrls = new MediaItem[]{
                new ImageItem(new ImageView(this.markerModalFragment.getContext()), "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg"),
                new VideoItem(new VideoView(this.markerModalFragment.getContext()), "http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4"),
                new VideoItem(new VideoView(this.markerModalFragment.getContext()), "http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4"),
                new ImageItem(new ImageView(this.markerModalFragment.getContext()), "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg")
        };

        ViewPager mediaViewPager = this.markerModalFragment.getView().findViewById(R.id.mediaViewPager);
        MediaPageAdapter adapter = new MediaPageAdapter(this.markerModalFragment.getContext(), mediaUrls);
        mediaViewPager.setAdapter(adapter);

        mediaViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                for(int i = 0; i < adapter.getCount(); i++){
                    View resetView = markerModalFragment.getView().findViewWithTag(i);
                    mediaUrls[i].reset(resetView);
                }

                View view = markerModalFragment.getView().findViewWithTag(position);
                mediaUrls[position].load(view);
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
                                sendMarkerDeleteRequest();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show()
                        .getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(markerModalFragment.getResources().getColor(R.color.error_red));
            }
        });
    }

    void sendMarkerDeleteRequest(){
        HttpMarkerDelete httpMarkerDelete = new HttpMarkerDelete(this.markerModalFragment.getContext(), this.markerModalFragment.getMarker().getId(), this.markerModalFragment);
        httpMarkerDelete.execute();
    }
}
