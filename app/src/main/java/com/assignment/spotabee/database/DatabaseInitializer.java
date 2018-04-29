package com.assignment.spotabee.database;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.assignment.spotabee.customutils.Time;

import java.util.ArrayList;
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

    public static Description addData(final AppDatabase db, Description description, List<UserScore> userScores) {
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

    private static void populateFakeScores(AppDatabase db){
        ArrayList<String> testUsers = new ArrayList<>();
        testUsers.add("Harry");
        testUsers.add("Hermione");
        testUsers.add("Ron");
        testUsers.add("Rachel");
        testUsers.add("Ross");
        testUsers.add("Monica");
        testUsers.add("Chandler");
        testUsers.add("Joey");
        testUsers.add("Phoebe");
        testUsers.add("Frasier");
        testUsers.add("Nial");
        testUsers.add("Daphney");
        testUsers.add("Eddie");
        testUsers.add("Marty");
        testUsers.add("Basil Faulty");
        testUsers.add("Sybil Faulty");
        testUsers.add("Carrie");
        testUsers.add("Charlotte");
        testUsers.add("Sam");
        testUsers.add("Miranda");
        testUsers.add("Chuck");
        testUsers.add("Blaire");
        testUsers.add("Serena");
        testUsers.add("Dan");
        testUsers.add("Jenny");
        testUsers.add("Nate");
        testUsers.add("Vanessa");
        testUsers.add("Charlie");
        testUsers.add("Cece");

        UserScore userScore;
        for (int i = 0; i <testUsers.size(); i++){
            userScore = new UserScore(testUsers.get(i), i+i);
            db.descriptionDao().insertUserScore(userScore);
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
            populateFakeScores(mDb);
            return null;
        }

    }
}
