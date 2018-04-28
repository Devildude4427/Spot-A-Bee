package com.assignment.spotabee;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.assignment.spotabee.fragments.PaymentInfo;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import static com.assignment.spotabee.Config.Config.PAYPAL_REQUEST_CODE;

import org.json.JSONException;

public class Permissions extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS = 0;
    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final int PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS = 2;
    public static final int IMAGE_CAPTURE = 3;
    public static final int ACCESS_IMAGE_GALLERY = 4;
    public static final int PERMISSION_REQUEST_CAMERA = 5;
    public static final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 6;
    public static final int CHOOSE_ACCOUNT = 99;
    private  static AccountManager accountManager;
    private static final String TAG = "Permissions Debug";

    /**
     * Checks each permission if it is given, and, if not, requests them.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkIfPermissionsGiven(Context context) {

        MainActivity mainActivity = new MainActivity();
        AppCompatActivity activity = mainActivity.getActivity();
        accountManager = mainActivity.getAccountManager();

        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
                    Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                requestLocationAccountPermission(activity);
                Log.v(TAG, "Requesting account and location Permissions");
            } else if(ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission(context);
                Log.v(TAG, "Only requesting location Permission");
            } else if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.GET_ACCOUNTS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestAccountPermission(context);
                Log.v(TAG, "Only requesting account Permission"); }
            else if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED){
                requestCameraPermission(context);
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
//    protected void onActivityResult(final int requestCode, final int resultCode,
//                                    final Intent data){
//        try {
//            if (requestCode == CHOOSE_ACCOUNT && resultCode == RESULT_OK) {
//                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
//                Log.v(TAG, accountName);
//            } else if (requestCode == CHOOSE_ACCOUNT) {
//                Log.v(TAG, "There was an error in the account picker");
//            } else if (requestCode == PAYPAL_REQUEST_CODE){
//                payPalResult(requestCode, resultCode, data);
//            } else{
//                Log.v(TAG, "Nothing exists to handle that request code" + requestCode);
//            }
//        } catch (Exception e) {
//            Log.v(TAG, "Exception " + e);
//        }
//    }

    /**
     * Requests permissions to use device location and access accounts.
     */
    private void requestLocationAccountPermission(AppCompatActivity activity) {
        // Permission has not been granted and must be requested.
        // Request the permission. The result will be received in onRequestPermissionResult().
        Log.v(TAG, " " + activity);
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.GET_ACCOUNTS},
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS);
    }

    /**
     * Requests permission to use device location.
     */
    private static void requestLocationPermission(Context context) {
        // Permission has not been granted and must be requested.
        // Request the permission. The result will be received in onRequestPermissionResult().
        ActivityCompat.requestPermissions((Activity)context,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
    }

    /**
     * Requests permission to use accounts.
     */
    private static void requestAccountPermission(Context context) {
        // Permission has not been granted and must be requested.
        // Request the permission. The result will be received in onRequestPermissionResult().
        ActivityCompat.requestPermissions((Activity)context,
                new String[]{Manifest.permission.GET_ACCOUNTS},
                PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS);
    }

    /**
     * Requests permission to use device camera.
     */
    private static void requestCameraPermission(Context context) {
        // Permission has not been granted and must be requested.
        // Request the permission. The result will be received in onRequestPermissionResult().
        ActivityCompat.requestPermissions((Activity)context,
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
        Log.v(TAG, "UMMM.. hello?");
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission to both granted");

                    try {
                        Intent intent = accountManager.newChooseAccountIntent(null, null, new String[]{"com.google"}, null, null, null, null);
                        startActivityForResult(intent, CHOOSE_ACCOUNT);

//                        checkIfPermissionsGiven(this, );

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
//                    checkIfPermissionsGiven(this);
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
//                        checkIfPermissionsGiven(this);
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
//                    checkIfPermissionsGiven(this);
                }
            }
            default: {
                Log.v(TAG, "Unknown permission request");
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void payPalResult(final int requestCode, final int resultCode,
                             final Intent data){
        Log.d(TAG, "We are in payPalResult");
        String amount = "test amount";
        if (resultCode == RESULT_OK) {
            PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirmation != null) {
                try {
                    String paymentDetails = confirmation.toJSONObject().toString(7);
                    PaymentInfo paymentInfo = new PaymentInfo();
                    Bundle args = new Bundle();
                    args.putString("amount", "$100");
                    args.putString("paymentInfo", paymentDetails);
                    paymentInfo.setArguments(args);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, paymentInfo).commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();

        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();

    }
}
