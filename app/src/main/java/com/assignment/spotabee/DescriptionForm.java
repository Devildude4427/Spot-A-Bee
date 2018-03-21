package com.assignment.spotabee;


import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import assignment.com.spotabee.database.Description;
import assignment.com.spotabee.database.AppDatabase;
import assignment.com.spotabee.database.DescriptionDao;


public class DescriptionForm extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton submit;
    private AppCompatEditText location;
    private AppCompatEditText flower;
    private AppCompatEditText description;

    private AppDatabase db;

    private static final String TAG = "DESCRIPTION_FORM";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);

        AppCompatButton submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "App Database"
        ).build();

        AppCompatEditText flower = findViewById(R.id.flowerField);
        AppCompatEditText location = findViewById(R.id.locationField);
        AppCompatEditText description = findViewById(R.id.descriptionField);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.submit:
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.descriptionDao()
                                .insertDescriptions(
                                        new Description("Bute Park",
                                                "Sunflower",
                                                "No comment"));

                        List<Description> allDescriptions = db.descriptionDao()
                                .getAllDescriptions();

                        for(Description description : allDescriptions){
                            Log.d(TAG, description.getFlowerType().toString());
                        }
                    }
                });
                break;
        }
    }
}

