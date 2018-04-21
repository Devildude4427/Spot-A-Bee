package com.assignment.spotabee.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.assignment.spotabee.MainActivity;
import com.assignment.spotabee.OutdatedClassMap;
import com.assignment.spotabee.R;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.database.Description;

import java.io.File;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static android.location.LocationManager.GPS_PROVIDER;
import static com.assignment.spotabee.Permissions.PERMISSION_REQUEST_ACCESS_IMAGE_CAPTURE;
import static com.assignment.spotabee.Permissions.PERMISSION_REQUEST_ACCESS_IMAGE_GALLERY;


public class FragmentHome extends Fragment  {

    private static final String TAG = "Debug";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private AppCompatButton buttonCamera;
    private AppCompatButton buttonDescriptionForm;
    private AppCompatButton buttonUploadPictures;
    private ImageView imgGallery;
    private AppDatabase db;

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


        buttonCamera = (AppCompatButton) view.findViewById(R.id.button_camera);
        buttonCamera.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    //Open the camera HOPEFULLY

                    if (id == R.id.button_camera){
                        dispatchTakePictureIntent();
                    }else{
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

        buttonDescriptionForm = (AppCompatButton) view.findViewById(R.id.button_description_form);
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

        buttonUploadPictures = (AppCompatButton) view.findViewById(R.id.button_upload_picture);
        buttonUploadPictures.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int id = v.getId();
                //Open the camera HOPEFULLY
                if (id == R.id.button_upload_picture){
                    onImageGallery();
                }else{
                    //Go back to main button
                    intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });





        //Create a view/reference for the Gallery
        imgGallery = (ImageView) view.findViewById(R.id.imgGallery);

        return view;
        }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, PERMISSION_REQUEST_ACCESS_IMAGE_CAPTURE);
            //}
        } catch (Exception e){
            Log.v(TAG, "Exception " + e);
        }
    }

    //------------------------------------------------------------------------------------------
    //GALLERY OF IMAGES:
    //Add the method to invoke the Gallery of the phone
    public void onImageGallery() {

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

    public void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data){
        try {
            if (requestCode == PERMISSION_REQUEST_ACCESS_IMAGE_GALLERY && resultCode == RESULT_OK) {
                //What will happen if yes??
                Uri galleryUri = data.getData();

                //Create a Stream to read the image data for the memory
                //If we are unable to catch information from the data for any reasy, try/catch it
                //re edit the exception or put it in the catch block
                InputStream inputStream;
                try {
                    Context applicationContext = MainActivity.getContextOfApplication();
                    inputStream = applicationContext.getContentResolver().openInputStream(galleryUri);

                    // Get Bitmap, get an instance of the image view. Catch info, Tell the users that the image was unable to find.
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    //Show the gallery or image to user:
                    imgGallery.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.v(TAG, "Exception " + e);
                }
            } else if (requestCode == PERMISSION_REQUEST_ACCESS_IMAGE_GALLERY) {
                Log.v(TAG, "There was an error in the image gallery" + resultCode);
            } else {
                Log.v(TAG, "Nothing exists to handle that request code" + requestCode);
            }
        } catch (Exception e) {
            Log.v(TAG, "Exception " + e);
        }
    }

}
