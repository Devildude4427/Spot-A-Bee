package com.assignment.spotabee.customutils;
/**
 * Made by: C1769948
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Reference: https://stackoverflow.com/questions/15866035/android-show-a-message-if-no-internet-connection-and-continue-to-check?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
 *
 * Not modified from above source, copied and pasted.
 */

public class NetworkConnection {


    private static final String TAG = NetworkConnection.class.getSimpleName();
    private Context context;

    public NetworkConnection(Context context){
        this.context = context;
    }


    public boolean internetIsAvailable()
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
            Log.d(TAG,"no internet connection");
            return false;
        }
        else
        {
            if(info.isConnected())
            {
                Log.d(TAG," internet connection available...");
                return true;
            }
            else
            {
                Log.d(TAG," internet connection");
                return true;
            }

        }
    }
}
