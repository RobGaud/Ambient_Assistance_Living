package com.pervasivesystems.notificationtest;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Andrea on 20/04/2016.
 */
public class Utils {
    private final static int ID_NOT_A = 3;
    public static int Notification(AppCompatActivity activity){
        // prepare intent which is triggered if the
        // notification is selected


        Intent intent = new Intent(activity, MainActivity.class);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(activity,
                (int) System.currentTimeMillis(), intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(activity)
                .setContentTitle("Bind People APP")
                .setContentText("oii c'è un beacon")
                .setSmallIcon(R.drawable.blind)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(Uri.parse("android.resource://"+ activity.getPackageName()+"/"+R.raw.tethys))
                .build();


        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);

        notificationManager.notify(ID_NOT_A, n);
        return ID_NOT_A;
    }
    public static void deleteNotification(int id,AppCompatActivity activity){
        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);

        notificationManager.cancel(id);
    }


}
