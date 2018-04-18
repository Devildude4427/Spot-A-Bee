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


    public Description(Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Ignore
    public Description(Double latitude, Double longitude, String location, String flowerType, String furtherDetails){
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.flowerType = flowerType;
        this.furtherDetails = furtherDetails;
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

    public void setFlowerType(String flowerType) {
        this.flowerType = flowerType;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getFlowerType() {
        return flowerType;
    }

    public void setFlower(String flowerType) {
        this.flowerType = flowerType;
    }

    public String getFurtherDetails() {
        return furtherDetails;
    }

    public void setFurtherDetails(String furtherDetails) {
        this.furtherDetails = furtherDetails;
    }
}
