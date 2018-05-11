package com.assignment.spotabee.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.assignment.spotabee.BuildConfig;
import com.assignment.spotabee.KeyChain;
import com.assignment.spotabee.LocationHelper;
import com.assignment.spotabee.MainActivity;
import com.assignment.spotabee.customutils.CheckNetworkConnection;
import com.assignment.spotabee.customutils.FileOp;
import com.assignment.spotabee.imagerecognition.ByteClarifaiRequest;
import com.assignment.spotabee.imagerecognition.ClarifaiClientGenerator;

import com.assignment.spotabee.outdated.OutdatedClassMap;
import com.assignment.spotabee.R;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.database.Description;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import clarifai2.api.ClarifaiClient;

import static android.location.LocationManager.GPS_PROVIDER;
import static com.assignment.spotabee.MainActivity.getContextOfApplication;

/**
 * Home fragment. Creates and controls all
 * camera activity and the views on the home screen.
 */
public class FragmentHome extends Fragment  {

    /**
     * TAG used in Log statements that can narrow down where the message
     * or error is coming from.
     */
    private static final String TAG = "Home Debug";

    /**
     * Request code that is sent when the app wants to
     * take a picture.
     */
    public static final int IMAGE_CAPTURE = 1;

    /**
     * Request code that is sent when the app wants to
     * open the gallery.
     */
    public static final int IMAGE_GALLERY = 2;

    /**
     * Manager for the location listener, handles it's outputs.
     */
    private LocationManager locationManager;

    /**
     * Checks to see if the device has moved to new locations.
     */
    private LocationListener locationListener;

    /**
     * Instance of a database.
     */
    private AppDatabase db;


    /**
     * Instance of a ClarifaiClient. Necessary in order to compare images
     * taken.
     */
    private static ClarifaiClient client;

    /**
     * Intent for use somewhere on this page.
     */
    private Intent intent;

    /**
     * File path for the current photo. Used
     * to save a final version of it.
     */
    private String currentPhotoPath;


    /**
     * On create of the fragment, loads the layout
     * of the page.
     *
     * @param inflater Creates the layout for the fragment.
     * @param container Assigns the overall container
     *                  that the fragment sits in.
     * @param savedInstanceState Save the state so that the
     *                           fragment can be opened and
     *                           shut without losing your
     *                           changes.
     * @return The finished view for the fragment.
     */
    @Nullable
    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater,
                             final @Nullable ViewGroup container,
                             final @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        //Set the button to a listener
        View view = inflater.inflate(
                R.layout.fragment_menu_home, container, false);

        locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new OutdatedClassMap().new MyLocationListener();

//        locationListener = new FragmentMap().new MyLocationListener();

        db = AppDatabase.getAppDatabase(getContext());


        ImageView buttonCamera = view.findViewById(R.id.button_camera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                int id = v.getId();
                //Open the camera HOPEFULLY

                if (id == R.id.button_camera) {
                    dispatchTakePictureIntent();
                } else {
                    //Go back to main button
                    intent = new Intent(getActivity()
                            .getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }


                try {
                    if (ContextCompat.checkSelfPermission(
                            getActivity().getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(
                                GPS_PROVIDER, 5000, 10, locationListener);

                        Double lat = locationManager
                                .getLastKnownLocation(GPS_PROVIDER)
                                .getLatitude();
                        Double lng = locationManager
                                .getLastKnownLocation(GPS_PROVIDER)
                                .getLongitude();
                        Log.v(TAG, "Lat: " + lat + "Lng: " + lng);

                        db.databasenDao()
                                .insertDescriptions(new Description(
                                        lat,
                                        lng)
                                );
                    }
                } catch (Exception e) {
                    Log.v(TAG, "Exception " + e);
                }
            }
        });

        ImageView buttonDescriptionForm = view.findViewById(
                R.id.button_no_image_upload);
        buttonDescriptionForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //creating fragment object
                Fragment fragment = null;

                Bundle locationArgs = new Bundle();
                locationArgs.putBoolean("formSelected", true);
                fragment = new LocationHelper();
                fragment.setArguments(locationArgs);

