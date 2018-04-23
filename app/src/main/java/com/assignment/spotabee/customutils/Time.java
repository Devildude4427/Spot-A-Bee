package com.assignment.spotabee.customutils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by Lauren on 4/23/2018.
 * Class to generate the current date and time and validate user's
 * input of these values if they want to change it themselves
 */

public class Time {
    private Date dateNow;
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy.MM.dd");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private static final Logger LOGGER = Logger.getLogger( Time.class.getName());

    public Time(){
        dateNow = new Date();

    }

    public static void main(String[] args){
        Date testDate = new Date();
        System.out.println(getTodaysDate(testDate));
        System.out.println(getCurrentTime(testDate));
    }
    public static String getTodaysDate(Date dateNow){

        return dateFormat.format(dateNow);
    }

    public static String getCurrentTime(Date dateNow){
        return timeFormat.format(dateNow);
    }
}
