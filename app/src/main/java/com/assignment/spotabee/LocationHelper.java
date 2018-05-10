package com.assignment.spotabee;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.assignment.spotabee.customutils.CheckNetworkConnection;
import com.assignment.spotabee.fragments.FragmentDescriptionForm;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;



public class LocationHelper extends android.support.v4.app.Fragment{
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private Context context;
    private LocationResult locationResult;
    private static final String TAG = "Location Helper";
    private boolean haveSelectedForm;
    private String flowerType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        this.mFusedLocationClient = new FusedLocationProviderClient(context);

        haveSelectedForm = getArguments().getBoolean("formSelected");
        flowerType = getArguments().getString("flowerName", null);

        getCurrentCoOrdinates();
    }




    /**
     * Manager for the location listener, handles it's outputs.
     */
    private LocationManager locationManager;

    /**
     * Checks to see if the device has moved to new locations.
     */
    private LocationListener locationListener;

    public void makeLocationRequest(){
        Log.d(TAG, "We are in makeLocationRequest()");
        if (ContextCompat.checkSelfPermission(context
                        .getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permissions have been granted");

            if (locationCallback == null)
                locationCallback = new UserLocationCallback();
            Log.d(TAG, "Have created location callback");
            if(CheckNetworkConnection.isInternetAvailable(getActivity())){
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


    /**
     * Listener class to get lat and lng of the device.
     */
    public class MyLocationListener implements LocationListener {

        /**
         * Creates a toast when the user's device
         * sends a coordinates.
         *
         * @param loc Current location of the device.
         */
        @Override
        public void onLocationChanged(final Location loc) {
            Toast.makeText(context, "Location changed : Lat: "
                            + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(final String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(final String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(final String provider,
                                    final int status, final Bundle extras) {
            // TODO Auto-generated method stub
        }
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
