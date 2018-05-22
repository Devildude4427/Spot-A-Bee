package com.assignment.spotabee;
/**
 * Made by: C1769948
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.assignment.spotabee.customexceptions.ObsceneNumberException;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.database.Description;

import java.util.List;

/**
 * Model containing all data about a flower submission.
 *
 */

public class Flower {
    private String species;
    private String date;
    private String time;
    private int numOfBees;
    private double latitude;
    private double longitude;
    private String description;

    private AppDatabase db;

    private String obsceneNumMessage;

    private final String TAG = "Flower debug";

    public Flower(String species, String date,
                  String time, int numOfBees,
                  double latitude, double longitude,String description,
                  Context context) throws ObsceneNumberException{
        if(numOfBees > 50){
            throw new ObsceneNumberException(obsceneNumMessage);
        }
        this.species = species;
        this.date = date;
        this.time = time;
        this.numOfBees = numOfBees;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.obsceneNumMessage = context.getString(R.string.obscene_number_message);

        db = AppDatabase.getAppDatabase(context);
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumOfBees() {
        return numOfBees;
    }

    public void setNumOfBees(int numOfBees) throws ObsceneNumberException{
        if (numOfBees <= 50){
            this.numOfBees = numOfBees;
        } else {
            throw new ObsceneNumberException(obsceneNumMessage);
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }
//
    public void setDescription(String description) {
        this.description = description;
    }


    public void commitDataToDB(final Context context) {
        Log.d(TAG, "We are in commitToDB");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    db.databaseDao()
                            .insertDescriptions(new Description(
                                    latitude,
                                    longitude,
                                    species,
                                    description,
                                    numOfBees,
                                    date,
                                    time
                            ));


                    List<Description> allDescriptions = db.databaseDao()
                            .getAllDescriptions();

                    for (Description description : allDescriptions) {
                        Log.d(TAG, description.getFlowerType().toString());
                        Log.d(TAG, description.getLatitude().toString());
                        Log.d(TAG, description.getLongitude().toString());
                        Log.d(TAG, "Number Of BEES: " + description.getNumOfBees());
                        Log.d(TAG, description.getFurtherDetails().toString());
                        Log.d(TAG, description.getDate().toString());
                        Log.d(TAG, description.getTime().toString());
                    }


                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(context,
                            "Sorry. An error occurred. We can't save your information right now...",
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error: " + e.getMessage());
                }

            }
        });
    }


}
