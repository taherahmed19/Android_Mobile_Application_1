package com.example.myapplication.Models.ImageItem;

import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.Models.MediaItem.MediaItem;
import com.squareup.picasso.Picasso;

public class ImageItem extends MediaItem {

    public ImageItem(View mediaType, String mediaUrl) {
        super(mediaType, mediaUrl);
    }

    @Override
    public View configure(MediaItem mediaItem, int position){
        ImageView view = (ImageView) mediaItem.getMedia();

        if(view != null){
            view.setTag(position);

            Picasso.get()
                    .load(mediaItem.getMediaUrl())
                    .fit()
                    .centerCrop()
                    .into(view);
        }

        return view;
    }
}
