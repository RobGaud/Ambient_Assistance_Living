package utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.pervasivesystems.compasstest.R;

/**
 * Created by Andrea on 20/04/2016.
 */
public class Utils {
    public final static int ID_NOT_A = 3;
    public static int Notification(AppCompatActivity from_activity,Class to_activity, String title, String msg){
        // prepare intent which is triggered if the
        // notification is selected


        Intent intent = new Intent(from_activity, to_activity);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(from_activity,
                (int) System.currentTimeMillis(), intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(from_activity)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.blind)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(Uri.parse("android.resource://"+ from_activity.getPackageName()+"/"+R.raw.tethys))
                .build();


        NotificationManager notificationManager =
                (NotificationManager) from_activity.getSystemService(from_activity.NOTIFICATION_SERVICE);

        notificationManager.notify(ID_NOT_A, n);
        return ID_NOT_A;
    }
    public static void deleteNotification(int id,AppCompatActivity activity){
        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);

        notificationManager.cancel(id);
    }

    public static Notification Notification(int id,Service from_activity,
                                            Class to_activity, String title, String msg,String UUID){
        // prepare intent which is triggered if the
        // notification is selected


        Intent intent = new Intent(from_activity, to_activity);
        intent.putExtra("Region", UUID);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(from_activity,
                (int) System.currentTimeMillis(), intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n = new Notification.Builder(from_activity)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.blind)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(Uri.parse("android.resource://"+ from_activity.getPackageName()+"/"+R.raw.tethys))
                .build();


        NotificationManager notificationManager =
                (NotificationManager) from_activity.getSystemService(from_activity.NOTIFICATION_SERVICE);

        notificationManager.notify(id, n);

        return n;
    }
    public static void deleteNotification(int id,Service activity){
        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);

        notificationManager.cancel(id);
    }
    public static void deleteAllNotification(int id,Service activity) {
        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);

        notificationManager.cancelAll();
    }



    }
