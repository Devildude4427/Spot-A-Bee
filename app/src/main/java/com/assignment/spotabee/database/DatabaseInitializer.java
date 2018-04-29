package com.assignment.spotabee.database;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.assignment.spotabee.customutils.Time;

import java.util.Date;
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
        Date testDate = new Date();

        Description description = new Description(53.3094,
                -4.6330, "HolyHead", "A daffodil or something",
                "None", 2, Time.getTodaysDate(testDate), Time.getCurrentTime(testDate));
        addDescription(db, description);

        List<Description> descriptionList = db.descriptionDao().getAllDescriptions();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + descriptionList.size());
        for (Description d : descriptionList) {
            Log.d(TAG, d.getFlowerType().toString());
            Log.d(TAG, d.getLatitude().toString());
            Log.d(TAG, d.getLongitude().toString());
            Log.d(TAG, d.getNumOfBees() + "");
            Log.d(TAG, d.getFurtherDetails().toString());
            Log.d(TAG, d.getDate().toString());
            Log.d(TAG, d.getTime().toString());
        }
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
