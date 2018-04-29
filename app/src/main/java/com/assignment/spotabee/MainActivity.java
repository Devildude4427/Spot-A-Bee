package com.assignment.spotabee;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.assignment.spotabee.customutils.CheckNetworkConnection;
import com.assignment.spotabee.customutils.FileOp;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.database.DatabaseInitializer;
import com.assignment.spotabee.database.Description;
import com.assignment.spotabee.fragments.DonationLogin;
import com.assignment.spotabee.fragments.FragmentAboutUs;
import com.assignment.spotabee.fragments.FragmentDescriptionForm;
import com.assignment.spotabee.fragments.FragmentHome;
import com.assignment.spotabee.fragments.FragmentHowTo;
import com.assignment.spotabee.fragments.FragmentLeaderboard;
import com.assignment.spotabee.fragments.FragmentMap;
import com.assignment.spotabee.fragments.PaymentInfo;
import com.assignment.spotabee.imagerecognition.ClarifaiClientGenerator;
import com.assignment.spotabee.imagerecognition.ClarifaiRequest;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.io.InputStream;

import clarifai2.api.ClarifaiClient;

import static com.assignment.spotabee.Config.Config.PAYPAL_REQUEST_CODE;
import static com.assignment.spotabee.Permissions.ACCESS_IMAGE_GALLERY;


import clarifai2.api.ClarifaiClient;

import static com.assignment.spotabee.Permissions.CHOOSE_ACCOUNT;
import static com.assignment.spotabee.Permissions.PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS;
import static com.assignment.spotabee.Permissions.PERMISSION_REQUEST_ACCESS_FINE_LOCATION;
import static com.assignment.spotabee.Permissions.PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS;
import static com.assignment.spotabee.Permissions.PERMISSION_REQUEST_CAMERA;
import static com.assignment.spotabee.Permissions.PERMISSION_REQUEST_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS = 0;
//    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1;
//    private static final int PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS = 2;
//    public static final int PERMISSION_REQUEST_ACCESS_IMAGE_CAPTURE = 3;
//    public static final int PERMISSION_REQUEST_ACCESS_IMAGE_GALLERY = 4;
//    private static final int PERMISSION_REQUEST_CAMERA = 5;
//    private static final int CHOOSE_ACCOUNT = 99;
    private AccountManager accountManager;
    public static Context contextOfApplication;
    private AppDatabase db;

    public static final int PICK_IMAGE = 100;
    private static final String API_KEY = "d984d2d494394104bb4bee0b8149523d";
    private static ClarifaiClient client;
    private ImageView imgGallery;

    private DrawerLayout mDrawerLayout;
    private static final String TAG = "Main Activity Debug";

    private int payPalResultCode;
    private Intent payPalData;

    private boolean mReturningWithResult = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accountManager = (AccountManager)
                getSystemService(Context.ACCOUNT_SERVICE);

        contextOfApplication = this;

        checkIfPermissionsGiven();

        db = AppDatabase.getAppDatabase(getApplicationContext());

        //This clears out the database, and is called every time! Remove if you need persistence!
        db.descriptionDao().nukeTable();
        //This clears out the database, and is called every time! Remove if you need persistence!

        DatabaseInitializer.populateAsync(db);

        Description description = new Description(
                51.4816,-3.1791,
                "Cardiff", "Rose", "None",
                1, "17-05-2018", "15:39");

        db.descriptionDao().insertDescriptions(description);



        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_home);

//        Intent intent = new Intent(this, ScreenService.class);
//        startService(intent);
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new FragmentHome();
                break;
            case R.id.nav_howto:
                fragment = new FragmentHowTo();
                break;
            case R.id.nav_results:
                fragment = new FragmentMap();
                break;
            case R.id.nav_aboutus:
                fragment = new FragmentAboutUs();
                break;
            case R.id.nav_leaderboard:
                fragment = new FragmentLeaderboard();
                break;

            case R.id.nav_donate:
                fragment = new DonationLogin();
                break;

            //Adding btnHome from the DonationInfo fragment so that
            //The user can easily go back home after making a donation
            case R.id.btnHome:
                fragment = new FragmentHome();
                break;
