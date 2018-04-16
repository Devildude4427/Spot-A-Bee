package com.assignment.spotabee;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

    private static final String CHANNEL_ID = "One";
    private static final String TAG = "Debug";
    private GoogleMap googleMap;
    private NotificationManager notificationManager;
    private LocationListener locationListener;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Log.v(TAG, "Check 1");
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Log.v(TAG, "Check 2");




        initChannel();

        /*Builder for notifications*/
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.camera_button)
                .setContentTitle("Spot-A-Bee")
                .setContentText("This is a notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        /*Way to call a notification*/
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification
        notificationManager.notify(1, mBuilder.build());
    }

    /**
     * Creates the Channel for the notification to be used
     */
    public void initChannel() {
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

    /**
     * Creates the map that is going to be used
     *
     * @param googleMap GoogleMap object
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        try {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                Log.v(TAG, "Check 3");
                locationListener = new Map.MyLocationListener();
                locationManager.requestLocationUpdates(LocationManager
                        .GPS_PROVIDER, 5000, 10, locationListener);

            } else {
                setUpMap(55.481581, -3.179090);
                Log.v(TAG, "Running setUpMap with default coords");
            }
        } catch (Exception e) {
            Log.v(TAG, "Exception " + e);
        }
    }

    /*Method to create a marker at positions*/
    public void setUpMap(Double Latitude, Double Longitude) {
        LatLng newMarker = new LatLng(Latitude, Longitude);
        googleMap.addMarker(new MarkerOptions().position(newMarker)
                .title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(newMarker));
    }

    //Listener class to get longitude and latitude
    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            Toast.makeText(getBaseContext(),"Location changed : Lat: "
                            + loc.getLatitude() + " Lng: " + loc.getLongitude(),
                    Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(TAG, latitude);

            setUpMap(loc.getLatitude(), loc.getLongitude());
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
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }


}
