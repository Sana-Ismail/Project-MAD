package com.example.reminder_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReciever extends BroadcastReceiver {
    @Override         // we can execute the code here without opening our app
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("toastmsg");
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();    // only works when inside the application
    }
}
