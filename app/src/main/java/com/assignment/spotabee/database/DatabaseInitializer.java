package com.assignment.spotabee.database;
/**
 * Made by: C1717381
 */
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.assignment.spotabee.customutils.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Populates the data with test information.
 */
public class DatabaseInitializer {

    /**
     * TAG used for debugging purposes.
     */
    private static final String TAG = DatabaseInitializer.class.getName();

    /**
     * Populates the database asynchronously.
     *
     * @param db An instance of a database.
     */
    public static void populateAsync(@NonNull final AppDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    /**
     * Populates the database synchronously.
     *
     * @param db An instance of a database.
     */
    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    /**
     * Add a description of a flower to the database.
     *
     * @param db An instance of a database.
     * @param description An object that takes multiple
     *                    fields to describe a picture
     *                    or location details.
     * @return The completed description, after it is inserted.
     */
    public static Description addDescription(final AppDatabase db,
                                             final Description description) {
        db.databaseDao().insertDescriptions(description);
        return description;
    }


    /**
     * Fills the database instance with test data.
     *
     * @param db The instance of the database to add to.
     */
    private static void populateWithTestData(final AppDatabase db) {

        DateTime dateTime = new DateTime();

        Description description = new Description(53.3094,
                -4.6330, "A daffodil or something",
                "None", 2, dateTime.getTodaysDate(),
                dateTime.getCurrentTime());
        addDescription(db, description);

        List<Description> descriptionList = db.databaseDao()
                .getAllDescriptions();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + descriptionList.size());
    }


    private static void populateFakeScores(AppDatabase db){
        ArrayList<String> testUsers = new ArrayList<>();
        testUsers.add("Harry");
        testUsers.add("Hermione");

        UserScore userScore;
        for (int i = 0; i <testUsers.size(); i++){
            userScore = new UserScore(testUsers.get(i), i+i);
            db.databaseDao().insertUserScore(userScore);
        }
    }

    /**
     * Sets the database to fill asynchronously, as
     * there is no way to just do this automatically.
     */

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        /**
         * Instance of a database.
         */
        private final AppDatabase mDb;

        /**
         * Finds which database it is to fill.
         *
         * @param db Instance of db to fill.
         */
        PopulateDbAsync(final AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            populateFakeScores(mDb);
            return null;
        }

    }
}
