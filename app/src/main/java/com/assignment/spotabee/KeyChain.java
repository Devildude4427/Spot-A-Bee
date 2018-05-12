package com.assignment.spotabee;

/**
 * Made by: C1769948
 */
public class KeyChain {
    private static final String CURRENT_FRAGMENT_KEY = "current_fragment";

    private static final String PAYPAL_CLIENT_ID = "ATrEZJJShX-0PfR3f6gra9C1Dxjn3RTWGCGrKRI3syITNnPtfX_THta5U4gX8uSLHCElyhafhG_VU0ch";

    //This will be called from Payment
    private static final int PAYPAL_REQUEST_CODE = 1717;

    private static final int CHOOSE_ACCOUNT = 1;

    private static final String CLARIFAI_API_KEY = "d984d2d494394104bb4bee0b8149523d";

    public static String getCurrentFragmentKey() {
        return CURRENT_FRAGMENT_KEY;
    }

    public static String getPaypalClientId() {return PAYPAL_CLIENT_ID; }

    public static int getPaypalRequestCode() {
        return PAYPAL_REQUEST_CODE;
    }

    public static int getChooseAccount() {
        return CHOOSE_ACCOUNT;
    }

    public static String getClarifaiApiKey() {return CLARIFAI_API_KEY;}
}


