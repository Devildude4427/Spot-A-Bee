package com.assignment.spotabee.receivers;
/**
 * Made by: C1769948
 */

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
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.assignment.spotabee.KeyChain;
import com.assignment.spotabee.MainActivity;
import com.assignment.spotabee.R;

import static android.content.Context.DOWNLOAD_SERVICE;
import static java.security.AccessController.getContext;

public class DownloadReceiver extends BroadcastReceiver {
    private static final String TAG = "DownloadReceiver debug";
    public static final String CHANNEL_ID = "com.assignment.spotabee";
    public static final int NOTIFICATION_ID = 1;
    private Context context;
    private boolean haveReceived = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.d(TAG, "We are in onReceive");
        haveReceived = true;
        Toast downloadSuccessful = Toast.makeText(context, "Download Complete", Toast.LENGTH_SHORT);
        downloadSuccessful.setGravity(Gravity.TOP, 25, 400);
        downloadSuccessful.show();
        makeNotifications();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager notificationManager, String description) {

        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            CharSequence name = "Spot a Bee";

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);

            notificationManager.createNotificationChannel(channel);
        }
    }


    private Notification createNotification() {

        Intent fileIntent = new Intent(Intent.ACTION_VIEW);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        // Grabs the Uri for the file that was downloaded.
        long downloadReference = context.getSharedPreferences("com.assignment.spotabee", Context.MODE_PRIVATE)
                .getLong("download_reference", 1);

        Uri mostRecentDownload =
                downloadManager.getUriForDownloadedFile(downloadReference);
        // DownloadManager stores the Mime Type. Makes it really easy for us.
        String mimeType =
                downloadManager.getMimeTypeForDownloadedFile(downloadReference);
//        String mimeType = "application/pdf";
        fileIntent.setDataAndType(mostRecentDownload, mimeType);
        fileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, fileIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingOpenFile = PendingIntent.getActivity(context, 0, fileIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent goToDownloads = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        PendingIntent pendingGoToDownloads = PendingIntent.getActivity(context, 0, goToDownloads, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_content_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) //Set priority for backwards compatibility
                .addAction(0, context.getString(R.string.open_your_guide), pendingGoToDownloads) //Sets no icon in this example
                .setContentIntent(pendingGoToDownloads)
                .setAutoCancel(true);


        return mBuilder.build();
    }


    public void makeNotifications() {

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nManager == null)
            Toast.makeText(context, "Unable to post notification", Toast.LENGTH_SHORT).show();
        else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                createNotificationChannel(nManager, context.getString(R.string.open_your_spot_a_bee_guide));

            Notification notification = createNotification();

            nManager.notify(NOTIFICATION_ID, notification);
        }
    }

    public boolean getHasReceived(){
        return haveReceived;
    }

}
