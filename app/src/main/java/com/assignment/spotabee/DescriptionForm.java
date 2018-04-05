package com.assignment.spotabee;


import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;

import java.io.IOException;
import java.util.List;

import com.assignment.spotabee.database.Description;
import com.assignment.spotabee.database.AppDatabase;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;


public class DescriptionForm extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton submit;
    private AppCompatEditText location;
    private AppCompatEditText flower;
    private AppCompatEditText description;
    private ImageView search;

    private AppDatabase db;

    private Context context;
//    private PlaceAutocompleteFragment autocompleteFragment;

    private static final String TAG = "DESCRIPTION_FORM";
    private Geocoder geocoder;
    private Spinner addressSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);

//        this.autocompleteFragment = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                // TODO: Get info about the selected place.
//                Log.i(TAG, "Place: " + place.getName());
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO: Handle the error.
//                Log.i(TAG, "An error occurred: " + status);
//            }
//        });

        context = this;

        this.search = findViewById(R.id.search_location);
        this.search.setOnClickListener(this);

        this.addressSpinner = new Spinner(this);

        this.geocoder  = new Geocoder(DescriptionForm.this);

        AppCompatButton submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "App Database"
        ).build();

        flower = findViewById(R.id.flowerField);
        location = findViewById(R.id.locationField);
        description = findViewById(R.id.descriptionField);


    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.submit:

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            db.descriptionDao()
                                    .insertDescriptions(new Description(
                                            location.getText().toString(),
                                            flower.getText().toString(),
                                            description.getText().toString()
                                    ));

                            List<Description> allDescriptions = db.descriptionDao()
                                    .getAllDescriptions();

                            for(Description description : allDescriptions){
                                Log.d(TAG, description.getFlowerType().toString());
                            }

                            finish();

                        } catch (Exception e){
                            Log.e(TAG, "Error: " + e.getMessage());
                        }

                    }
                });
                break;

            case R.id.search_location:
                Log.d(TAG, "search icon has been selected");
                try {
                    String locationToSearch = location.getText().toString();
                    List<Address> possibleUserAddresses = geocoder.getFromLocationName(locationToSearch,  5);
                    for(Address address : possibleUserAddresses){
                        Log.d(TAG,  address.getLatitude() + ", " + address.getLongitude());
                    }

                    ArrayAdapter<Address> addressSpinnerAdapter = new ArrayAdapter<Address>(
                            this, android.R.layout.simple_spinner_item,
                            possibleUserAddresses
                    );

//                    addressSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
//                    addressSpinner.setAdapter(addressSpinnerAdapter);
//                    addressSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Log.d(TAG, "onItemClick... works");
//                        }
//                    });
                } catch (IOException e){
                    Toast.makeText(this, "Woops! Couldn't find your address..." +
                            "Maybe try a different search?", Toast.LENGTH_SHORT);
                }

        }
    }
}
