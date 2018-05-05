package com.assignment.spotabee.imagerecognition;

import java.io.File;

/**
 * Stores file/ byte[] that will be used in a Clarifai request
 * to identify an image and the String name that is returned from
 * such request
 */

public class FlowerIdentificationModel {
    private File imageFileToIdentify;
    private byte[] imageBytesToIdentify;
    private String identifiedName;


    // Overloaded constructor to allow use of different data sources
    public FlowerIdentificationModel(File imageFileToIdentify) {
        this.imageFileToIdentify = imageFileToIdentify;
        this.identifiedName = null;
    }

    public FlowerIdentificationModel(byte[] imageBytesToIdentify) {
        this.imageBytesToIdentify = imageBytesToIdentify;
        this.identifiedName = null;
    }


    public File getImageFileToIdentify() {
        return imageFileToIdentify;
    }

    public byte[] getImageBytesToIdentify() {
        return imageBytesToIdentify;
    }

    public String getIdentifiedName() {
        return identifiedName;
    }

    public void setImageFileToIdentify(File imageFileToIdentify) {
        this.imageFileToIdentify = imageFileToIdentify;
    }

    public void setImageBytesToIdentify(byte[] imageBytesToIdentify) {
        this.imageBytesToIdentify = imageBytesToIdentify;
    }

    public void setIdentifiedName(String identifiedName) {
        this.identifiedName = identifiedName;
    }
}
