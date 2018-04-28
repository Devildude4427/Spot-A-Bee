package com.assignment.spotabee.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.assignment.spotabee.MainActivity;
import com.assignment.spotabee.OutdatedClassMap;
import com.assignment.spotabee.R;
import com.assignment.spotabee.customutils.CheckNetworkConnection;
import com.assignment.spotabee.customutils.FileOp;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.database.Description;
import com.assignment.spotabee.imagerecognition.ClarifaiClientGenerator;
import com.assignment.spotabee.imagerecognition.ClarifaiRequest;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import clarifai2.api.ClarifaiClient;

import static android.app.Activity.RESULT_OK;
import static com.assignment.spotabee.Config.Config.PAYPAL_REQUEST_CODE;
import static android.location.LocationManager.GPS_PROVIDER;
import static com.assignment.spotabee.MainActivity.PICK_IMAGE;
import static com.assignment.spotabee.MainActivity.getContextOfApplication;
import static com.assignment.spotabee.Permissions.IMAGE_CAPTURE;
import static com.assignment.spotabee.Permissions.ACCESS_IMAGE_GALLERY;


public class FragmentHome extends Fragment  {

    private static final String TAG = "Home Debug";
    private LocationManager locationManager;
    private LocationListener locationListener;
//    private AppCompatButton buttonCamera;
    private ImageView buttonCamera;
//    private AppCompatButton buttonDescriptionForm;
//    private AppCompatButton buttonUploadPictures;
    private ImageView buttonDescriptionForm;
    private ImageView buttonUploadPictures;
    private AppDatabase db;
    private static final String API_KEY = "d984d2d494394104bb4bee0b8149523d";
    private static ClarifaiClient client;
    private String currentPhotoPath;

    Intent intent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        //Set the button to a listener
        View view = inflater.inflate(R.layout.fragment_menu_home, container, false);

        locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        OutdatedClassMap.MyLocationListener listener = new OutdatedClassMap().new MyLocationListener();
        locationListener = listener;

        db = AppDatabase.getAppDatabase(getContext());


        buttonCamera = view.findViewById(R.id.button_camera);
        buttonCamera.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    //Open the camera HOPEFULLY

                    if (id == R.id.button_camera){
                        Toast.makeText(getActivity(), "We have at least registered an onclick event", Toast.LENGTH_SHORT).show();
                        dispatchTakePictureIntent();
                    } else {
                        //Go back to main button
                        intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }

                    try {
                        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            locationManager.requestLocationUpdates(
                                    GPS_PROVIDER, 5000, 10, locationListener);

                            Double lat = locationManager.getLastKnownLocation(GPS_PROVIDER).getLatitude();
                            Double lng = locationManager.getLastKnownLocation(GPS_PROVIDER).getLongitude();
                            Log.v(TAG, "Lat: " + lat + "Lng: " + lng);

                            db.descriptionDao()
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

        buttonDescriptionForm = view.findViewById(R.id.button_no_image_upload);
        buttonDescriptionForm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //creating fragment object
                Fragment fragment = null;

                //initializing the fragment object which is selected
                fragment = new FragmentDescriptionForm();

                //replacing the fragment
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        });

        buttonUploadPictures = view.findViewById(R.id.button_image_upload);
        buttonUploadPictures.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int id = v.getId();
                //Open the camera HOPEFULLY
                if (id == R.id.button_upload_picture) {
                if (id == R.id.button_image_upload){
                    onImageGallery();
                }else if(id == R.id.btnHome){
                    //Go back to main button
                    Intent intent = new Intent(getActivity().getApplicationContext(), FragmentHome.class);

                }else{
                    //Go back to main button
                    intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
        }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

    private File createImageFile() {
        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = image.getAbsolutePath();
            return image;

        } catch (Exception e) {
            Log.v(TAG, "Exception " + e);
        }
        return null;
    }

    private void dispatchTakePictureIntent() {
        Log.v(TAG, "Started Pic Intent");
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                photoFile = createImageFile();
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Log.v(TAG, "Photo file is not null");
                    Uri photoURI = FileProvider.getUriForFile(getContextOfApplication(),
                            "com.assignment.spotabee.fileprovider",
                            photoFile);
                    Log.v(TAG, "Still bueno");
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    getActivity().startActivityForResult(takePictureIntent, IMAGE_CAPTURE);
                }
            }
        } catch (Exception e) {
            Log.v(TAG, "Exception " + e);
        }
    }

    /**
     * Allows user to upload picture from phone's storage
     */
    public void onImageGallery() {
        Log.v(TAG, "onImageGallery");
        getActivity().startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), PICK_IMAGE);
    }

}
