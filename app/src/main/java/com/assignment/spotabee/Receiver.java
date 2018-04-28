package com.assignment.spotabee;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast downloadSuccessful = Toast.makeText(context, "Download Complete", Toast.LENGTH_SHORT);
        downloadSuccessful.setGravity(Gravity.TOP, 25, 400);
        downloadSuccessful.show();
    }
}
