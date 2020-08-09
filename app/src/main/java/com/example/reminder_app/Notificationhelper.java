package com.example.reminder_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class Notificationhelper extends ContextWrapper {

    public static final String CHANNEL1_ID = "channel1_id";
    public static final String CHANNEL1_NAME = "channel_1";
    public static final String CHANNEL2_ID = "channel2_id";
    public static final String CHANNEL2_NAME = "channel_2";

    private NotificationManager notificationManager;

    public Notificationhelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            create_channel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void create_channel(){
        NotificationChannel channel_one = new NotificationChannel(CHANNEL1_ID, CHANNEL1_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel_one.enableLights(true);
        channel_one.enableVibration(true);
        channel_one.setLightColor(R.color.colorAccent);
        channel_one.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);     // look up this to find more

        getManager().createNotificationChannel(channel_one);

    }

    public NotificationManager getManager(){
        if (notificationManager==null){
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return notificationManager;
    }

    public NotificationCompat.Builder getChannel1Notificatoin(String title, String message){

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL1_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000})
                .setSmallIcon(R.drawable.ic_clock);
    }

    public NotificationCompat.Builder getChannelNotificatoin(){

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL1_ID)
                .setContentTitle("This is title")
                .setContentText("This is message")
                .setSmallIcon(R.drawable.ic_clock);
    }

}
