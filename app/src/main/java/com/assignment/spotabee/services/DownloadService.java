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
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.widget.Toast;

import com.assignment.spotabee.KeyChain;
import com.assignment.spotabee.R;
import com.assignment.spotabee.receivers.DownloadReceiver;


// * Created by Lauren on 4/25/2018.
// * (Download Manager tutorial) Reference: https://www.codeproject.com/Articles/1112730/Android-Download-Manager-Tutorial-How-to-Download
// */

public class DownloadService extends Service {
    //Constants used for foreground notification
    public static final String SERVICE_CHANNEL_ID = "com.assignment.spotabee.service";
    private long downloadReference;
    private DownloadManager downloadManager;
    private DownloadReceiver downloadReceiver;

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

        downloadManager = (DownloadManager) getBaseContext().getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setTitle("Help the Bees");
        request.setDescription("Guide on helping to preserve the bee population");

        request.setDestinationInExternalFilesDir(getBaseContext(),
                Environment.DIRECTORY_DOWNLOADS,"Bee_Guide.pdf");

        downloadReference = downloadManager.enqueue(request);
        getBaseContext()
                .getSharedPreferences("com.assignment.spotabee", MODE_PRIVATE)
                .edit()
                .putLong("download_reference", downloadReference)
                .apply();


        check_download_Status(downloadReference);
        this.downloadReference = downloadReference;
        return downloadReference;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service is starting", Toast.LENGTH_SHORT).show();

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




    @Override
    public IBinder onBind(Intent intent) {
        downloadReference = intent.getLongExtra("download_id", 1);
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void check_download_Status(long downloadReference) {

        DownloadManager.Query pdfDownloadQuery = new DownloadManager.Query();

        pdfDownloadQuery.setFilterById(downloadReference);

        Cursor cursor = downloadManager.query(pdfDownloadQuery);
        if(cursor.moveToFirst()){
            downloadStatus(cursor, downloadReference);
        }
    }

    private void downloadStatus(Cursor cursor, long downloadId){

        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        //column for reason code if the download failed or paused
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);

        String statusText = "";
        String reasonText = "";

        switch(status){
            case DownloadManager.STATUS_FAILED:
                statusText = getString(R.string.status_failed);
                switch(reason){
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = getString(R.string.error_cannot_resume);
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = getString(R.string.error_device_not_found);
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = getString(R.string.error_file_already_exists);
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = getString(R.string.error_file_error);
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = getString(R.string.error_http_error);
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = getString(R.string.error_insufficient_space);
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = getString(R.string.error_too_many_redirects);
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = getString(R.string.error_unhandled_exception);
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = getString(R.string.error_unknown);
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = getString(R.string.status_paused);
                switch(reason){
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = getString(R.string.paused_queue_for_wifi);
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = getString(R.string.paused_unknown);
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = getString(R.string.paused_waiting_for_network);
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = getString(R.string.paused_waiting_to_retry);
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = getString(R.string.status_pending);
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = getString(R.string.status_running);
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = getString(R.string.status_successful);
                reasonText = getString(R.string.successfully_downloaded);

                break;
        }

        if(downloadId == downloadReference) {

            Toast toast = Toast.makeText(getBaseContext(),
                    getString(R.string.pdf_download) + "\n" + statusText + reasonText,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        }
    }
}

