package com.example.myapplication.Services;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.HttpRequest.HttpFirebaseToken.HttpFirebaseToken;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("Print", "Firebase token " + token);
        new Runnable() {
            @Override
            public void run() {
                HttpFirebaseToken httpFirebaseToken = new HttpFirebaseToken(getApplicationContext(), LoginPreferenceData.getUserId(getApplicationContext()), token);
                httpFirebaseToken.execute();
            }
        };
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
         String markerId = remoteMessage.getData().get("markerId");
         String lat = remoteMessage.getData().get("lat");
         String lng = remoteMessage.getData().get("lng");

        Log.d("Print", "Remote message " + lat + " " + lng + " = " + markerId);
    }
}
