package com.assignment.spotabee.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity
public class Description {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "flower_type")
    private String flowerType;

    @ColumnInfo(name = "further_details")
    private String furtherDetails;

    public Description(String location, String flowerType, String furtherDetails){
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
