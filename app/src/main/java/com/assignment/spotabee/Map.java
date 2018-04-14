package com.assignment.spotabee;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity
        implements OnMapReadyCallback {

//    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS = 0;
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1;
//    private static final int PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS = 2;
//    private static final int CHOOSE_ACCOUNT = 99;
    private static final String CHANNEL_ID = "One";
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private AccountManager accountManager;
    private NotificationManager notificationManager;

    private static final String TAG = "Debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        initChannels();

//        accountManager = (AccountManager)
//                getSystemService(Context.ACCOUNT_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.camera_button)
                .setContentTitle("Spot-A-Bee")
                .setContentText("This is a notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

    // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, mBuilder.build());
    }

    public void initChannels() {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(getString(R.string.channel_description));
        notificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        checkIfPermissionsGiven();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkIfPermissionsGiven() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
//                        Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
//            requestLocationAccountPermission(); }
           if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission(); }
//        else {
//            requestAccountPermission();
//        }

        try {
            locationListener = new Map.MyLocationListener();
            locationManager.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 5000, 10,locationListener);
//            Intent intent = accountManager.newChooseAccountIntent(null, null, new String[]{"com.google"}, null, null, null,
//                    null);
//            startActivityForResult(intent, CHOOSE_ACCOUNT);
        } catch (Exception e) {
            Log.d(TAG, "Exception" + e);
        }
    }

//    protected void onActivityResult(final int requestCode, final int resultCode,
//                                    final Intent data){
//        if (requestCode == CHOOSE_ACCOUNT && resultCode == RESULT_OK) {
//            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
//            Log.v(TAG, accountName);
//        } else {
//            Log.v(TAG, "There was an error in the account picker");
//        }
//    }

//    private void requestLocationAccountPermission() {
//        // Permission has not been granted and must be requested.
//        // Request the permission. The result will be received in onRequestPermissionResult().
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.GET_ACCOUNTS},
//                PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS);
//    }

    private void requestLocationPermission() {
        // Permission has not been granted and must be requested.
        // Request the permission. The result will be received in onRequestPermissionResult().
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
    }

//    private void requestAccountPermission() {
//        // Permission has not been granted and must be requested.
//        // Request the permission. The result will be received in onRequestPermissionResult().
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.GET_ACCOUNTS},
//                PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS);
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
//            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    checkIfPermissionsGiven();
//                } else {
//                    setUpMap(51.481581, -3.179090);
//                    Log.v(TAG, "User needs to make an account");
//                }
//                return;
//            }
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    setUpMap(51.481581, -3.179090);
                }
                return;
            }
//            case PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                } else {
//                    //Redirect to force user to create an account
//                    Log.v(TAG, "User needs to make an account");
//                }
//            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void setUpMap(Double Latitude, Double Longitude) {
        LatLng newMarker = new LatLng(Latitude, Longitude);
        googleMap.addMarker(new MarkerOptions().position(newMarker)
                .title("Marker"));
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

            setUpMap(loc.getLatitude(), loc.getLongitude());
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
