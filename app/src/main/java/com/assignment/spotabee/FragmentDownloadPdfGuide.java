package com.assignment.spotabee;

import android.app.DownloadManager;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import static android.content.Context.DOWNLOAD_SERVICE;


//
public class FragmentDownloadPdfGuide extends Fragment implements View.OnClickListener{
    private DownloadManager downloadManager;
    private ImageView downloadPdf;
    private long downloadReference;
    private Context appContext;
    private Receiver downloadReceiver;
    private View rootView;
    private Uri uri;
    private View v;
    public static final String TAG = "Download PDF Debug";
    public FragmentDownloadPdfGuide() {
        // Required empty public constructor
    }


    public static FragmentDownloadPdfGuide newInstance(String param1, String param2) {
        FragmentDownloadPdfGuide fragment = new FragmentDownloadPdfGuide();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fragment_download_pdf_guide, container, false);

        downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);

        downloadPdf = rootView.findViewById(R.id.download_pdf_bee);
        downloadPdf.setOnClickListener(this);
        appContext = getActivity().getApplicationContext();

        downloadReceiver = new Receiver();

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getActivity().registerReceiver(downloadReceiver, filter);

        return rootView;
    }

    private long downloadData (Uri uri) {
        this.uri = uri;

        downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setTitle("Help the Bees");
        request.setDescription("Guide on helping to preserve the bee population");

        request.setDestinationInExternalFilesDir(getActivity(),
                Environment.DIRECTORY_DOWNLOADS,"Bee_Guide.pdf");

        downloadReference = downloadManager.enqueue(request);
        check_download_Status(downloadReference);

        return downloadReference;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.download_pdf_bee:
                Uri uriForPdf = Uri.parse("https://friendsoftheearth.uk/sites/default/files/downloads/bees_booklet.pdf");
                downloadData(uriForPdf);
                break;
        }
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

            Toast toast = Toast.makeText(getActivity(),
                    "PDF download:" + "\n" + statusText + reasonText,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        }
    }
}
