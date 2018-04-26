package com.assignment.spotabee;

/**
 * Created by Lauren on 4/25/2018.
 *
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Screen event", Toast.LENGTH_SHORT).show();
    }
}

