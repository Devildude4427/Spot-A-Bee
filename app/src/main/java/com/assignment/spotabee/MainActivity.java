package com.assignment.spotabee;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS = 0;
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS = 2;
    static final int PERMISSION_REQUEST_ACCESS_IMAGE_CAPTURE = 3;
    public static final int PERMISSION_REQUEST_ACCESS_IMAGE_GALLERY = 4;
    private static final int PERMISSION_REQUEST_CAMERA = 5;
    private static final int CHOOSE_ACCOUNT = 99;
    private AccountManager accountManager;
    private ImageView imgGallery;
    private DrawerLayout mDrawerLayout;
    private static final String TAG = "Debug";

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        displaySelectedScreen(R.id.nav_aboutus);

        checkIfPermissionsGiven();

        accountManager = (AccountManager)
                getSystemService(Context.ACCOUNT_SERVICE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        displaySelectedScreen(menuItem.getItemId());


                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new Home();
                break;
            case R.id.nav_howto:
                fragment = new HowTo();
                break;
            case R.id.nav_aboutus:
                fragment = new AboutUs();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Checks each permission if it is given, and, if not, requests them.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkIfPermissionsGiven() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
//                        Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
//            requestLocationAccountPermission();
//            Log.v(TAG, "Requesting account and location Permissions");}
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            Log.v(TAG, "Only requesting location Permission");}
//        else if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.GET_ACCOUNTS)
//                != PackageManager.PERMISSION_GRANTED) {
//            requestAccountPermission();
//            Log.v(TAG, "Only requesting account Permission"); }
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            requestCameraPermission();
            Log.v(TAG, "Requesting camera Permission");}
        else {
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
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data){
        try {
            if (requestCode == CHOOSE_ACCOUNT && resultCode == RESULT_OK) {
                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                Log.v(TAG, accountName);
            } else if (requestCode == PERMISSION_REQUEST_ACCESS_IMAGE_GALLERY && resultCode == RESULT_OK) {
                //What will happen if yes??
                Uri galleryUri = data.getData();

                //Create a Stream to read the image data for the memory
                //If we are unable to catch information from the data for any reasy, try/catch it
                //re edit the exception or put it in the catch block
                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(galleryUri);

                    // Get Bitmap, get an instance of the image view. Catch info, Tell the users that the image was unable to find.
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    //Show the gallery or image to user:
                    imgGallery.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unnabble to find the image or it is unavailable", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == CHOOSE_ACCOUNT) {
                Log.v(TAG, "There was an error in the account picker");
            } else if (requestCode == PERMISSION_REQUEST_ACCESS_IMAGE_GALLERY) {
                Log.v(TAG, "There was an error in the image gallery" + resultCode);
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
//    private void requestLocationAccountPermission() {
//        // Permission has not been granted and must be requested.
//        // Request the permission. The result will be received in onRequestPermissionResult().
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.GET_ACCOUNTS},
//                PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS);
//    }

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
//    private void requestAccountPermission() {
//        // Permission has not been granted and must be requested.
//        // Request the permission. The result will be received in onRequestPermissionResult().
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.GET_ACCOUNTS},
//                PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS);
//    }

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
//            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.v(TAG, "Permission to both granted");
//
//                    try {
//                        Intent intent = accountManager.newChooseAccountIntent(null, null, new String[]{"com.google"}, null, null, null, null);
//                        startActivityForResult(intent, CHOOSE_ACCOUNT);
//                        Log.v(TAG, "Intent to Choose Account go");
//                        checkifPermissionsGiven();
//
//                    } catch (Exception e) {
//                        Log.v(TAG, "Exception " + e);
//                    }
//
//                } else {
//                    Log.v(TAG, "User needs to make an account");
//                }
//            }
//            return;

            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission to only location granted");
                    checkIfPermissionsGiven();
                }
            }
            return;

//            case PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.v(TAG, "Permission to only account granted");
//                    try {
//                        Intent intent = accountManager.newChooseAccountIntent(null, null, new String[]{"com.google"}, null, null, null, null);
//                        startActivityForResult(intent, CHOOSE_ACCOUNT);
//                        Log.v(TAG, "Intent to choose just account a go");
//                    } catch (Exception e) {
//                        Log.v(TAG, "Exception " + e);
//                    }
//
//                } else {
//                    //Redirect to force user to create an account
//                    Log.v(TAG, "User needs to make an account");
//                }
//            }
//            return;

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
