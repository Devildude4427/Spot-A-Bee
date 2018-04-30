package com.assignment.spotabee.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.assignment.spotabee.database.Description;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DescriptionDao {

    // Description
    @Insert
    void insertDescriptions(Description... descriptions);

    @Query("SELECT * FROM Description")
    List<Description> getAllDescriptions();


    @Transaction
    @Query("SELECT latitude FROM Description")
    List<Double> getAllLatitudes();

    @Transaction
    @Query("SELECT longitude FROM Description")
    List<Double> getAllLongitudes();

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
