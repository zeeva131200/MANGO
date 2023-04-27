package com.example.testerapp;

import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMsgService extends FirebaseMessagingService {
    String title, msg;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //once msg is retrieved from a remote msg
        super.onMessageReceived(remoteMessage);
        title=remoteMessage.getData().get("Title");
        msg=remoteMessage.getData().get("Message");

        //generate notification, msg get from data
        NotificationCompat.Builder builder =  new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(msg);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
