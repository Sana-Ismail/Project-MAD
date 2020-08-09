package com.example.reminder_app;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class Alertreciever extends BroadcastReceiver {

    String title;
    String description;

    //int notification_id = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        //notification_id++;
        //ArrayList<Notificationhelper> notifArray = new ArrayList<Notificationhelper>();

        title = intent.getStringExtra("title");
        description = intent.getStringExtra("desc");

        Notificationhelper notificationhelper = new Notificationhelper(context);
        NotificationCompat.Builder nb = notificationhelper.getChannel1Notificatoin(title, description);
        notificationhelper.getManager().notify(1,nb.build());

        //notifArray.add(notificationhelper);
    }
}
