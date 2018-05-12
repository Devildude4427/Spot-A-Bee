package com.assignment.spotabee.imagerecognition;
/**
 * Made by: C1769948
 */

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Prediction;

public abstract class ClarifaiRequest {
    private static final Logger LOGGER = Logger.getLogger( ClarifaiRequest.class.getName());


    public abstract String execute();

    public String getMostLikelyPrediction(ClarifaiResponse<List<ClarifaiOutput<Prediction>>> listClarifaiResponse){
        // Get most likely prediction from Clarifai
        List<Prediction> predictions = listClarifaiResponse.get().get(0).data();
        Prediction mostLikelyFlower = predictions.get(0);

        // Get the name of the prediction
        LOGGER.log(Level.SEVERE, mostLikelyFlower.asConcept().name());
        return mostLikelyFlower.asConcept().name();
    }
}
