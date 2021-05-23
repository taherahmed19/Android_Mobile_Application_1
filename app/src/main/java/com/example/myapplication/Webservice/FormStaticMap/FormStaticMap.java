package com.example.myapplication.Webservice.FormStaticMap;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.myapplication.Utils.Bitmap.BitmapHandler;
import com.example.myapplication.Utils.SSL.SSL;

public class FormStaticMap extends AsyncTask<String , Void ,String> {

    ImageView formLocationImage;
    Bitmap bitmap;

    public FormStaticMap(ImageView formLocationImage) {
        this.formLocationImage = formLocationImage;
        this.bitmap = null;
    }

    @Override
    protected String doInBackground(String... strings) {
        SSL.AllowSSLCertificates();

        this.bitmap = BitmapHandler.getGoogleMapThumbnail(strings[0]);

        return "";
    }

    @Override
    protected void onPostExecute(String str) {
        formLocationImage.setImageBitmap(this.bitmap);
    }
}
