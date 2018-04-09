package com.assignment.spotabee.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.assignment.spotabee.DescriptionForm;

import com.assignment.spotabee.database.Description;
import com.assignment.spotabee.database.DescriptionDao;

@Database(entities = {Description.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DescriptionDao descriptionDao();

}
