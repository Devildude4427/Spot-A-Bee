package com.assignment.spotabee.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.assignment.spotabee.BuildConfig;
import com.assignment.spotabee.MainActivity;
import com.assignment.spotabee.customutils.CheckNetworkConnection;
import com.assignment.spotabee.customutils.FileOp;
import com.assignment.spotabee.imagerecognition.ClarifaiClientGenerator;
import com.assignment.spotabee.imagerecognition.ClarifaiRequest;
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

public class FragmentHome extends Fragment  {

    private static final String TAG = "Home Debug";
    public static final int IMAGE_CAPTURE = 1;
    public static final int IMAGE_GALLERY = 2;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private AppDatabase db;
    private static final String API_KEY = "d984d2d494394104bb4bee0b8149523d";
    private static ClarifaiClient client;
    private String currentPhotoPath;
    private Intent intent;


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


        ImageView buttonCamera = view.findViewById(R.id.button_camera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    //Open the camera HOPEFULLY

                    if (id == R.id.button_camera) {
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

        ImageView buttonDescriptionForm = view.findViewById(R.id.button_no_image_upload);
        buttonDescriptionForm.setOnClickListener(new View.OnClickListener() {
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

        ImageView buttonUploadPictures = view.findViewById(R.id.button_image_upload);
        buttonUploadPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onImageGallery();
            }
        });
        return view;
    }

        @Override
    public void onViewCreated(final @NonNull View view,
                              final @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = "file: " + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.v(TAG, "IO Exception " + ex);
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContextOfApplication(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, IMAGE_CAPTURE);
                }
            }
        } catch (Exception e) {
            Log.v(TAG, "Exception in dispatch " + e);
        }
    }

    /**
     * Allows user to upload picture from phone's storage
     */
    public void onImageGallery() {
        Log.v(TAG, "onImageGallery");
        startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), IMAGE_GALLERY);
    }

    private void galleryAddPic() {
        try {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            getContextOfApplication().sendBroadcast(mediaScanIntent);
        } catch (Exception e) {
            Log.v(TAG, "Exception " + e);
        }
    }

    public void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        try {
            if (requestCode == IMAGE_GALLERY) {

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

                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragmentDescriptionForm);
                            fragmentTransaction.commit();
                            progress.dismiss();
                        }
                    });
                }

            }else {
                Log.v(TAG, "Nothing exists to handle that request code" + requestCode);
            }
        } catch (Exception e) {
            Log.v(TAG, "Exception with Activity Start " + e);
            e.printStackTrace();
        }
    }

}
