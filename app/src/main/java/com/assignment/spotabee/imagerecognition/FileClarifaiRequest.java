package com.assignment.spotabee.imagerecognition;
/**
 * Made by: C1769948
 */

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Prediction;

public class FileClarifaiRequest extends ClarifaiRequest {

    private static final Logger LOGGER = Logger.getLogger( FileClarifaiRequest.class.getName());
    private File imageFile;
    private ClarifaiClient client;
    private String modelId;


    /**
     * @param client - Clarifai client
     * @param modelId - String id for the custom model you want to use to specify results
     * @param imageFile - File image
     */
    public FileClarifaiRequest(ClarifaiClient client, String modelId, File imageFile) {
        this.client = client;
        this.modelId = modelId;
        this.imageFile = imageFile;
    }

    @Override
    public String execute() {
        try {
            // Retrieve a list of ordered predictions
            ClarifaiResponse<List<ClarifaiOutput<Prediction>>> listClarifaiResponse
                    = client.predict(modelId)
                    .withInputs(
                            ClarifaiInput.forImage(imageFile)
                    )
                    .executeSync();

            // Get most likely prediction from Clarifai
            return getMostLikelyPrediction(listClarifaiResponse);
            // Catch in the event the API call was unsuccessful
        } catch (NoSuchElementException e){
            return null;
        }
    }
}
