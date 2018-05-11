package com.assignment.spotabee;

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
 * Flower type, location, time, date, number of bees will be added
 * at appropriate points throughout the user's journey
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

    private static final String OBSCENE_NUMBER_MESSAGE = "That is an obscene number of bees to be spotted\n" +
            "on one bunch of flowers! Please enter a number less than pr equal to 50.";
    private static final String TAG = "Flower debug";

    public Flower(String species, String date,
                  String time, int numOfBees,
                  double latitude, double longitude,String description,
                  Context context) throws ObsceneNumberException{
        if(numOfBees > 50){
            throw new ObsceneNumberException(OBSCENE_NUMBER_MESSAGE);
        }
        this.species = species;
        this.date = date;
        this.time = time;
        this.numOfBees = numOfBees;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;

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
            throw new ObsceneNumberException(OBSCENE_NUMBER_MESSAGE);
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void commitDataToDB(final Context context) {
        Log.d(TAG, "We are in commitFormToDB");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    db.databasenDao()
                            .insertDescriptions(new Description(
                                    getLatitude(),
                                    getLongitude(),
                                    getSpecies(),
                                    getDescription(),
                                    getNumOfBees(),
                                    getDate(),
                                    getTime()));


                    List<Description> allDescriptions = db.databasenDao()
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
