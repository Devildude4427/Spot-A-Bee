package com.assignment.spotabee.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * The interface in which the app can add, view, or delete data
 * from the database.
 */
@Dao
public interface DescriptionDao {

    /**
     * Inserts a new row, or rows, into the database.
     *
     * @param descriptions Takes any number of Description
     *                     objects.
     */

    @Insert
    void insertDescriptions(Description... descriptions);

    /**
     * Searches and retrieves all information from
     * the database.
     *
     * @return All rows from the database.
     */
    @Query("SELECT * FROM Description")
    List<Description> getAllDescriptions();

    /**
     * Empties the database completely.
     */
    @Query("DELETE FROM Description")
    void nukeTable();

    // UserScore
    @Insert
    void insertUserScore(UserScore... UserScore);

    @Query("SELECT * FROM UserScore")
    List<UserScore> getAllUserScores();

    @Query("DELETE FROM UserScore")
    void nukeUserScores();

    @Transaction
    @Query("SELECT * FROM UserScore WHERE account_name = :name")
    UserScore getUserScoreByName(String name);

    @Update
    void updateUserScore(UserScore userScore);

}
