package com.assignment.spotabee;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;


public class FragmentOpenDownloadNotification extends Fragment {
    private View rootView;
    public static final String EXAMPLE_CHANNEL_ID = "com.assignment.spotabee.type11";
    public static final int EXAMPLE_NOTIFICATION_ID = 1;
    private Long downloadId;

    private Context context;


    public FragmentOpenDownloadNotification() {
        // Required empty public constructor
    }


    public static FragmentOpenDownloadNotification newInstance(String param1, String param2) {
        FragmentOpenDownloadNotification fragment = new FragmentOpenDownloadNotification();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.downloadId = getArguments().getLong("download_id");
            makeNotification();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_fragment_open_download_notification, container, false);
        this.context = getActivity().getApplicationContext();
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createAnExampleNotificationChannel(NotificationManager notificationManager) {

        if (notificationManager.getNotificationChannel(EXAMPLE_CHANNEL_ID) == null) {
            CharSequence name = "Spot A Bee";
            String description = "Open your guide";

            NotificationChannel channel = new NotificationChannel(EXAMPLE_CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);

            notificationManager.createNotificationChannel(channel);
        }
    }


    private Notification createExampleNotification() {


        Intent appIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent openAppIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(downloadId != null){
            Intent openDownloads = openDownloadedAttachment(getActivity(), downloadId);
            PendingIntent openDownloadsIntent = PendingIntent.getActivity(context, 0, openDownloads, PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), EXAMPLE_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_bell)
                    .setContentTitle("Spot A Bee")
                    .setContentText("Open your guide")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT) //Set priority for backwards compatibility

                    .addAction(0, "Open your guide (action)", openAppIntent) //Sets no icon in this example
                    .setContentIntent(openAppIntent)
                    .setAutoCancel(true);


            return mBuilder.build();
        } else {
            return null;
        }
    }


    public void makeNotification() {
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nManager == null)
            Toast.makeText(getContext(), "Unable to post notification", Toast.LENGTH_SHORT).show();
        else {

            //TO-DO(6) Solution
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                createAnExampleNotificationChannel(nManager);

            //TO-DO(8) Solution
            Notification exampleNotification = createExampleNotification();

            //TO-DO(10) Solution
            nManager.notify(EXAMPLE_NOTIFICATION_ID, exampleNotification);
        }

    }


    private Intent openDownloadedAttachment(final Context context, final long downloadId) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String downloadLocalUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            String downloadMimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            if ((downloadStatus == DownloadManager.STATUS_SUCCESSFUL)) {
                return getIntentToOpenFile(context, Uri.parse(downloadLocalUri), downloadMimeType);
            }
        }
        cursor.close();
        return null;
    }

    private Intent getIntentToOpenFile(final Context context, Uri attachmentUri, final String attachmentMimeType) {
        if (attachmentUri != null) {
            // Get Content Uri.
            if (ContentResolver.SCHEME_FILE.equals(attachmentUri.getScheme())) {
                // FileUri - Convert it to contentUri.
                File file = new File(attachmentUri.getPath());
                attachmentUri = FileProvider.getUriForFile(getActivity(), "com.freshdesk.helpdesk.provider", file);
                ;
            }

            Intent openAttachmentIntent = new Intent(Intent.ACTION_VIEW);
            openAttachmentIntent.setData(attachmentUri);
//            openAttachmentIntent.setDataAndType(attachmentUri, attachmentMimeType);
            openAttachmentIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            return openAttachmentIntent;
        }
        return null;
    }

}
