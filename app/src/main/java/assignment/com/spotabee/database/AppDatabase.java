package assignment.com.spotabee.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import assignment.com.spotabee.DescriptionForm;

@Database(entities = {Description.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DescriptionDao descriptionDao();

}
