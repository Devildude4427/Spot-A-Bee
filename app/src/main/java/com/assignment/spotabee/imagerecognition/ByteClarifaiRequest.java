package com.assignment.spotabee.imagerecognition;

/**
 * Made by: C1769948
 */
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiImage;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Prediction;

public class ByteClarifaiRequest extends ClarifaiRequest {
    private static final Logger LOGGER = Logger.getLogger( ByteClarifaiRequest.class.getName());
    private byte[] imageBytes;
    private ClarifaiClient client;
    private String modelId;

    /**
     * @param client - Clarifai client
     * @param modelId - String id for the custom model you want to use to specify results
     * @param imageBytes - byte[] of image file
     */
    public  ByteClarifaiRequest(ClarifaiClient client, String modelId, byte[] imageBytes) {
        this.client = client;
        this.modelId = modelId;
        this.imageBytes = imageBytes;
    }

    @Override
    public String execute() {
        try {
            // Return a List of ordered predictions
            ClarifaiResponse<List<ClarifaiOutput<Prediction>>> listClarifaiResponse
                    = client.predict(modelId)
                    .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBytes)))
                    .executeSync();


            // Get most likely prediction from Clarifai
            return getMostLikelyPrediction(listClarifaiResponse);
            // Catch in the event the API call was unsuccessful
        } catch (NoSuchElementException e){
            return null;
        }
    }
}
