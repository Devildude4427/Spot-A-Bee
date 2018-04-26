package com.assignment.spotabee;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;

import com.assignment.spotabee.imagerecognition.ClarifaiClientGenerator;
import com.assignment.spotabee.imagerecognition.ClarifaiRequest;
import com.assignment.spotabee.imagerecognition.FlowerIdentificationModel;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import clarifai2.api.ClarifaiClient;


import static junit.framework.Assert.assertEquals;

/**
 * Created by Lauren on 4/25/2018.
 * Tests Clarifai business logic away from UI
 */

public class TestClarifai {
    private final Logger LOGGER = Logger.getLogger(com.assignment.spotabee.imagerecognition.TestClarifai.class.getName());
    private final ClarifaiClient client = ClarifaiClientGenerator.generate("d984d2d494394104bb4bee0b8149523d");
    private FlowerIdentificationModel flowerIdentificationModel;
    private ClarifaiRequest clarifaiRequest;
    private Context context;

    @Before
    public void initialise() {
        try {
            context = InstrumentationRegistry.getInstrumentation().getContext();
            InputStream inputSteamOfImage = context.getAssets().open("testimage/comparativeSunflower.jpg");
            byte[] testImageBytes = getBitMap(inputSteamOfImage);

            clarifaiRequest = new ClarifaiRequest(client, "flower_species", testImageBytes);
        } catch (IOException e){
            LOGGER.log(Level.SEVERE, "IOException occurred when retrieving image file");
        }

    }

    public byte[] getBitMap(InputStream inputStream){

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(inputStream);
            final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            return outStream.toByteArray();

        } catch (Exception e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    @Test
    public void testCorrectIdentification() {
        try {
            context = InstrumentationRegistry.getInstrumentation().getContext();
            InputStream inputSteamOfImage = context.getAssets().open("testimages/comparativeSunflower.jpg");
            byte[] testImageBytes = getBitMap(inputSteamOfImage);

            clarifaiRequest = new ClarifaiRequest(client, "flower_species", testImageBytes);
            assertEquals("sunflower", clarifaiRequest.executRequest());
        } catch (IOException e){
            LOGGER.log(Level.SEVERE, "IOException occurred when retrieving image file");
        }
    }
}