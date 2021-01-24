package com.example.myapplication.Models.MediaItem;

import android.view.View;

public class MediaItem {

    protected View media;
    protected String mediaUrl;

    public MediaItem(View media, String mediaUrl) {
        this.media = media;
        this.mediaUrl = mediaUrl;
    }

    public View configure(MediaItem mediaItems, int position){ return null; }

    public void reset(View view){ }

    public void load(View view){ }

    public View getMedia() {
        return media;
    }

    public void setMedia(View media) {
        this.media = media;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}
