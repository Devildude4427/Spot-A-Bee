package com.assignment.spotabee.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Description.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DescriptionDao descriptionDao();

}
