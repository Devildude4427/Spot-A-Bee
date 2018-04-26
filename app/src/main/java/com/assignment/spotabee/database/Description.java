package com.assignment.spotabee.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;


@Entity
public class Description {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "latitude")
    private Double latitude;

    @ColumnInfo(name = "longitude")
    private Double longitude;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "flower_type")
    private String flowerType;

    @ColumnInfo(name = "further_details")
    private String furtherDetails;

    @ColumnInfo(name = "number_of_bees")
    private int numOfBees;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "time")
    private String time;


    @Ignore
    public Description(Double latitude, Double longitude, String location,
                            String flowerType, String furtherDetails,
                       int numOfBees, String date, String time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.flowerType = flowerType;
        this.furtherDetails = furtherDetails;
        this.numOfBees = numOfBees;
        this.date = date;
        this.time = time;
    }

    public Description(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFlowerType() {
        return flowerType;
    }

    public void setFlowerType(String flowerType) {
        this.flowerType = flowerType;
    }

    public String getFurtherDetails() {
        return furtherDetails;
    }

    public void setFurtherDetails(String furtherDetails) {
        this.furtherDetails = furtherDetails;
    }

    public int getNumOfBees() {
        return numOfBees;
    }

    public void setNumOfBees(int numOfBees) {
        this.numOfBees = numOfBees;
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

}
