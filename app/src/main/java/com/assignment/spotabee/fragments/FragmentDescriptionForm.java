package com.assignment.spotabee.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.assignment.spotabee.R;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.database.Description;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FragmentDescriptionForm extends Fragment
        implements View.OnClickListener {

    // Widgets
    private AppCompatButton submit;
    private AppCompatEditText location;
    private AppCompatEditText flower;
    private AppCompatEditText description;
    private AppCompatEditText numberOfBeesField;
    private AppCompatEditText timeField;
    private AppCompatEditText dateField;
    private ImageView flowerIdentify;
    private FrameLayout flowerSearch;
    private Spinner addressSpinner;

    // Database
    private AppDatabase db;

    // Outside helpers
    private Geocoder geocoder;

    // Activity specific values
    private Context context;
    private List<Address> possibleUserAddresses;
    private LatLng userLocation;
    private static final String TAG = "Description_form_debug";
    private final static String imageIdentifyUrl = "https://www.imageidentify.com/";
    String flowerIdentification;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            flowerIdentification = getArguments().getString("flowerName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_description_form, container, false);

        ImageView search = rootView.findViewById(R.id.search_location);
        search.setOnClickListener(this);


        addressSpinner = (Spinner) rootView.findViewById(R.id.addressSpinner);
        addressSpinner.setVisibility(View.GONE);

        flower = rootView.findViewById(R.id.flowerField);
        if(flowerIdentification != null){
            flower.setText(flowerIdentification);
        }

        location = rootView.findViewById(R.id.locationField);
        description = rootView.findViewById(R.id.descriptionField);
        numberOfBeesField = rootView.findViewById(R.id.numOfBees);
        timeField = rootView.findViewById(R.id.formTime);
        dateField = rootView.findViewById(R.id.formDate);

        submit = rootView.findViewById(R.id.submit);
        flowerIdentify = rootView.findViewById(R.id.flowerIdentify);
        flowerSearch = rootView.findViewById(R.id.flower_search);

        flowerSearch.setOnClickListener(this);
        submit.setOnClickListener(this);

        db = AppDatabase.getAppDatabase(getContext());


        context = getActivity();

        userLocation = null;
        geocoder = new Geocoder(context.getApplicationContext());

        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location.setText(parent.getItemAtPosition(position).toString());
                setCoordinatesToStore(parent, view, position, id);

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(R.string.details_title);
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentDescriptionForm newInstance(String param1, String param2) {
        FragmentDescriptionForm fragment = new FragmentDescriptionForm();
        Bundle args = new Bundle();

        return fragment;
    }

    public void goToImageIdentify(){
        Intent imageIdentify = new Intent(Intent.ACTION_VIEW);
        imageIdentify.setData(Uri.parse(imageIdentifyUrl));
        startActivity(imageIdentify);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.flower_search:
                goToImageIdentify();
                break;

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
                } catch (IOException e) {
                    Toast.makeText(context,
                            "Woops! Couldn't find your address..."
                                    + "Maybe try a different search?",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: Error");
                    Log.d(TAG, e.getMessage());
                    this.userLocation = new LatLng(51.5842, 2.9977);
                }
        }
    }

    private boolean userLocationIsNull() {
        Log.d(TAG, "We are in setCoordinatesToStore");
        if (this.userLocation == null) {
            Toast.makeText(context,
                    "You need to select a location first",
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "userLocationIsNull: TRUE");
            return true;
        }
        return false;
    }

    private void updateSpinner(ArrayAdapter<String> stringAddressAdapter) {
        Log.d(TAG, "We are in updateSpinner");
        stringAddressAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        addressSpinner.setAdapter(stringAddressAdapter);
    }

    @Nullable
    private ArrayAdapter<String> getStringArrayAdapter() throws IOException {
        Log.d(TAG, "We are in getStringArrayAdapter");
        String locationToSearch = location.getText().toString();
        if (geocoder.isPresent()) {
            Log.d(TAG, "Geocoder is present");
        } else {
            Log.d(TAG, "Geocode is NOT present");
        }
        this.possibleUserAddresses = geocoder.getFromLocationName(locationToSearch, 5);
        Log.d(TAG, "geocoder has finished search results");

        List<String> addressLines = new ArrayList<>();
        for (Address address : this.possibleUserAddresses) {
            addressLines.add(address.getAddressLine(0));
        }
        Log.d(TAG, "Have stored addresses in array");

        ArrayAdapter<String> stringAddressAdapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item,
                addressLines
        );
        Log.d(TAG, "Have made an array adapter");

        if (possibleUserAddresses.size() > 0) {
            Log.d(TAG, "At least one address detected");
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
        Log.d(TAG, "We are in commitFormToDB");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                try {

                    db.descriptionDao()
                            .insertDescriptions(new Description(
                                    ( userLocation.latitude),
                                    userLocation.longitude,
                                    location.getText().toString(),
                                    flower.getText().toString(),
                                    description.getText().toString(),
                                    Integer.parseInt(numberOfBeesField.getText().toString()),
                                    dateField.getText().toString(),
                                    timeField.getText().toString()
                            ));

                    List<Description> allDescriptions = db.descriptionDao()
                            .getAllDescriptions();

                    for (Description description : allDescriptions) {
                        Log.d(TAG, description.getFlowerType().toString());
                        Log.d(TAG, description.getLatitude().toString());
                        Log.d(TAG, description.getLongitude().toString());
                        Log.d(TAG, description.getNumOfBees() + "");
                        Log.d(TAG, description.getFurtherDetails().toString());
                        Log.d(TAG, description.getDate().toString());
                        Log.d(TAG, description.getTime().toString());
                    }


                } catch (Exception e) {
                    Toast.makeText(context,
                            "Sorry. An error occurred. We can't save your information right now...",
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error: " + e.getMessage());
                }

            }
        });
    }

    private Address setCoordinatesToStore(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "We are in setCoordinatesToStore");
        String addressLineToMatch = parent.getItemAtPosition(position).toString();
        Address addressToFind = null;

        for (Address address : this.possibleUserAddresses) {
            if (address.getAddressLine(0).equals(addressLineToMatch)) {
                addressToFind = address;
                Log.d(TAG, "We are have found an address");
            }
        }

        if (addressToFind == null) {
            return addressToFind;
        } else {
            this.userLocation = new LatLng(addressToFind.getLatitude(), addressToFind.getLongitude());
            Log.d(TAG, "new user location has been made");
            return addressToFind;
        }

    }



}
