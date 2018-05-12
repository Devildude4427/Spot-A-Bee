package com.assignment.spotabee;
/**
 * Made by: C1769948
 */
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.assignment.spotabee.customutils.NetworkConnection;
import com.assignment.spotabee.fragments.FragmentDescriptionForm;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;


public class LocationHelper extends android.support.v4.app.Fragment{
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private Context context;
    private LocationResult locationResult;
    private static final String TAG = "Location Helper";
    private boolean haveSelectedForm;
    private String flowerType;
    private NetworkConnection networkConnection;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        this.mFusedLocationClient = new FusedLocationProviderClient(context);

        haveSelectedForm = getArguments().getBoolean("formSelected");
        flowerType = getArguments().getString("flowerName", null);

        getCurrentCoOrdinates();


    }


    public void makeLocationRequest(){
        networkConnection = new NetworkConnection(getActivity());
        Log.d(TAG, "We are in makeLocationRequest()");

        if (ContextCompat.checkSelfPermission(context
                        .getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permissions have been granted");

            if (locationCallback == null)
                locationCallback = new UserLocationCallback();
            Log.d(TAG, "Have created location callback");
            if(networkConnection.internetIsAvailable()){
                this.mFusedLocationClient.requestLocationUpdates(
                        createSingleLocationRequest(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY),
                        locationCallback,
                        null);
                Log.d(TAG, "Have requested single update");
            }

        } else {

        }
    }

    private void setLocationResult(LocationResult locationResult){
        this.locationResult = locationResult;
        Log.d(TAG, "We have set location result object");
    }

    public void getCurrentCoOrdinates(){
        makeLocationRequest();

        if(locationResult != null){
            Log.d(TAG, "Location result ISN'T null");
            Location currentLocation = locationResult.getLastLocation();
            if(currentLocation != null){
                Log.d(TAG, "CurrentLocation object NOT NULL");
            } else {
                Log.d(TAG, "Current location object IS NULL");
            }
            locationResult = null;
        }
        else {
            Log.d(TAG, "Location result IS null");
        }
    }

    public static LocationRequest createSingleLocationRequest(int priority){
        Log.d(TAG, "We are in createSingleLocationRequest()");
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setPriority(priority);
        return mLocationRequest;
    }


    private class UserLocationCallback extends LocationCallback {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "We are in onLocationResult");
            setLocationResult(locationResult);
            Log.d(TAG, locationResult.getLastLocation().getLatitude() + ", " + locationResult.getLastLocation().getLongitude());

            if(haveSelectedForm){
                Log.d(TAG, "We are going to form");
                Bundle formArgs = new Bundle();
                formArgs.putDouble("latitude", locationResult.getLastLocation().getLatitude());
                formArgs.putDouble("longitude", locationResult.getLastLocation().getLongitude());

                if(flowerType != null){
                    formArgs.putString("flowerName", flowerType);
                }

                FragmentDescriptionForm descriptionForm = new FragmentDescriptionForm();
                descriptionForm.setArguments(formArgs);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, descriptionForm)
                        .commit();
            }
        }
    }

}