//            case R.id.nav_identify_image:
//                startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), PICK_IMAGE);
//                break;

            case R.id.nav_resources:
                fragment = new FragmentDownloadPdfGuide();
                break;


        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    /**
     * A static method that retrieves Activity context
     *
     * @return The context for the current activity
     */
    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    /**
     * Checks each permission if it is given, and, if not, requests them.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkIfPermissionsGiven() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            requestLocationAccountPermission();
            Log.v(TAG, "Requesting account and location Permissions");
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            Log.v(TAG, "Only requesting location Permission");
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestAccountPermission();
            Log.v(TAG, "Only requesting account Permission");
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
            Log.v(TAG, "Requesting camera Permission");
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestExternalStoragePermission();
            Log.v(TAG, "Requesting camera Permission");
        } else {
            Log.v(TAG, "No permissions requested");
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
//    @Override
//    public void onActivityResult(final int requestCode, final int resultCode,
//                                    final Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//
//        try {
//            if(data == null && requestCode == PICK_IMAGE){
//                return;
//            } else if (requestCode == CHOOSE_ACCOUNT && resultCode == RESULT_OK) {
//                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
//                Log.v(TAG, accountName);
//            } else if (requestCode == CHOOSE_ACCOUNT) {
//                Log.v(TAG, "There was an error in the account picker");
//            } else if (requestCode == 3) {
//                Log.v(TAG, "Request for camera");
//            } else if (requestCode == PICK_IMAGE) {
//
//                final ProgressDialog progress = new ProgressDialog(this);
//                progress.setTitle("Loading");
//                progress.setMessage("Identify your flower..");
//                progress.setCancelable(false);
//                progress.show();
//
//                if (!CheckNetworkConnection.isInternetAvailable(this)) {
//                    progress.dismiss();
//                    Toast.makeText(this,
//                            "Internet connection unavailable.",
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                client = ClarifaiClientGenerator.generate(API_KEY);
//                final byte[] imageBytes = FileOp.getByteArrayFromIntentData(this, data);
//                if (imageBytes != null) {
//
//                    AsyncTask.execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.d(TAG, "We have started run thread");
//                            ClarifaiRequest clarifaiRequest = new ClarifaiRequest(client, "flower_species", imageBytes);
//                            String flowerType = clarifaiRequest.executRequest();
//                            Log.d(TAG, "Flower Type: " + flowerType);
//
//                            Bundle descriptionFormBundle = new Bundle();
//                            descriptionFormBundle.putString("flowerName", flowerType);
//
//                            FragmentDescriptionForm fragmentDescriptionForm = new FragmentDescriptionForm();
//                            fragmentDescriptionForm.setArguments(descriptionFormBundle);
//
//                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                            fragmentTransaction.replace(R.id.content_frame, fragmentDescriptionForm);
//                            fragmentTransaction.commit();
//                            progress.dismiss();
//                        }
//                    });
//                }
//            } else if (requestCode == Activity.RESULT_CANCELED){
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.content_frame, new FragmentHome());
//                fragmentTransaction.commit();
//            } else if (requestCode == PAYPAL_REQUEST_CODE){
//                payPalResult(requestCode, resultCode, data);
//
//            } else {
//                Log.v(TAG, "Nothing exists to handle that request code" + requestCode);
//            }
//        } catch (Exception e) {
//            Log.v(TAG, "Exception " + e);
//        }
//    }

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
     * Requests permission to use device camera.
     */
    private void requestExternalStoragePermission() {
        // Permission has not been granted and must be requested.
        // Request the permission. The result will be received in onRequestPermissionResult().
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_EXTERNAL_STORAGE);
    }


    /**
     * Checks the results of the permission requests.
     *
     * @param requestCode  An integer that relates to the permission. So
     *                     location might be 1, account access 2, and so on.
     * @param permissions  The permission being requested.
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
                        checkIfPermissionsGiven();

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
                    checkIfPermissionsGiven();
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
                    checkIfPermissionsGiven();
                }
            }
            return;

            case PERMISSION_REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission to use external storage granted");
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }


    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data) {


        try {
            if (data != null && requestCode == PICK_IMAGE) {
                imageRecognitionResult(resultCode, data);
            }

            if (requestCode == PAYPAL_REQUEST_CODE) {
                mReturningWithResult = true;
                payPalData = data;
                payPalResultCode = resultCode;
            }

            else {
                Log.v(TAG, "Nothing exists to handle that request code" + requestCode);
            }
        } catch (Exception e) {
            Log.v(TAG, "Exception " + e);
        }
    }

    public void payPalResult(final int resultCode,
                             final Intent data) {
        Log.d(TAG, "We are in payPalResult");
//        String paymentDetails = "test paymentDetails";
        if (resultCode == RESULT_OK) {
            PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirmation != null) {
                try {
                    String paymentDetails = confirmation.toJSONObject().toString(7);
                    PaymentInfo paymentInfo = new PaymentInfo();
                    Bundle args = new Bundle();
                    args.putString("paymentInfo", paymentDetails);
                    paymentInfo.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, paymentInfo).commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();

        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(getActivity(), "Invalid", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mReturningWithResult) {
            payPalResult(payPalResultCode, payPalData);
        }
        // Reset the boolean flag back to false for next time.
        mReturningWithResult = false;
    }

    private void imageRecognitionResult(int resultCode, Intent data) {

            if (data == null) {
                return;
            } else {
                Log.d(TAG, "WE ARE IN IMAGE RECOGNITION");
                final ProgressDialog progress = new ProgressDialog(getContextOfApplication());
                progress.setTitle("Loading");
                progress.setMessage("Identify your flower..");
                progress.setCancelable(false);
                progress.show();

                if (!CheckNetworkConnection.isInternetAvailable(getContextOfApplication())) {
                    progress.dismiss();
                    Toast.makeText(getContextOfApplication(),
                            "Internet connection unavailable.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                client = ClarifaiClientGenerator.generate(API_KEY);
                final byte[] imageBytes = FileOp.getByteArrayFromIntentData(getContextOfApplication(), data);
                if (imageBytes != null) {

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "We have started run thread");
                            ClarifaiRequest clarifaiRequest = new ClarifaiRequest(client, "flower_species", imageBytes);
                            String flowerType = clarifaiRequest.executRequest();
                            Log.d(TAG, "Flower Type: " + flowerType);

                            Bundle descriptionFormBundle = new Bundle();
                            descriptionFormBundle.putString("flowerName", flowerType);

                            FragmentDescriptionForm fragmentDescriptionForm = new FragmentDescriptionForm();
                            fragmentDescriptionForm.setArguments(descriptionFormBundle);

                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragmentDescriptionForm);
                            fragmentTransaction.commit();
                            progress.dismiss();
                        }
                    });
                }
            }
        }


}

