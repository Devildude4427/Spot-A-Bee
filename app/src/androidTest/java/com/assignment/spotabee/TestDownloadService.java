package com.assignment.spotabee;


import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;


import com.assignment.spotabee.services.DownloadService;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.TimeoutException;

import static junit.framework.Assert.assertEquals;

import static org.mockito.Mockito.mock;

public class TestDownloadService {

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Mock
    MainActivity mainActivity;
    private DownloadService service2;
    private DownloadService mService;

    @Test
    public void testWithBoundService() throws TimeoutException {
        try {
            mainActivity = mock(MainActivity.class);
            // Create the service Intent.
            Intent serviceIntent =
                    new Intent(InstrumentationRegistry.getTargetContext(),
                            DownloadService.class);

            // Data can be passed to the service via the Intent.
            serviceIntent.putExtra("download_id", 1);
            mServiceRule.startService(serviceIntent);

            // Bind the service and grab a reference to the binder.
            IBinder binder = mServiceRule.bindService(serviceIntent);

            // Get the reference to the service, or you can call
            // public methods on the binder directly.
            service2 =
                ((DownloadService.LocalBinder) binder).getService();

            // Verify that the service is working correctly.
            Thread.sleep(15000);

            assertEquals(service2.isDownloadSuccessful(1, mainActivity), true);
        } catch (Exception e){

        }

    }

}
