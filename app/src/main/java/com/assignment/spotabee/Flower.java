package com.assignment.spotabee;

/**
 * Created by Lauren on 4/23/2018.
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

    public Flower(String species, String date, String time, int numOfBees, double latitude, double longitude) {
        this.species = species;
        this.date = date;
        this.time = time;
        this.numOfBees = numOfBees;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public void setNumOfBees(int numOfBees) {
        this.numOfBees = numOfBees;
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
}
