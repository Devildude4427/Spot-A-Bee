package com.assignment.spotabee.database;
/**
 * Initially made by C1769948. Completely refactored to be more efficient by C1717381
 */
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


/**
 * Starts or retrieves the database.
 */
@Database(entities = {Description.class, UserScore.class}, version = 6, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    /**
     * Instance of the database.
     */
    private static AppDatabase instance;

    /**
     * Only method left from initial file created by C1768848 in this file
     */
    /**
     * Description of the flower data.
     *
     * @return the DescriptionDAO.
     */
    public abstract DatabaseDao databaseDao();

    /**
     * Creates a new instance of the database if one
     * does not already exist.
     *
     * @param context Context of the application.
     * @return The instance. If it doesn't exist, it will be
     * a new instance. Otherwise, it will get the existing instance.
     */
    public static AppDatabase getAppDatabase(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "App Database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    /**
     * Destroys the database instance entirely.
     */
    public static void destroyInstance() {
        instance = null;
    }
}

