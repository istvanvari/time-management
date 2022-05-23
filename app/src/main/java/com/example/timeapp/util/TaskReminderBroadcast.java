package com.example.timeapp.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.example.timeapp.MainActivity;
import com.example.timeapp.R;

import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;

public class TaskReminderBroadcast extends BroadcastReceiver {
    private String CHANNEL_ID = "TaskReminder";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String text = bundle.getString("name");
        String time = bundle.getString("time");
        OffsetTime offsetTime;
        try {
            offsetTime = OffsetTime.parse(time);
        } catch (Exception e) {
            offsetTime = LocalDateTime.now().toLocalTime().atOffset(OffsetTime.now().getOffset());
        }
        int remindType = bundle.getInt("remindType");

        String title;
        if (remindType == 1)
            title = "You have a scheduled task right now!";
        else {
            title = "You have an upcoming task in ";
            switch (remindType) {
                case 2:
                    title += "5 minutes";
                    break;
                case 3:
                    title += "15 minutes";
                    break;
                case 4:
                    title += "30 minutes";
                    break;
                case 5:
                    title += "1 hour";
                    break;
            }
        }

        //Click on Notification
        Intent clickAction = new Intent(context, MainActivity.class);
        clickAction.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Notification Builder
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, clickAction, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "TaskReminder");


        mBuilder.setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setContentTitle(title)
                .setContentText(offsetTime.format(DateTimeFormatter.ofPattern("HH:mm")) + ": " + text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(CHANNEL_ID);
        notificationManager.createNotificationChannel(channel);
        mBuilder.setChannelId(CHANNEL_ID);

        notificationManager.notify(1, mBuilder.build());
    }
}
