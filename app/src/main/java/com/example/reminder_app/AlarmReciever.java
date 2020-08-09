package com.example.reminder_app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import  android.support.v4.app.*;



public class AlarmReciever extends BroadcastReceiver {

    private NotificationManagerCompat notificationManagerCompat;

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        // when you click on the notif:
        Intent activityIntent = new Intent(context, MainActivity.class);
        // use intents to send the contents back, or use database
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, activityIntent,0);   //

        // For snooz btn
        Intent broadcastIntent = new Intent(context,NotificationReciever.class);
        broadcastIntent.putExtra("toastmsg", description);
        PendingIntent actionIntent = PendingIntent.getBroadcast(context, 0 ,broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManagerCompat = NotificationManagerCompat.from(context);

        Notification notification = new NotificationCompat.Builder(context,channels.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .addAction(R.drawable.ic_notify,"Snooze", actionIntent)
                .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000})
                .setColor(Color.CYAN)
                .setContentIntent(pendingIntent)      // what happens when clicked on notif
                .setAutoCancel(true)
                .build(); // shall buiuld this notif

        notificationManagerCompat.notify(1, notification);
        Toast.makeText(context, "Reminder set", Toast.LENGTH_SHORT).show();

    }
}
