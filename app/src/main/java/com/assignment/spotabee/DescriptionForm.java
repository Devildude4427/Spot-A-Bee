package com.assignment.spotabee;


import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.database.Description;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;


public class DescriptionForm extends Fragment implements View.OnClickListener {

    // Widgets
    private AppCompatButton submit;
    private AppCompatEditText location;
    private AppCompatEditText flower;
    private AppCompatEditText description;
    private ImageView search;
    private Spinner addressSpinner;

    // Database
    private AppDatabase db;

    // Outside helpers
    private Geocoder geocoder;

    // Activity specific values
    private Context context;
    private List<Address> possibleUserAddresses;
    private LatLng userLocation;
    private static final String TAG = "description_form_debug";
    private View v;

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_description_form);
//
//        // Initialisation of widgets
//        this.search = findViewById(R.id.search_location);
//        this.search.setOnClickListener(this);
//        this.addressSpinner = (Spinner) findViewById(R.id.addressSpinner);
//        this.addressSpinner.setVisibility(View.GONE);
//        flower = findViewById(R.id.flowerField);
//        location = findViewById(R.id.locationField);
//        description = findViewById(R.id.descriptionField);
//        this.submit = findViewById(R.id.submit);
//        submit.setOnClickListener(this);
//
//        // Database build
//        db = Room.databaseBuilder(
//                getApplicationContext(),
//                AppDatabase.class,
//                "App Database"
//        ).fallbackToDestructiveMigration().build();
//
//
//        context = this;
//        this.userLocation = null;
//        this.geocoder  = new Geocoder(DescriptionForm.this);
//
//        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                location.setText(parent.getItemAtPosition(position).toString());
//                setCoOrdinatesToStore(parent, view, position, id);
//
//            }
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_description_form, container, false);

        this.search = v.findViewById(R.id.search_location);
        this.search.setOnClickListener(this);
        this.addressSpinner = (Spinner) v.findViewById(R.id.addressSpinner);
        this.addressSpinner.setVisibility(View.GONE);
        flower = v.findViewById(R.id.flowerField);
        location = v.findViewById(R.id.locationField);
        description = v.findViewById(R.id.descriptionField);
        this.submit = v.findViewById(R.id.submit);
        submit.setOnClickListener(this);

        // Database build
        db = Room.databaseBuilder(
                getActivity(),
                AppDatabase.class,
                "App Database"
        ).fallbackToDestructiveMigration().build();


        context = getActivity();
        this.userLocation = null;
        this.geocoder  = new Geocoder(context);

        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location.setText(parent.getItemAtPosition(position).toString());
                setCoOrdinatesToStore(parent, view, position, id);

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return v;
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.submit:
                if (userLocationIsNull()) return;

                commitFormDataToDB();
                break;

            case R.id.search_location:
                Log.d(TAG, "Search icon has been selected");
                this.addressSpinner.setVisibility(View.VISIBLE);

                try {
                    ArrayAdapter<String> stringAddressAdapter = getStringArrayAdapter();
                    if (stringAddressAdapter == null) return;

                    updateSpinner(stringAddressAdapter);
                } catch (IOException e){
                    Toast.makeText(context,
                            "Woops! Couldn't find your address..."
                            + "Maybe try a different search?",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: Error");
                }

        }
    }

    private boolean userLocationIsNull() {
        if(this.userLocation == null){
            Toast.makeText(context,
                    "You need to select a location first",
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "userLocationIsNull: TRUE");
            return true;
        }
        return false;
    }

    private void updateSpinner(ArrayAdapter<String> stringAddressAdapter) {
        stringAddressAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        addressSpinner.setAdapter(stringAddressAdapter);
    }

    @Nullable
    private ArrayAdapter<String> getStringArrayAdapter() throws IOException {
        Log.d(TAG, "we are in getStringArrayAdapter");
        String locationToSearch = location.getText().toString();
        this.possibleUserAddresses = geocoder.getFromLocationName(locationToSearch,  5);


        List<String> addressLines= new ArrayList<>();
        for(Address address : this.possibleUserAddresses){
            addressLines.add(address.getAddressLine(0));
        }


        ArrayAdapter<String> stringAddressAdapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item,
                addressLines
        );

        if (possibleUserAddresses.size() > 0){
            this.location.setText(possibleUserAddresses.get(0).getAddressLine(0));
        } else {
            Toast.makeText(context,
                    "Please change your search",
                    Toast.LENGTH_SHORT).show();
            return null;
        }
        return stringAddressAdapter;
    }

    private void commitFormDataToDB() {
        Log.d(TAG, "We are in commitFormDataToDB");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                try {

                    db.descriptionDao()
                            .insertDescriptions(new Description(
                                    ((Double) userLocation.latitude),
                                    (Double) userLocation.longitude,
                                    location.getText().toString(),
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


                } catch (Exception e){
                    Toast.makeText(context,
                            "Sorry. An error occurred. We can't save your information right now...",
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error: " + e.getMessage());
                }

            }
        });
    }

    private Address setCoOrdinatesToStore(AdapterView<?> parent, View view, int position, long id){
        Log.d(TAG, "We are in setCoOrdinatesToStore");
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
