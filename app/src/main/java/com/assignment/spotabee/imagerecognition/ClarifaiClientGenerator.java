package com.assignment.spotabee.imagerecognition;


import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import okhttp3.OkHttpClient;


/**
 * Created by Lauren on 4/22/2018.
 * Purpose to provide a ClarifaiClient that can be used to make
 * image recognition requests
 */

public class ClarifaiClientGenerator {
    private static final Logger LOGGER = Logger.getLogger( ClarifaiClientGenerator.class.getName());

    /**
     *
     * @param  apiKey - String value API key from Clarifai
     * @return
     */
    public static ClarifaiClient generate(String apiKey){
        ClarifaiClient client= new ClarifaiBuilder(apiKey)
                .client(new OkHttpClient.Builder()
                        .readTimeout(30, TimeUnit.SECONDS) // Increase timeout for poor mobile networks
                        .build()
                )
                .buildSync();
        return client;

    }
}