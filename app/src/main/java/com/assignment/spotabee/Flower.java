package com.assignment.spotabee;

import com.assignment.spotabee.customexceptions.ObsceneNumberException;

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
    private String location;
    private String description;

    public Flower(String species, String date,
                  String time, int numOfBees,
                  double latitude, double longitude,
                  String location, String description) throws ObsceneNumberException{
        if(numOfBees > 50){
            throw new ObsceneNumberException("That is an obscene number of bees to be spotted" +
                    "on one bunch of flowers! Please enter a number less than pr equal to 50.");
        }
        this.species = species;
        this.date = date;
        this.time = time;
        this.numOfBees = numOfBees;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.description = description;
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
            throw new ObsceneNumberException("That is an obscene number of bees to be spotted" +
                    "on one bunch of flowers! Please enter a number less than pr equal to 50.");
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
