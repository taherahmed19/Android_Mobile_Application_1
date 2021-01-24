package com.example.myapplication.Models.VideoItem;

import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

import com.example.myapplication.Models.MediaItem.MediaItem;

public class VideoItem extends MediaItem {

    public VideoItem(View mediaType, String mediaUrl) {
        super(mediaType, mediaUrl);
    }

    @Override
    public View configure(MediaItem mediaItem, int position){
        VideoView videoView = (VideoView) mediaItem.getMedia();

        if(videoView != null){
            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(!videoView.isPlaying()){
                        videoView.start();
                    }else{
                        videoView.pause();
                    }
                    return false;
                }
            });

            videoView.setTag(position);

        }

        return videoView;
    }

    @Override
    public void load(View view){
        if(view != null){
            VideoView videoView = (VideoView) view;
            if(!videoView.isPlaying()){
                videoView.setVideoPath(mediaUrl);
                videoView.start();
            }
        }
    }

    @Override
    public void reset(View view){
        if(view != null){
            VideoView videoView = (VideoView) view;
            if(videoView.isPlaying()){
                videoView.stopPlayback();
            }
        }
    }
}
