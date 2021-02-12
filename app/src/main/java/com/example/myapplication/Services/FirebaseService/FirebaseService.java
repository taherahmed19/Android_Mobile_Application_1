package com.example.myapplication.Services.FirebaseService;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.Activities.LoginActivity.LoginActivity;
import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.Fragments.MainMapFragment.MainMapFragment;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.HttpRequest.HttpFirebaseToken.HttpFirebaseToken;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("Print", "Firebase token " + token);
        passTokenToActivity(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        passMessageToActivity(remoteMessage);
    }

    void passTokenToActivity(@NonNull String token){
        Intent intent = new Intent(LoginActivity.class.toString());
        intent.putExtra("token", token);
        sendBroadcast(intent);
    }

    void passMessageToActivity(RemoteMessage remoteMessage){
        String userId = remoteMessage.getData().get("userId");
        String markerId = remoteMessage.getData().get("markerId");
        String category = remoteMessage.getData().get("category");
        String description = remoteMessage.getData().get("description");
        String lat = remoteMessage.getData().get("lat");
        String lng = remoteMessage.getData().get("lng");
        String firstName = remoteMessage.getData().get("firstName");
        String lastName = remoteMessage.getData().get("lastName");
        String rating = remoteMessage.getData().get("rating");

        Log.d("Remote message", "");

        Intent intent = new Intent(MapFragment.class.toString());
        intent.putExtra("userId", userId);
        intent.putExtra("markerId", markerId);
        intent.putExtra("category", category);
        intent.putExtra("description", description);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("rating", rating);
        sendBroadcast(intent);
    }
}
