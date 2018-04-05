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


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.assignment.spotabee.database.Description;
import com.assignment.spotabee.database.AppDatabase;
import com.google.android.gms.maps.model.LatLng;


public class DescriptionForm extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton submit;
    private AppCompatEditText location;
    private AppCompatEditText flower;
    private AppCompatEditText description;
    private ImageView search;

    private AppDatabase db;

    private Context context;
    private List<Address> possibleUserAddresses;
//    private PlaceAutocompleteFragment autocompleteFragment;

    private static final String TAG = "DESCRIPTION_FORM";
    private Geocoder geocoder;
    private Spinner addressSpinner;
    private LatLng userLocation;
    //private List<String> addressLines;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);


        context = this;

        this.userLocation = null;

        this.search = findViewById(R.id.search_location);
        this.search.setOnClickListener(this);

        this.addressSpinner = (Spinner) findViewById(R.id.addressSpinner);
        this.addressSpinner.setVisibility(View.GONE);

        this.geocoder  = new Geocoder(DescriptionForm.this);

        AppCompatButton submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "App Database"
        ).fallbackToDestructiveMigration().build();

        flower = findViewById(R.id.flowerField);
        location = findViewById(R.id.locationField);
        description = findViewById(R.id.descriptionField);


    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.submit:
                if(this.userLocation == null){
                    Toast.makeText(DescriptionForm.this,
                            "You need to select a location first",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            db.descriptionDao()
                                    .insertDescriptions(new Description(
                                            ((Double) userLocation.latitude),
                                            (Double) userLocation.longitude,
                                            flower.getText().toString(),
                                            description.getText().toString()
                                    ));

                            List<Description> allDescriptions = db.descriptionDao()
                                    .getAllDescriptions();

                            for(Description description : allDescriptions){
                                Log.d(TAG, description.getFlowerType().toString());
                                Log.d(TAG, description.getLatitude().toString());
                                Log.d(TAG, description.getLongitude().toString());
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
                this.addressSpinner.setVisibility(View.VISIBLE);
                try {
                    String locationToSearch = location.getText().toString();
                    this.possibleUserAddresses = geocoder.getFromLocationName(locationToSearch,  5);
                    for(Address address : this.possibleUserAddresses){
                        Log.d(TAG,  address.getLatitude() + ", " + address.getLongitude());
                    }

                    List<String> addressLines= new ArrayList<>();
                    for(Address address : this.possibleUserAddresses){
                        addressLines.add(address.getAddressLine(0));
                    }


                    ArrayAdapter<Address> addressSpinnerAdapter = new ArrayAdapter<Address>(
                            DescriptionForm.this, android.R.layout.simple_spinner_item,
                            possibleUserAddresses
                    );

                    ArrayAdapter<String> stringAddressAdapter = new ArrayAdapter<String>(
                            DescriptionForm.this, android.R.layout.simple_spinner_item,
                            addressLines
                    );

                    if (possibleUserAddresses.size() > 0){
                        this.location.setText(possibleUserAddresses.get(0).getAddressLine(0));
                    } else {
                        Toast.makeText(DescriptionForm.this,
                                "Please change hyour search",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    stringAddressAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    addressSpinner.setAdapter(stringAddressAdapter);
                    addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            location.setText(parent.getItemAtPosition(position).toString());
                            setCoOrdinatesToStore(parent, view, position, id);

                        }
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                } catch (IOException e){
                    Toast.makeText(this, "Woops! Couldn't find your address..." +
                            "Maybe try a different search?", Toast.LENGTH_SHORT).show();
                }

        }
    }

    private Address setCoOrdinatesToStore(AdapterView<?> parent, View view, int position, long id){
        String addressLineToMatch = parent.getItemAtPosition(position).toString();
        Address addressToFind = null;

        for (Address address : this.possibleUserAddresses){
            if(address.getAddressLine(0).equals(addressLineToMatch)){
                addressToFind = address;
            }
        }

        if(addressToFind == null){
            return addressToFind;
        } else {
            this.userLocation = new LatLng(addressToFind.getLatitude(), addressToFind.getLongitude());
            return addressToFind;
        }

    }
}
