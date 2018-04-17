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
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Map extends Fragment
        implements OnMapReadyCallback {

    private static final String CHANNEL_ID = "One";
    private static final String TAG = "Debug";
    private GoogleMap googleMap;
    private NotificationManager notificationManager;
    private LocationListener locationListener;
    private LocationManager locationManager;
    MapView mapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootView = inflater.inflate(R.layout.fragment_menu_map, container, false);

        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    // For showing a move to my location button
                    googleMap.setMyLocationEnabled(true);
                } else {
                    googleMap.setMyLocationEnabled(false);
                }
            }
        });

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Map");

        locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        initChannel();

        /*Builder for notifications*/
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity().getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.camera_button)
                .setContentTitle("Spot-A-Bee")
                .setContentText("This is a notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        /*Way to call a notification*/
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity().getApplicationContext());

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
                getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
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
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
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

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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
//            Toast.makeText(getBaseContext(),"Location changed : Lat: "
//                            + loc.getLatitude() + " Lng: " + loc.getLongitude(),
//                    Toast.LENGTH_SHORT).show();
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
