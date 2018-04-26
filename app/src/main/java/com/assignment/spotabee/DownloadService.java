package com.assignment.spotabee;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by Lauren on 4/25/2018.
 *
 */

public class DownloadService extends Service {
    //Constants used for foreground notification
    public static final String SERVICE_CHANNEL_ID = "com.assignment.spotabee.service";
    private static final int SERVICE_NOTIFICATION_ID = 900;

    //Empty constructor
    public DownloadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();


        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nManager == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (nManager.getNotificationChannel(SERVICE_CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(
                        SERVICE_CHANNEL_ID,
                        "Test Service",
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("Test");

                nManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, SERVICE_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle("Test title")
                .setContentText("Test content")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Notification n = mBuilder.build();

        startForeground(SERVICE_NOTIFICATION_ID, n);
        nManager.notify(SERVICE_NOTIFICATION_ID, n);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service is starting", Toast.LENGTH_SHORT).show();


        registerScreenEvents();

        return START_STICKY;
    }

    private void registerScreenEvents(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        this.registerReceiver(new DownloadServiceReceiver(), filter);
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
