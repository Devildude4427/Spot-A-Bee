package com.assignment.spotabee;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.Configurator;
import android.support.v4.content.LocalBroadcastManager;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.assignment.spotabee.receivers.DownloadReceiver;
import com.assignment.spotabee.services.DownloadService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.v4.content.LocalBroadcastManager.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class TestDownloadReceiver {
    private DownloadReceiver mReceiver;
    private Context mContext;
    private static final String TAG = "Test Receiver";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {

        mReceiver = new DownloadReceiver();
        mContext = mActivityTestRule.getActivity();
    }
//
//    @Test
//    public void testStartActivity() {
//        try {
//            Intent mService = new Intent(mContext, DownloadService.class);
//            mService.putExtra("download_id", 1);
//            mContext.startService(mService);
//
//            Thread.sleep(15000);
//
////        mReceiver.onReceive(mContext, mService);
//            mReceiver.getResultExtras(false).get
//            assertEquals(true, mReceiver.getHasReceived());
//        } catch (Exception e){
//            Log.e(TAG, e.getMessage());
//        }
//
//
//
//    }

    @Before
    public void init() {
        mContext = new RenamingDelegatingContext(InstrumentationRegistry.getInstrumentation().getTargetContext(), "test_");
    }

    @Test
    public void myReceiverTest() throws InterruptedException {

        DownloadReceiver receiver = new DownloadReceiver();

        //remeber to change Intent.ACTION_PACKAGE_REPLACED
//with your action name from your mainfest file
        getInstance(mContext).registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        String pn = mContext.getPackageName();

        Intent i = new Intent(Intent.ACTION_SCREEN_ON);
        i.setData(Uri.parse("package:" + mContext.getPackageName()));
        getInstance(mContext).sendBroadcast(i);
        Thread.sleep(2000);
        assertTrue(receiver.getHasReceived());

        getInstance(mContext).unregisterReceiver(receiver);
        assertTrue(true);


    }
}
