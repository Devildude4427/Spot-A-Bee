package com.assignment.spotabee.fragments;
/**
 * Made by: C1769948
 */
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.assignment.spotabee.R;
import com.assignment.spotabee.receivers.DownloadReceiver;
import com.assignment.spotabee.services.DownloadService;

import static android.content.Context.DOWNLOAD_SERVICE;


// (Download Manager tutorial) Reference: https://www.codeproject.com/Articles/1112730/Android-Download-Manager-Tutorial-How-to-Download
public class FragmentDownloadPdfGuide extends Fragment implements View.OnClickListener{
    private DownloadManager downloadManager;
    private ImageView downloadPdf;
    private AppCompatButton downloadBtn;
    private long downloadReference;
    private Context appContext;
    private DownloadReceiver downloadReceiver;
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

        appContext = getActivity().getApplicationContext();

//        downloadReceiver = new DownloadReceiver();

        downloadBtn = rootView.findViewById(R.id.downloadGuideBtn);
        downloadBtn.setOnClickListener(this);

//        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//        getActivity().registerReceiver(downloadReceiver, filter);

        getActivity().setTitle(getString(R.string.download_pdf_fragment_title));

        return rootView;
    }



    private long downloadData (Uri uri) {
        this.uri = uri;

        downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setTitle(getString(R.string.download_pdf_title));
        request.setDescription(getString(R.string.download_pdf_description));

        request.setDestinationInExternalFilesDir(getActivity(),
                Environment.DIRECTORY_DOWNLOADS,getString(R.string.bee_pdf_guide));

        downloadReference = downloadManager.enqueue(request);
        check_download_Status(downloadReference);

        return downloadReference;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.downloadGuideBtn:
//                Uri uriForPdf = Uri.parse("https://friendsoftheearth.uk/sites/default/files/downloads/bees_booklet.pdf");
//                downloadData(uriForPdf);
                Intent mService = new Intent(getContext(), DownloadService.class);
                mService.putExtra("download_id", 1);
                getContext().startService(mService);

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

            Toast toast = Toast.makeText(getActivity(),
                    getString(R.string.pdf_download) + "\n" + statusText + reasonText,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        }
    }
}
