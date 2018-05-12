package com.assignment.spotabee.database;
/**
 * Made by: C1769948
 */
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Creates a new database entry.
 */
@Entity
public class Description {

    /**
     * Creates a column with a uniqueId.
     */
    @PrimaryKey(autoGenerate = true)
    private int uid;

    /**
     * Creates a column for latitude.
     */
    @ColumnInfo(name = "latitude")
    private Double latitude;

    /**
     * Creates a column for longitude.
     */
    @ColumnInfo(name = "longitude")
    private Double longitude;


    /**
     * Creates a column for flowerType.
     */
    @ColumnInfo(name = "flower_type")
    private String flowerType;

    /**
     * Creates a column for furtherDetails.
     */
    @ColumnInfo(name = "further_details")
    private String furtherDetails;

    /**
     * Creates a column for numOfBees.
     */
    @ColumnInfo(name = "number_of_bees")
    private int numOfBees;

    /**
     * Creates a column for date.
     */
    @ColumnInfo(name = "date")
    private String date;

    /**
     * Creates a column for time.
     */
    @ColumnInfo(name = "time")
    private String time;


    /**
     * Creates a description object using all fields.
     *
     * @param latitude Double latitude of location.
     * @param longitude Double longitude of location.
     * @param flowerType String type of flower.
     * @param furtherDetails String of any additional comments.
     * @param numOfBees Int number of bees seen.
     * @param date String date bees were seen.
     * @param time String time bees were seen.
     */
    @Ignore
    public Description(final Double latitude, final Double longitude, final String flowerType,
                       final String furtherDetails, final int numOfBees,
                       final String date, final String time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.flowerType = flowerType;
        this.furtherDetails = furtherDetails;
        this.numOfBees = numOfBees;
        this.date = date;
        this.time = time;
    }

    /**
     * Creates a description object with only
     * the latitude and longitude.
     *
     * @param latitude Double latitude of location.
     * @param longitude Double longitude of location.
     */
    public Description(final Double latitude, final Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the itemId field.
     *
     * @return ItemID of database entry.
     */
    public int getUid() {
        return uid;
    }

    /**
     * Sets the item ID field.
     *
     * @param uid item ID.
     */
    public void setUid(final int uid) {
        this.uid = uid;
    }

    /**
     * Gets the latitude field.
     *
     * @return Latitude of database entry.
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude field.
     *
     * @param latitude Latitude coordinate of bee's location.
     */
    public void setLatitude(final Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude field.
     *
     * @return Longitude of database entry.
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude field.
     *
     * @param longitude Longitude coordinate of bee's location.
     */
    public void setLongitude(final Double longitude) {
        this.longitude = longitude;
    }



    /**
     * Gets the flowerType field.
     *
     * @return FlowerType of database entry.
     */
    public String getFlowerType() {
        return flowerType;
    }

    /**
     * Sets the flowerType field.
     *
     * @param flowerType Type of flower that the bee was on.
     */
    public void setFlowerType(final String flowerType) {
        this.flowerType = flowerType;
    }

    /**
     * Gets the furtherDetails field.
     *
     * @return FurtherDetails of database entry.
     */
    public String getFurtherDetails() {
        return furtherDetails;
    }

    /**
     * Sets the furtherDetails field.
     *
     * @param furtherDetails Any additional comments the user adds.
     */
    public void setFurtherDetails(final String furtherDetails) {
        this.furtherDetails = furtherDetails;
    }

    /**
     * Gets the number of bees field.
     *
     * @return NumOfBees of database entry.
     */
    public int getNumOfBees() {

        return numOfBees;
    }

    /**
     * Sets the number of bees field.
     *
     * @param numOfBees How many bees were seen.
     */
    public void setNumOfBees(final int numOfBees) {
        if (numOfBees <= 50){
            this.numOfBees = numOfBees;
        }
    }

    /**
     * Gets the date field.
     *
     * @return Date of database entry.
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date field.
     *
     * @param date When the bees were seen.
     */
    public void setDate(final String date) {
        this.date = date;
    }

    /**
     * Gets the time field.
     *
     * @return DateTime of database entry.
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the time field.
     *
     * @param time DateTime the bee was seen.
     */
    public void setTime(final String time) {
        this.time = time;
    }

}
