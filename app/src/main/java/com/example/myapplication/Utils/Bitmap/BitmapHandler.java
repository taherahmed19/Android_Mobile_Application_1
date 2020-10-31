package com.example.myapplication.Utils.Bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class BitmapHandler {

    public static Bitmap getGoogleMapThumbnail(String imageUrl){
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
