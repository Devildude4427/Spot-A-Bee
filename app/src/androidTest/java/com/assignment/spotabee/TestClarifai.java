package com.assignment.spotabee;

import com.assignment.spotabee.imagerecognition.ClarifaiClientGenerator;
import com.assignment.spotabee.imagerecognition.ClarifaiRequest;
import com.assignment.spotabee.imagerecognition.FlowerIdentificationModel;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
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


    // Commented out because Android Studio can't find the input file
//    @Before
//    public void initialise() {
//
//
//        flowerIdentificationModel = new FlowerIdentificationModel(new File("src/androidTest/res/comparativeSunflower.jpg"));
//
////      ?  clarifaiRequest = new ClarifaiRequest(client, "flower_species", flowerIdentificationModel.getImageFileToIdentify());
//
//    }
//
//    @Test
//    public void testCorrectIdentification() {
//        assertEquals("sunflower", clarifaiRequest.executRequest());
//    }
}