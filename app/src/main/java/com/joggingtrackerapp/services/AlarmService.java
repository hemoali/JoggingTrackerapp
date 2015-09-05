package com.joggingtrackerapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.joggingtrackerapp.R;

/**
 * Created by ibrahimradwan on 9/5/15.
 */
public class AlarmService extends Service {
    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        if (intent != null) {
            Log.e("TAG", "TA");
            String email = intent.getStringExtra("email");
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification n;
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                Notification.Builder b = new Notification.Builder(this)
                        .setContentTitle("Weekly Report!")
                        .setContentText("Weekly Report: " + email).setSound(alarmSound)
                        .setSmallIcon(R.drawable.ic_notification).setAutoCancel(true);

                n = b.build();

            } else {
                n = new Notification.Builder(this)
                        .setContentTitle("Weekly Report!")
                        .setContentText("Weekly Report: " + email).setSound(alarmSound)
                        .setSmallIcon(R.drawable.ic_notification).setAutoCancel(true).getNotification();
            }
            notificationManager.notify(0, n);

        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }
}
