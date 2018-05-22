package com.assignment.spotabee.services;
/**
 * Made by: C1769948
 */
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.widget.Toast;

import com.assignment.spotabee.KeyChain;
import com.assignment.spotabee.R;
import com.assignment.spotabee.customutils.NetworkConnection;
import com.assignment.spotabee.receivers.DownloadReceiver;

import java.io.File;

import static com.assignment.spotabee.MainActivity.getContextOfApplication;


// *
// * (Download Manager tutorial) Reference: https://www.codeproject.com/Articles/1112730/Android-Download-Manager-Tutorial-How-to-Download
// */

public class DownloadService extends Service {
    //Constants used for foreground notification
    public static final String SERVICE_CHANNEL_ID = "com.assignment.spotabee.service";
    private long downloadReference;
    private DownloadManager downloadManager;
    private DownloadReceiver downloadReceiver;
    private boolean downloadSuccessful;
    private final IBinder mBinder = new LocalBinder();

    //Empty constructor
    public DownloadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        downloadReceiver = new DownloadReceiver();

        downloadManager = (DownloadManager) getBaseContext().getSystemService(DOWNLOAD_SERVICE);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nManager == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (nManager.getNotificationChannel(SERVICE_CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(
                        SERVICE_CHANNEL_ID,
                        getString(R.string.pdf_notification_channel_name),
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(getString(R.string.pdf_download_channel_description));

                nManager.createNotificationChannel(channel);
            }
        }


    }




    private long downloadData (Uri uri) {

        NetworkConnection nc = new NetworkConnection(getBaseContext());

        if(!nc.internetIsAvailable()){
            Toast.makeText(getContextOfApplication(),
                    R.string.internet_unavailable,
                    Toast.LENGTH_SHORT).show();
            stopSelf();
        } else {
            downloadManager = (DownloadManager) getBaseContext().getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);

            request.setTitle("Help the Bees");
            request.setDescription("Guide on helping to preserve the bee population");

            String fileName = Environment.DIRECTORY_DOWNLOADS + "Bee_Guide.pdf";
            String filePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/Bee_Guide.pdf";
            File downloadFile = new File(filePath);
            if(!downloadFile.exists()){
                request.setDestinationInExternalFilesDir(getBaseContext(),
                        Environment.DIRECTORY_DOWNLOADS,"Bee_Guide.pdf");

                downloadReference = downloadManager.enqueue(request);
                getBaseContext()
                        .getSharedPreferences("com.assignment.spotabee", MODE_PRIVATE)
                        .edit()
                        .putLong("download_reference", downloadReference)
                        .apply();


                this.downloadReference = downloadReference;
            } else {
                Toast.makeText(getBaseContext(), "You've already downloaded this file", Toast.LENGTH_SHORT).show();
                stopSelf();
            }
        }

        return downloadReference;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        downloadSuccessful = false;


        Uri uriForPdf = Uri.parse("https://friendsoftheearth.uk/sites/default/files/downloads/bees_booklet.pdf");
        downloadData(uriForPdf);

        registerScreenEvents();

        stopSelf();
        return START_STICKY;
    }

    private void registerScreenEvents(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        this.registerReceiver(downloadReceiver, filter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadReceiver);
    }

    public class LocalBinder extends Binder {

        public DownloadService getService() {
            // Return this instance of LocalService so clients can call public methods.
            return DownloadService.this;
        }
    }



    @Override
    public IBinder onBind(Intent intent) {
        downloadReference = intent.getLongExtra("download_id", 1);
        return mBinder;
    }

    public boolean isDownloadSuccessful(long downloadReference, Context context) {
        boolean success = false;

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadReference);
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Cursor cursor = manager.query(query);
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);

        if(status == DownloadManager.STATUS_SUCCESSFUL){
            success = true;
        }
        return success;
    }

}

