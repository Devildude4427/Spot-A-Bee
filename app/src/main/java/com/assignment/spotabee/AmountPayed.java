package com.assignment.spotabee;

/**
 * Created by Lauren on 4/28/2018.
 *
 */

public class AmountPayed {
    private static String amountPayed;
    private static String paymentDetails;

    public static String getAmountPayed() {
        return amountPayed;
    }

    public static void setAmountPayed(String amountPayed) {
        AmountPayed.amountPayed = amountPayed;
    }

    public static String getPaymentDetails() {
        return paymentDetails;
    }

    public static void setPaymentDetails(String paymentDetails) {
        AmountPayed.paymentDetails = paymentDetails;
    }
}
