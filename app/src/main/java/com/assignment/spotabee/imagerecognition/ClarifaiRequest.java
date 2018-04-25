package com.assignment.spotabee.imagerecognition;


import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiImage;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Prediction;

/**
 * Created by Lauren on 4/22/2018.
 * Executes image recognition request with a custom model in order to receive application
 * specific outputs.
 */

public class ClarifaiRequest {
    private static final Logger LOGGER = Logger.getLogger( ClarifaiRequest.class.getName());
    private byte[] imageBytes;
    private File imageFile;
    private ClarifaiClient client;
    private String modelId;


    // Overloaded constructors to make requests for both an image in the form
    // of  a byte[] and a File

    /**
     * @param client - Clarifai client
     * @param modelId - String id for the custom model you want to use to specify results
     * @param imageBytes - byte[] of image file
     */
    public ClarifaiRequest(ClarifaiClient client, String modelId, byte[] imageBytes) {
        this.client = client;
        this.modelId = modelId;
        this.imageBytes = imageBytes;
        this.imageFile = null;
    }

    /**
     * @param client - Clarifai client
     * @param modelId - String id for the custom model you want to use to specify results
     * @param imageFile - File image
     */
    public ClarifaiRequest(ClarifaiClient client, String modelId, File imageFile) {
        this.client = client;
        this.modelId = modelId;
        this.imageFile = imageFile;
        this.imageBytes = null;
    }


    public String executRequest(){
        String mostLikelyFlowerName = null;
        ClarifaiResponse<List<ClarifaiOutput<Prediction>>> listClarifaiResponse;

        // Decide if request should be made with File or byte[] object
        if(this.imageBytes == null){
            listClarifaiResponse = requestWithFile();

        } else {
            listClarifaiResponse = requestWithBytes();
        }

        // Get most likely prediction from Clarifai
        List<Prediction> predictions = listClarifaiResponse.get().get(0).data();
        Prediction mostLikelyFlower = predictions.get(0);

        // Get the name of the prediction
        mostLikelyFlowerName = mostLikelyFlower.asConcept().name();
        LOGGER.log(Level.SEVERE, mostLikelyFlower.asConcept().name());

        return mostLikelyFlowerName;
    }

    // Two versions of a method to make the image recognition request
    public ClarifaiResponse<List<ClarifaiOutput<Prediction>>> requestWithBytes(){
        // Using modelId allows us to use a specific custom 'model' we create that stores
        // predictions we are looking for. Models can be 'trained' to recognise these
        // concepts/predictions programmatically or on Clarifai's website
        // https://clarifai.com/developer/guide/train#train
        ClarifaiResponse<List<ClarifaiOutput<Prediction>>> listClarifaiResponse = client.predict(modelId)
                .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBytes)))
                .executeSync();

        return listClarifaiResponse;
    }

    public ClarifaiResponse<List<ClarifaiOutput<Prediction>>> requestWithFile(){
        ClarifaiResponse<List<ClarifaiOutput<Prediction>>> listClarifaiResponse = client.predict(modelId)
                .withInputs(
                        ClarifaiInput.forImage(imageFile)
                )
                .executeSync();
        return listClarifaiResponse;
    }

}
