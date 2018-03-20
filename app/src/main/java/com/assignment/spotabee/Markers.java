package com.assignment.spotabee;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class Markers extends FragmentActivity
        implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 0;
    private static final int PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS = 1;
    private GoogleMap googleMap;
    private LocationManager locationManager =null;
    private LocationListener locationListener=null;
    private AccountManager accountManager;

    private static final String TAG = "Debug";
    private static HashMap<String, LatLng> coOrdinatesDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        accountManager = (AccountManager)
                getSystemService(Context.ACCOUNT_SERVICE);

        coOrdinatesDemo = new HashMap<>();

        coOrdinatesDemo.put("Bute Park", new LatLng(51.4846, 3.1853));
        coOrdinatesDemo.put("Roath Park", new LatLng(51.5055, 3.1749));
        coOrdinatesDemo.put("Whitchurch", new LatLng(51.5154, 3.2264));
        coOrdinatesDemo.put("Cardiff Bay", new LatLng(51.4462, 3.1661));
        coOrdinatesDemo.put("St Davids Shopping Center", new LatLng(51.14792, 3.1748));
    }

    public void checkIfPermissionsGiven() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationListener = new Markers.MyLocationListener();
            locationManager.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 5000, 10,locationListener);
        } else {
            requestLocationPermission();
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS)
                == PackageManager.PERMISSION_GRANTED) {

            Account[] list = accountManager.getAccountsByType("com.google");
            Log.v(TAG, "Yes, this does still work");
            if (list.length < 1){
                Log.v(TAG, "Accounts are empty");
            } else {
                for (Account account :list){
                    Log.v(TAG, account.name);
                }
            }
        } else {
            requestAccountPermission();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        checkIfPermissionsGiven();
    }

    private void requestLocationPermission() {
        // Permission has not been granted and must be requested.
        // Request the permission. The result will be received in onRequestPermissionResult().
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
    }

    private void requestAccountPermission() {
        // Permission has not been granted and must be requested.
        // Request the permission. The result will be received in onRequestPermissionResult().
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.GET_ACCOUNTS},
                PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    locationListener = new Markers.MyLocationListener();
                    locationManager.requestLocationUpdates(LocationManager
                            .GPS_PROVIDER, 5000, 10,locationListener);
                    checkIfPermissionsGiven();
                } else {
                    setUpMap(51.481581, -3.179090);
                    checkIfPermissionsGiven();
                }
                return;
            }
            case PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Account[] list = accountManager.get(this).getAccountsByType("com.google");
                    Log.v(TAG, "Yes, this does still work");
                    for (Account account :list){
                        Log.v(TAG, account.name);
                    }
                } else {
                    //Redirect to force user to create an account
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void setUpMap(Double Latitude, Double Longitude) {
        LatLng newMarker = new LatLng(Latitude, Longitude);
        googleMap.addMarker(new MarkerOptions().position(newMarker)
                .title("Maker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(newMarker));

    }

    //Listener class to get longitude and latitude
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            Toast.makeText(getBaseContext(),"Location changed : Lat: " +
                            loc.getLatitude()+ " Lng: " + loc.getLongitude(),
                    Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " +loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " +loc.getLatitude();
            Log.v(TAG, latitude);

            //setUpMap(loc.getLatitude(), loc.getLongitude());
            setMarkers(coOrdinatesDemo);
        }

        public void setMarkers(HashMap<String, LatLng> coOrdinates) {
            String demoKey = "";

            if(coOrdinates.isEmpty()){
                Log.d(TAG, "setMarkers: co-ordinates HashMap is empty.");
                return;
            }

            final ArrayList<LatLng> arrayListLatLang = new ArrayList<>();
            for (String title : coOrdinates.keySet()){
                googleMap.addMarker(new MarkerOptions()
                        .position(coOrdinates.get(title))
                        .title(title));

                demoKey = title;
                arrayListLatLang.add(coOrdinates.get(title));
            }

            LatLngBounds.Builder bld = new LatLngBounds.Builder();
            for (int i = 0; i < arrayListLatLang.size(); i++) {
                LatLng ll = arrayListLatLang.get(i);
                bld.include(ll);
            }
            LatLngBounds bounds = bld.build();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }


}