                //replacing the fragment
                FragmentTransaction ft = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        });

        ImageView buttonUploadPictures = view.findViewById(
                R.id.button_image_upload);
        buttonUploadPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onImageGallery();
            }
        });
        return view;
    }

    /**
     * Once the view is created, it sets the title
     * and will handle any other fragment methods.
     *
     * @param view The view return from 'onCreateView'
     * @param savedInstanceState What instance state the
     *                           fragment is currently on.
     */
    @Override
    public void onViewCreated(final @NonNull View view,
                              final @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here
        //for different fragments different titles
        getActivity().setTitle("Home");
    }

    /**
     * Controls what a picture's name will be
     * and where it will be saved on the phone.
     *
     * @return The new file.
     * @throws IOException Exception in case of file
     *                  not being created properly due
     *                  to rights or invalid directory.
     */
    private File createImageFile() throws IOException {
        File image = null;
        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            String imageFileName = "JPEG_"
                    + timeStamp + "_";
            File storageDir = new File(Environment
                    .getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM), "Camera");

            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",   /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = "filepath: " + image.getAbsolutePath();
            Log.v(TAG, "filepath: " + image.getAbsolutePath());
            return image;
        } catch (Exception e) {
            Log.e(TAG, "Exception in createImageFile(): " + e.getMessage());

        }
        return image;
    }



    /**
     * Starts the process of taking a picture and saving it.
     */
    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent
                    = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity()
                    .getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.v(TAG, "IO Exception " + ex);
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider
                            .getUriForFile(getContextOfApplication(),
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            photoURI);
                    startActivityForResult(takePictureIntent, IMAGE_CAPTURE);
                    Log.v(TAG, "Exiting");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in dispatch " + e.getMessage());
        }
    }

    /**
     * Method for saving the image.
     */
    private void galleryAddPic() {
        try {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            getContextOfApplication().sendBroadcast(mediaScanIntent);
        } catch (Exception e) {
            Log.e(TAG, "Exception in galleryAddPic(): " + e.getMessage());
        }
    }


    /**
     * Allows user to upload picture from phone's storage.
     */
    public void onImageGallery() {
        Log.v(TAG, "onImageGallery");
        startActivityForResult(new Intent(Intent.ACTION_PICK)
                .setType("image/*"), IMAGE_GALLERY);
    }

    private void handleImageIdentification(final Intent data) {
        Log.d(TAG, "Have started handleImageIdentification()");
            final ProgressDialog progress
                    = new ProgressDialog(getContextOfApplication());
            progress.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        progress.dismiss();
                    }
                    return true;
                }
            });

        try {

                progress.setTitle("Loading");
                progress.setMessage("Identify your flower..");
                progress.setCancelable(false);
                progress.show();

                if (!CheckNetworkConnection.isInternetAvailable(
                        getContextOfApplication())) {
                    Log.e(TAG, "No internet connection");
                    progress.dismiss();
                    Toast.makeText(getContextOfApplication(),
                            "Internet connection unavailable.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                client = ClarifaiClientGenerator.generate(KeyChain.getClarifaiApiKey());
                Log.d(TAG, "Have made clarifai client");
                final byte[] imageBytes
                        = FileOp.getByteArrayFromIntentStoreData(
                        getContextOfApplication(), data);
                Log.e(TAG, "Have gotten image bytes");
                if (imageBytes != null) {
                    Log.e(TAG, "Image bytes aren't null");
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "We have started run thread");
                            ByteClarifaiRequest clarifaiRequest
                                    = new ByteClarifaiRequest(client,
                                    "flower_species", imageBytes);
                            String flowerType = clarifaiRequest.execute();
                            Log.d(TAG, "Flower Type: " + flowerType);



                            Bundle locationFormBundle = new Bundle();
                            locationFormBundle.putString("flowerName",
                                    flowerType);


                            locationFormBundle.putBoolean("formSelected", true);

                            LocationHelper locationHelper
                                    = new LocationHelper();
                            locationHelper.setArguments(
                                    locationFormBundle);

                            FragmentTransaction fragmentTransaction
                                    = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(
                                    R.id.content_frame,
                                    locationHelper);
                            fragmentTransaction.commit();
                            progress.dismiss();
                        }
                    });
                } else {
                    Log.e(TAG, "Image bytes are null");
                    progress.dismiss();
                }
        } catch (Exception e) {
            progress.dismiss();
        }

    }

    /**
     * Handles the results from calling the gallery and
     * the camera.
     *
     * @param requestCode Which service is being requested.
     * @param resultCode Whether or not the service finished
     *                   correctly.
     * @param data Any data sent over. In this case, it would
     *             be the picture intent itself.
     */
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Creating a progress dialogue to show the user whilst
        // the Clarifai request is executing that can be dismissed on back press
        final ProgressDialog progress
                = new ProgressDialog(getContextOfApplication());
        progress.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    progress.dismiss();
                }
                return true;
            }
        });



        Log.v(TAG, "entering");

        try {
            if (requestCode == IMAGE_GALLERY) {
                // Displaying progress dialogue
                progress.setTitle("Loading");
                progress.setMessage("Identifying your flower..");
                progress.setCancelable(false);
                progress.show();

                // Verifying internet is available before making a Clarifai request
                if (!CheckNetworkConnection.isInternetAvailable(
                        getContextOfApplication())) {
                    progress.dismiss();
                    Toast.makeText(getContextOfApplication(),
                            "Internet connection unavailable.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Creating a ClarifaiClient to handle the request
                client = ClarifaiClientGenerator.generate(KeyChain.getClarifaiApiKey());

                // Creating a byte array of the image selected by the user that can be passed
                // to a ClarifaiRequest
                final byte[] imageBytes
                        = FileOp.getByteArrayFromIntentData(
                        getContextOfApplication(), data);
                if (imageBytes != null) {

                    // Executing Clarifai request and starting method
                    // to take a user to the description form with their location automatically
                    // identified and the flower species field of the form automatically filled out
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "We have started run thread");
                            ByteClarifaiRequest clarifaiRequest
                                    = new ByteClarifaiRequest(client,
                                    "flower_species", imageBytes);
                            String flowerType = clarifaiRequest.execute()
                                    ;
                            Log.d(TAG, "Flower Type: " + flowerType);

                            goToDescriptionFormWithBundle(flowerType);
                            progress.dismiss();
                        }
                    });
                }
            } else if (requestCode == IMAGE_CAPTURE) {
                Log.v(TAG, "Entering Image captured");
                galleryAddPic();
                handleImageIdentification(data);
            }
        } catch (Exception e) {
            try {
                progress.dismiss();
            } catch (Exception ex) {
                Log.v(TAG, "Exception " + ex);
            }

            Log.v(TAG, "Exception with Activity Start " + e);
            e.printStackTrace();
        }
    }

    private void goToDescriptionFormWithBundle(String flowerName){
        Bundle locationFormBundle = new Bundle();
        locationFormBundle.putString("flowerName",
                flowerName);

        locationFormBundle.putBoolean("formSelected", true);

        LocationHelper locationHelper
                = new LocationHelper();
        locationHelper.setArguments(
                locationFormBundle);

        FragmentTransaction fragmentTransaction
                = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(
                R.id.content_frame,
                locationHelper);
        fragmentTransaction.commit();
    }

}
