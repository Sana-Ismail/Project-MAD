package com.example.reminder_app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class channels extends Application {

    public static final String CHANNEL_1_ID = "Channel1";


    @Override
    public void onCreate() {
        super.onCreate();

        creatingNotificationChannels();

    }

    public void creatingNotificationChannels(){
        // check whether the app is 26 oreo or higher. Because channels cant be made in lower versions.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){

            // building notification channel:

            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "channel1",  // name of channel
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel1.setDescription("This is the description of channel1");

            // tell notification manager about channels

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }

    }
}
