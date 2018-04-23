package com.assignment.spotabee.database;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final AppDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    public static Description addDescription(final AppDatabase db, Description description) {
        db.descriptionDao().insertDescriptions(description);
        return description;
    }

    private static void populateWithTestData(AppDatabase db) {
        Description description = new Description(52.4816,
                -4.17909, "Somewhere", "A daffodil or something",
                "None", 2, "12-01-18", "12:07");
        addDescription(db, description);

        List<Description> descriptionList = db.descriptionDao().getAllDescriptions();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + descriptionList.size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }
}
