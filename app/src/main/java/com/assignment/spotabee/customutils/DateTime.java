package com.assignment.spotabee.customutils;
/**
 * Made by: C1769948
 */
import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Class to generate the current date and time
 * in String format
 */

public class DateTime {
    private Date dateNow;
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy.MM.dd");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private static final Logger LOGGER = Logger.getLogger( DateTime.class.getName());

    public DateTime(){
        dateNow = new Date();

    }

    public String getTodaysDate(){

        return dateFormat.format(dateNow);
    }

    public String getCurrentTime(){
        return timeFormat.format(dateNow);
    }
}
