package com.assignment.spotabee;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

//import com.assignment.spotabee.Home.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Home extends AppCompatActivity implements View.OnClickListener {


    //--------------------------------UPLOADING A PICTURE AND CONNECTING TO GALLERY-----------------------------------
    public static final int IMAGE_GALLERY_REQUEST = 20;
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

    }


    //Override onclick to open the camera on button 2
    @Override
    public void onClick(View v) {

        int id = v.getId();
        //Open the camera HOPEFULLY
        if ( id == R.id.button2){
            dispatchTakePictureIntent();
        }else{
            //Go back to main button
            intent = new Intent(this, Home.class);
            startActivity(intent);
        }


    }

    static final int REQUEST_IMAGE_CAPTURE = 10;

    private void dispatchTakePictureIntent() {

        Log.d("TEST", "HELLO THERE");


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        //}
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

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }
    //--------------------------------------------------------------------------------------------

    //NOW we need to get the request code
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST){
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
            }



        }



    }


}
