package com.assignment.spotabee;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.widget.Toast;

import static android.app.Service.START_STICKY;
import static android.provider.Settings.Global.getString;
import static java.security.AccessController.getContext;


// * Created by Lauren on 4/25/2018.
// *
// */

public class ScreenService extends Service {
    //Constants used for foreground notification
    public static final String SERVICE_CHANNEL_ID = "com.assignment.spotabee.service";
    private static final int SERVICE_NOTIFICATION_ID = 900;
    public static final String CHANNEL_ID = "com.assignment.spotabee.type11";
    public static final int NOTIFICATION_ID = 1;
    private IBinder mBinder;
    private long downloadReference;
    private DownloadManager downloadManager;

    //Empty constructor
    public ScreenService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        downloadManager = (DownloadManager) getBaseContext().getSystemService(DOWNLOAD_SERVICE);
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

    private long downloadData (Uri uri) {

        downloadManager = (DownloadManager) getBaseContext().getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setTitle("Help the Bees");
        request.setDescription("Guide on helping to preserve the bee population");

        request.setDestinationInExternalFilesDir(getBaseContext(),
                Environment.DIRECTORY_DOWNLOADS,"Bee_Guide.pdf");

        downloadReference = downloadManager.enqueue(request);
        getBaseContext()
                .getSharedPreferences("pref", MODE_PRIVATE)
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
        this.registerReceiver(new Receiver(), filter);
    }


    @Override
    public IBinder onBind(Intent intent) {
//        downloadReference = intent.getLongExtra("download_id", 1);
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
                statusText = "Status: FAILED";
                switch(reason){
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "Error: Cannot resume";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "Error: Device not found.";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "Error: File already exists.";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "Error: File error";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "Error: HTTP error";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "Error: Insufficient space";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "Error: Too many redirects";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "Error: Unhandled HTTP exception";
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "Error: Unknown";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "Status paused.";
                switch(reason){
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = "Paused. Queued for wifi.";
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = "Paused. Unknown.";
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = "Paused. Waiting for network.";
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = "Paused. Waiting to retry.";
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "Status pending";
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = "Status running";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "Status successful";
                reasonText = "Successfully downloaded";

                break;
        }

        if(downloadId == downloadReference) {

//            Toast toast = Toast.makeText(getBaseContext(),
//                    "PDF download:" + "\n" + statusText + reasonText,
//                    Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.TOP, 25, 400);
//            toast.show();
        }
    }
}

