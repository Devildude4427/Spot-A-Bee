package com.assignment.spotabee;

import android.Manifest;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.assignment.spotabee.Map.setUpMap;

//import com.assignment.spotabee.Home.R;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS = 0;
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS = 2;
    static final int PERMISSION_REQUEST_ACCESS_IMAGE_CAPTURE = 3;
    public static final int PERMISSION_REQUEST_ACCESS_IMAGE_GALLERY = 4;
    private static final int CHOOSE_ACCOUNT = 99;
    private AccountManager accountManager;
    private static final String TAG = "Debug";

    AppCompatButton button;
    AppCompatButton button2;
    AppCompatButton button3;
    private ImageView imgGallery;
    //declare the intent so that you can use it later as a global object
    Intent intent;


    @SuppressLint("WrongViewCast")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        //Set the button to a listener
        button = (AppCompatButton) findViewById(R.id.button2);
        button.setOnClickListener(this);

        //Create a view/reference for the Gallery
        imgGallery = (ImageView) findViewById(R.id.imgGallery);

        checkIfPermissionsGiven();

        accountManager = (AccountManager)
                getSystemService(Context.ACCOUNT_SERVICE);
    }

    //Adding the menu bar will allow the user to navigate easily througout the app.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        return true;
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
//    protected void onActivityResult(final int requestCode, final int resultCode,
//                                    final Intent data){
//        Log.v(TAG, "Found method Activity Result");
//        try {
//            if (requestCode == CHOOSE_ACCOUNT && resultCode == RESULT_OK) {
//                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
//                Log.v(TAG, accountName);
//            } else {
//                Log.v(TAG, "There was an error in the account picker");
//            }
//        } catch (Exception e) {
//            Log.v(TAG, "Exception " + e);
//        }
//    }

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

            case PERMISSION_REQUEST_ACCESS_IMAGE_CAPTURE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //What will happen if yes??
                    Uri galleryUri = takePictureIntent.getData();

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
                        Log.v(TAG, "Exception " + e);
                        Toast.makeText(this, "Unnabble to find the image or it is unavailable", Toast.LENGTH_LONG).show();
                    }
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    //Override onclick to open the camera on button 2
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if ( id == R.id.button2){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_ACCESS_IMAGE_CAPTURE);
        }
        else{
            //Go back to main button
            intent = new Intent(this, Home.class);
            startActivity(intent);
        }
    }

    //------------------------------------------------------------------------------------------
    //GALLERY OF IMAGES:
    //Add the method to invoke the Gallery of the phone
    public void onImageGallery(View v) {

        //Add the image Gallery using an implicit intent.
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        //Create a variable File, with name: galleryDir. Link the Gallery to the Directory
        File galleryDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String galleryDirPath = galleryDir.getPath();

        //Especify URI
        Uri data = Uri.parse(galleryDirPath);

        //What is the data type? I want all images , includes all extentions for images:
        photoPickerIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoPickerIntent, PERMISSION_REQUEST_ACCESS_IMAGE_GALLERY);
    }


    //--------------------------------------------------------------------------------------------
    //NOW we need to get the request code
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            if (requestCode == PERMISSION_REQUEST_ACCESS_IMAGE_GALLERY){

            }
        }
    }


}
