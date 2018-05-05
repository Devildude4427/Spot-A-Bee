package com.assignment.spotabee.imagerecognition;


import com.assignment.spotabee.imagerecognition.ClarifaiClientGenerator;
import com.assignment.spotabee.imagerecognition.ClarifaiRequest;
import com.assignment.spotabee.imagerecognition.FlowerIdentificationModel;


import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import clarifai2.api.ClarifaiClient;




public class TestClarifai {
    private static final Logger LOGGER = Logger.getLogger( TestClarifai.class.getName());
    private static final ClarifaiClient client = ClarifaiClientGenerator.generate("d984d2d494394104bb4bee0b8149523d");
    private static FlowerIdentificationModel flowerIdentificationModel;
    private static ClarifaiRequest clarifaiRequest;

    private static void initialise(){
        flowerIdentificationModel = new FlowerIdentificationModel(new File("app/src/main/java/com/assignment/spotabee/testimages/comparativeSunflower.jpg"));
        clarifaiRequest = new ClarifaiRequest(client, "flower_species", flowerIdentificationModel.getImageFileToIdentify());

    }

    public static void testCorrectIdentification(){
        initialise();
        flowerIdentificationModel.setIdentifiedName(clarifaiRequest.executRequest());
        LOGGER.log(Level.SEVERE, "Should be sunflower: " +flowerIdentificationModel.getIdentifiedName());
    }
}



