package com.assignment.spotabee;


import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.widget.Toast;

import static android.content.Context.DOWNLOAD_SERVICE;
import static java.security.AccessController.getContext;

public class Receiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "com.assignment.spotabee.type11";
    public static final int NOTIFICATION_ID = 1;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Toast downloadSuccessful = Toast.makeText(context, "Download Complete", Toast.LENGTH_SHORT);
        downloadSuccessful.setGravity(Gravity.TOP, 25, 400);
        downloadSuccessful.show();
        makeNotifications();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createAnExampleNotificationChannel(NotificationManager notificationManager, String description) {

        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            CharSequence name = "Spot a Bee";

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);

            notificationManager.createNotificationChannel(channel);
        }
    }


    private Notification createExampleNotification() {

        Intent fileIntent = new Intent(Intent.ACTION_VIEW);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        // Grabs the Uri for the file that was downloaded.
        Uri mostRecentDownload =
                downloadManager.getUriForDownloadedFile(DownloadReference.getDownloadReference());
        // DownloadManager stores the Mime Type. Makes it really easy for us.
        String mimeType =
                downloadManager.getMimeTypeForDownloadedFile(DownloadReference.getDownloadReference());
//        String mimeType = "application/pdf";
        fileIntent.setDataAndType(mostRecentDownload, mimeType);
        fileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, fileIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingOpenFile = PendingIntent.getActivity(context, 0, fileIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle("Spot a Bee")
                .setContentText("Help the Bees")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) //Set priority for backwards compatibility
                .addAction(0, "Open your guide", pendingOpenFile) //Sets no icon in this example
                .setContentIntent(pendingOpenFile)
                .setAutoCancel(true);


        return mBuilder.build();
    }


    public void makeNotifications() {

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nManager == null)
            Toast.makeText(context, "Unable to post notification", Toast.LENGTH_SHORT).show();
        else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                createAnExampleNotificationChannel(nManager, "Open your Spot a Bee PDF");

            Notification exampleNotification = createExampleNotification();

            nManager.notify(NOTIFICATION_ID, exampleNotification);
        }
    }

}
