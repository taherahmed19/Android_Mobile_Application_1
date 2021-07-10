package com.example.myapplication.Services.FirebaseService.FirebaseService;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.Activities.LoginActivity.LoginActivity;
import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.Application.App;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseService extends FirebaseMessagingService {

    String userId;
    String markerId;
    String category;
    String description;
    String lat;
    String lng;
    String firstName;
    String lastName;
    String rating;

    @Override
    public void onNewToken(@NonNull String token) {
        passTokenToActivity(token);
        Log.d("Print", "New token " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        passMessageToActivity(remoteMessage);
    }

    private void passTokenToActivity(@NonNull String token){
        Intent intent = new Intent(LoginActivity.class.toString());
        intent.putExtra("token", token);
        sendBroadcast(intent);
    }

    private void passMessageToActivity(RemoteMessage remoteMessage){
        userId = remoteMessage.getData().get("userId");
        markerId = remoteMessage.getData().get("markerId");
        category = remoteMessage.getData().get("category");
        description = remoteMessage.getData().get("description");
        lat = remoteMessage.getData().get("lat");
        lng = remoteMessage.getData().get("lng");
        firstName = remoteMessage.getData().get("firstName");
        lastName = remoteMessage.getData().get("lastName");
        rating = remoteMessage.getData().get("rating");


        if(!App.APP_VISIBLE){
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            Intent notificationIntent = new Intent(this, MainActivity.class);
            addIntentExtras(notificationIntent, 0, isVoiceNotificationEnabled());
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent intent = PendingIntent.getActivity(this, 100000, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_marker)
                    .setContentTitle("New " + category + " marker")
                    .setContentText(description)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(intent)
                    .build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(1, notification);
        }else{
            Intent intent = new Intent(MapFragment.class.toString());
            addIntentExtras(intent, 1, isVoiceNotificationEnabled());
            sendBroadcast(intent);
        }
    }

    private void addIntentExtras(Intent intent, int openNotification, boolean voiceEnabled){
        intent.putExtra("openNotification", openNotification);
        intent.putExtra("voiceEnabled", voiceEnabled);
        intent.putExtra("userId", userId);
        intent.putExtra("markerId", markerId);
        intent.putExtra("category", category);
        intent.putExtra("description", description);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("rating", rating);
    }

    boolean isVoiceNotificationEnabled(){
        SharedPreferences settingsPreference = this.getSharedPreferences("Radius_Marker_Settings", 0);
        return settingsPreference.getBoolean("voiceNotifications", false);
    }
}
