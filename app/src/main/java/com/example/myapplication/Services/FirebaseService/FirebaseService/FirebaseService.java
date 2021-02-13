package com.example.myapplication.Services.FirebaseService.FirebaseService;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.Activities.BlankActivity.BlankActivity;
import com.example.myapplication.Activities.LoginActivity.LoginActivity;
import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.Application.App;
import com.example.myapplication.Fragments.MainMapFragment.MainMapFragment;
import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Handlers.BackgroundNotificationHandler.BackgroundNotificationHandler;
import com.example.myapplication.HttpRequest.HttpFirebaseToken.HttpFirebaseToken;
import com.example.myapplication.R;
import com.example.myapplication.Services.FirebaseService.TTSService.TTSService;
import com.example.myapplication.SharedPreference.LoginPreferenceData.LoginPreferenceData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
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

        Log.d("Remote message", userId + " " + markerId + " " + lat + " " + lng);

        if(!App.APP_VISIBLE){
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.putExtra("userId", userId);
            notificationIntent.putExtra("markerId", markerId);
            notificationIntent.putExtra("category", category);
            notificationIntent.putExtra("description", description);
            notificationIntent.putExtra("lat", lat);
            notificationIntent.putExtra("lng", lng);
            notificationIntent.putExtra("firstName", firstName);
            notificationIntent.putExtra("lastName", lastName);
            notificationIntent.putExtra("rating", rating);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(this, 100000, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_marker)
                    .setContentTitle("title")
                    .setContentText("description")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(intent)
                    .build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(1, notification);

//            BackgroundNotificationHandler backgroundNotificationHandler =
//                    new BackgroundNotificationHandler(this, category, description, lat, lng);
        }else{
            Intent intent = new Intent(MapFragment.class.toString());
            intent.putExtra("openNotification", 0);
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
}
