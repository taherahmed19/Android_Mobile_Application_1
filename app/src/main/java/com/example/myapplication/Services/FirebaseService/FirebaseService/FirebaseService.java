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

    /**
     * new token generated when application is installed
     * @param token
     */
    @Override
    public void onNewToken(@NonNull String token) {
        passTokenToActivity(token);
        Log.d("Print", "New token " + token);
    }

    /**
     * message received from Firebase for notification
     * @param remoteMessage
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        passMessageToActivity(remoteMessage);
    }

    /**
     * token needed once logging in - send to web service from activity's model
     * @param token
     */
    private void passTokenToActivity(@NonNull String token){
        Intent intent = new Intent(LoginActivity.class.toString());
        intent.putExtra("token", token);
        sendBroadcast(intent);
    }

    /**
     * need to pass data to fragment to enable usage - render notifiation
     * @param remoteMessage
     */
    private void passMessageToActivity(RemoteMessage remoteMessage){
        userId = remoteMessage.getData().get("userId");
        markerId = remoteMessage.getData().get("markerId");
        category = remoteMessage.getData().get("category");
        description = remoteMessage.getData().get("description");
        lat = remoteMessage.getData().get("lat");
        lng = remoteMessage.getData().get("lng");
        firstName = remoteMessage.getData().get("firstName");
        lastName = remoteMessage.getData().get("lastName");

        Log.d("Print", "Received " + remoteMessage.getData().toString());

        //if app not visible custom notifiations not necessary - generate regular notification
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

    /**
     * add data to be passed to fragment
     * @param intent instance
     * @param openNotification notification setting
     * @param voiceEnabled notification setting
     */
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
    }

    /**
     * detect notification settings from local storage
     * if false in app setting = true
     * @return setting boolean
     */
    boolean isVoiceNotificationEnabled(){
        SharedPreferences settingsPreference = this.getSharedPreferences("Radius_Marker_Settings", 0);
        return settingsPreference.getBoolean("voiceNotifications", false);
    }
}
