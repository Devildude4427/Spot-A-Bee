package com.assignment.spotabee;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static android.support.v4.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;

public class Permissions extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS = 0;
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS = 2;
    public static final int PERMISSION_REQUEST_ACCESS_IMAGE_CAPTURE = 3;
    public static final int PERMISSION_REQUEST_ACCESS_IMAGE_GALLERY = 4;
    private static final int PERMISSION_REQUEST_CAMERA = 5;
    private static final int CHOOSE_ACCOUNT = 99;
    private  static AccountManager accountManager;
    private static final String TAG = "Permissions Debug";

    /**
     * Checks each permission if it is given, and, if not, requests them.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkIfPermissionsGiven(Context context) {

        Log.v(TAG, "Got here");
        MainActivity mainActivity = new MainActivity();
        accountManager = mainActivity.getAccountManager();
        Log.v(TAG, "Account Manager started");

        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                requestLocationAccountPermission();
                Log.v(TAG, "Requesting account and location Permissions");
            } else if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
                Log.v(TAG, "Only requesting location Permission");
            } else if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.GET_ACCOUNTS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestAccountPermission();
                Log.v(TAG, "Only requesting account Permission"); }
            else if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED){
                requestCameraPermission();
                Log.v(TAG, "Requesting camera Permission");}
            else {
                Log.v(TAG, "No permissions requested");
            }
        } catch (Exception e) {
            Log.v(TAG, "Exception " + e);
        }




    }

    /**
     * Creates gets details and confirms operation from account picker
     *
     * @param requestCode An integer that relates to the permission. So
     *                    location might be 1, account access 2, and so on.
     * @param resultCode If the result succeeded or failed
     * @param data The intent that is being requested
     */
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data){
        try {
            if (requestCode == CHOOSE_ACCOUNT && resultCode == RESULT_OK) {
                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                Log.v(TAG, accountName);
            } else if (requestCode == CHOOSE_ACCOUNT) {
                Log.v(TAG, "There was an error in the account picker");
            } else {
                Log.v(TAG, "Nothing exists to handle that request code" + requestCode);
            }
        } catch (Exception e) {
            Log.v(TAG, "Exception " + e);
        }
    }

    /**
     * Requests permissions to use device location and access accounts.
     */
    private void requestLocationAccountPermission() {
        // Permission has not been granted and must be requested.
        // Request the permission. The result will be received in onRequestPermissionResult().
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.GET_ACCOUNTS},
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS);
    }

    /**
     * Requests permission to use device location.
     */
    private void requestLocationPermission() {
        // Permission has not been granted and must be requested.
        // Request the permission. The result will be received in onRequestPermissionResult().
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
    }

    /**
     * Requests permission to use accounts.
     */
    private void requestAccountPermission() {
        // Permission has not been granted and must be requested.
        // Request the permission. The result will be received in onRequestPermissionResult().
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.GET_ACCOUNTS},
                PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS);
    }

    /**
     * Requests permission to use device camera.
     */
    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        // Request the permission. The result will be received in onRequestPermissionResult().
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CAMERA);
    }


    /**
     * Checks the results of the permission requests.
     *
     * @param requestCode An integer that relates to the permission. So
     *                    location might be 1, account access 2, and so on.
     * @param permissions The permission being requested.
     * @param grantResults What the result of result of the request is. The result
     *                     of whether or not the user allowed this permission.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission to both granted");

                    try {
                        Intent intent = accountManager.newChooseAccountIntent(null, null, new String[]{"com.google"}, null, null, null, null);
                        startActivityForResult(intent, CHOOSE_ACCOUNT);
                        checkIfPermissionsGiven(getApplicationContext());

                    } catch (Exception e) {
                        Log.v(TAG, "Exception " + e);
                    }

                } else {
                    Log.v(TAG, "User needs to make an account");
                }
            }
            return;

            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission to only location granted");
                    checkIfPermissionsGiven(getApplicationContext());
                }
            }
            return;

            case PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission to only account granted");
                    try {
                        Intent intent = accountManager.newChooseAccountIntent(null, null, new String[]{"com.google"}, null, null, null, null);
                        startActivityForResult(intent, CHOOSE_ACCOUNT);
                        Log.v(TAG, "Intent to choose just account a go");
                    } catch (Exception e) {
                        Log.v(TAG, "Exception " + e);
                    }

                } else {
                    //Redirect to force user to create an account
                    Log.v(TAG, "User needs to make an account");
                }
            }
            return;

            case PERMISSION_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission to use camera granted");
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
