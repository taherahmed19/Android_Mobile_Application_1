package com.example.myapplication.Utils.Tools;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Models.SpinnerItem.SpinnerItem;
import com.example.myapplication.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Tools {

    public static DisplayMetrics ScreenDimensions(Fragment fragment){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(fragment.getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics;
    }

    public static BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public static int pixelsToDP(int sizeInDp, Resources resources){
        float scale = resources.getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);

        return dpAsPixels;
    }

    public static String encodeString(String input){
        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void HideKeyboard(Activity activity){
//        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static int CategoryMarkerSwitch(String category){
        int categoryItem = -1;

        switch (category.toLowerCase()){
            case "environment":
                categoryItem = 1;
                break;
            case "weather":
                categoryItem = 2;
                break;
            case "people":
                categoryItem = 3;
                break;
            default:
                categoryItem = 0;
                break;
        }

        return categoryItem;
    }

    public static int MarkerPicker(int category){
        int drawable = -1;

        switch (category) {
            case 1:
                drawable = R.drawable.ic_alerts_feed_marker_environment;
                break;
            case 2:
                drawable = R.drawable.ic_alerts_feed_marker_weather;
                break;
            case 3:
                drawable = R.drawable.ic_alerts_feed_marker_people;
                break;
            default:
                break;
        }

        return drawable;
    }

    public static int MarkerPicker(String category){
        int drawable = -1;

        switch (category) {
            case "environment":
                drawable = R.drawable.ic_marker_green_maps;
                break;
            case "weather":
                drawable = R.drawable.ic_marker_blue_maps;
                break;
            case "people":
                drawable = R.drawable.ic_marker_purple_maps;
                break;
            default:
                drawable = R.drawable.ic_alerts_feed_marker;
                break;
        }

        return drawable;
    }

    public static String ArrayListToJson(ArrayList arrayList){
        return new Gson().toJson(arrayList).toString();
    }

    public static ArrayList<String> JsonToArrayList(String json) throws JSONException {
        ArrayList<String> arrayList = new ArrayList();
        JSONArray jsonArray = new JSONArray(json);

        try{
            for(int i = 0; i < jsonArray.length(); i++){
                arrayList.add(jsonArray.get(i).toString());
            }
        }catch (Exception e){
        }

        return arrayList;
    }

}
