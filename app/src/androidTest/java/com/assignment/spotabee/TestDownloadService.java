package com.assignment.spotabee;


import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.webkit.WebView;


import com.assignment.spotabee.fragments.FragmentDownloadPdfGuide;
import com.assignment.spotabee.services.DownloadService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.uiautomator.Until.findObject;
import static junit.framework.Assert.assertEquals;

import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.mock;

public class TestDownloadService {

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private final String guideName = "Bee_Guide.pdf";
    private String downloadsDirectoryPath;
    @Mock
    MainActivity mainActivity;
    private DownloadService mService;


    @Before
    public void init(){
        // Get path for Downloads directory
        downloadsDirectoryPath = mActivityTestRule
                .getActivity()
                .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath();

        // Get any existing pdf guides and delete them so that the download
        // will take place
        deleteAllExistingFilesByName(downloadsDirectoryPath, guideName);
    }

    @Test
    public void testSuccessfulDownloadViaService() throws TimeoutException {
        try {

            // Get the reference to the service to call methods on it directly
            mService = mockServiceLaunch();

            // Allow time  for the download to complete
            Thread.sleep(15000);

            // Verify that the download was successful
            assertEquals(true, mService.isDownloadSuccessful(1, mainActivity));
        } catch (Exception e){

        }

    }


    // !!!!! This test will pass as long as it is run on it's own !!!!!
    @Test
    public void testPdfNotDownloadedMoreThanOnce() throws TimeoutException {
        try {
            mActivityTestRule.launchActivity(new Intent());

            String filePath = mActivityTestRule
                    .getActivity()
                    .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath();

            // Get the reference to the service to call methods on it directly
            mService = mockServiceLaunch();

            // Allow time  for the download to complete
            Thread.sleep(15000);

            mockServiceLaunch();

            Thread.sleep(15000);

            // Verify that only one PDF guide exists, which will have downloaded
            // from the previous test
            assertEquals(1, getAllFilesByName(new File(filePath), guideName).size());
        } catch (Exception e){

        }
    }

    // Start the DownloadService and return a
    public DownloadService mockServiceLaunch(){
        try {
            mainActivity = mock(MainActivity.class);
            // Create a download service Intent.
            Intent serviceIntent =
                    new Intent(InstrumentationRegistry.getTargetContext(),
                            DownloadService.class);

            // Pass the download ID
            serviceIntent.putExtra("download_id", 1);
            mServiceRule.startService(serviceIntent);

            // Bind the service and grab a reference to the binder.
            IBinder binder = mServiceRule.bindService(serviceIntent);

            // Get the reference to the service to call methods on it directly
            mService =
                    ((DownloadService.LocalBinder) binder).getService();

            return mService;
        } catch (Exception e){
            return null;
        }

    }

    /**
     * Access to the internet must be manually disabled on a device before this test is  run
     */
    @Test
    public void testCaseNoInternet(){
        mockServiceLaunch();
        // Assert that an explanatory toast message is displayed to a user
        // Will also indicate if the service will have stopped itself since
        // stopSelf() is called immediately after Toast is displayed
        TestHelpers.isToastMessageDisplayed(R.string.internet_unavailable);
    }

    // Reference: https://stackoverflow.com/questions/9530921/list-all-the-files-from-all-the-folder-in-a-single-list?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    private List<File> getAllFilesByName(File parentDir, String fileName) {
        List<File> inFiles = new ArrayList<>();
        Queue<File> files = new LinkedList<>();
        files.addAll(Arrays.asList(parentDir.listFiles()));
        while (!files.isEmpty()) {
            File file = files.remove();
            if (file.isDirectory()) {
                files.addAll(Arrays.asList(file.listFiles()));
            } else if (file.getName().equals(fileName)) {
                inFiles.add(file);
            }
        }
        return inFiles;
    }

    private void deleteAllExistingFilesByName(String parentDir, String fileName){
        List<File> allGuides = getAllFilesByName(new File(parentDir), fileName);
        for(File file : allGuides){
            file.delete();
        }

    }

}
