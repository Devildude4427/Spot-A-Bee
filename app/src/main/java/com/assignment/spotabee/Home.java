package com.assignment.spotabee;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;

import java.io.File;

import static com.assignment.spotabee.MainActivity.PERMISSION_REQUEST_ACCESS_IMAGE_CAPTURE;
import static com.assignment.spotabee.MainActivity.PERMISSION_REQUEST_ACCESS_IMAGE_GALLERY;


public class Home extends Fragment  {


    private static final String TAG = "Debug";

    AppCompatButton button;
    AppCompatButton button2;
    AppCompatButton button3;

    Intent intent;

    //Override onclick to open the camera on button 2
    public void onClick(View v) {

        int id = v.getId();
        //Open the camera HOPEFULLY
        if ( id == R.id.button2){
            dispatchTakePictureIntent();
        }else{
            //Go back to main button
            startActivity(intent);
        }
    }

    private void dispatchTakePictureIntent() {

        Log.d("TEST", "HELLO THERE");

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
}
