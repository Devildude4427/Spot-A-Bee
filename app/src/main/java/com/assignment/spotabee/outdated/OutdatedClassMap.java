package com.assignment.spotabee.outdated;


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
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assignment.spotabee.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Outdated class that handles the creation and
 * manipulation of the map. No longer used, but
 * still exists so that it can be referred back
 * to if needed. Also semi-used as a testing environment.
 */
public class OutdatedClassMap extends Fragment
        implements OnMapReadyCallback {

    /**
     * Channel that an example notification can use.
     */
    private static final String CHANNEL_ID = "One";

    /**
     * TAG used in Log statements that can narrow down where the message
     * or error is coming from.
     */
    private static final String TAG = "OutdatedClassMapDebug";

    /**
     * Google map object that is used to create the background.
     */
    private GoogleMap googleMap;

    /**
     * Manager for the location listener, handles it's outputs.
     */
    private LocationManager locationManager;

    /**
     * MapView android item. Where the Google map is displayed.
     */
    private MapView mapView;

    /**
     * On create of the fragment, load the layout and
     * initialize the map.
     *
     * @param inflater Creates the layout for the fragment.
     * @param container Assigns the overall container
     *                  that the fragment sits in.
     * @param savedInstanceState Save the state so that the
     *                           fragment can be opened and
     *                           shut without losing your
     *                           changes.
     * @return The finished view for the fragment.
     */
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootView = inflater.inflate(R.layout.fragment_menu_map_outdated,
                container, false);

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
            public void onMapReady(final GoogleMap mMap) {
                googleMap = mMap;
                if (ContextCompat.checkSelfPermission(getActivity()
                                .getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                } else {
                    googleMap.setMyLocationEnabled(false);
                }
            }
        });

        return rootView;
    }

    /**
     * Once the view is created, start the location
     * services and create the notification system.
     *
     * @param view The view return from 'onCreateView'
     * @param savedInstanceState What instance state the
     *                           fragment is currently on.
     */
    @Override
    public void onViewCreated(final View view,
                              final @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for
        // different fragments different titles
        getActivity().setTitle("OutdatedClassMap");

        locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        initChannel();

        /*Builder for notifications*/
        NotificationCompat.Builder mBuilder
                = new NotificationCompat.Builder(getActivity()
                .getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.camera_button)
                .setContentTitle("Spot-A-Bee")
                .setContentText("This is a notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        /*Way to call a notification*/
        NotificationManagerCompat notificationManager
                = NotificationManagerCompat.from(getActivity()
                .getApplicationContext());

        // notificationId is a unique int for each notification
        notificationManager.notify(1, mBuilder.build());
    }

    /**
     * Creates the Channel for the notification to be used.
     */
    public void initChannel() {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager = (NotificationManager)
                getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(getString(R.string.channel_description));
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * Creates the map that is going to be used.
     *
     * @param googleMap GoogleMap object
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        try {
            if (ContextCompat.checkSelfPermission(getActivity()
                            .getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                LocationListener locationListener
                        = new OutdatedClassMap.MyLocationListener();
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

    /**
     * Handles the resuming of the fragment.
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * Handles the pausing of the fragment.
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * Handles the destruction of the fragment.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * Handles the fragment when the device
     * is running low on memory.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * Method to create a marker at specific lat and lng
     * coordinates.
     *
     * @param latitude Double representing latitude of the coord.
     * @param longitude Double representing longitude of the coord.
     */
    public void setUpMap(final Double latitude, final Double longitude) {
        LatLng newMarker = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(newMarker)
                .title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(newMarker));
    }

    /**
     * Listener class to get lat and lng of the device.
     */
    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(final Location loc) {
//            Toast.makeText(getBaseContext(),"Location changed : Lat: "
//                            + loc.getLatitude() + " Lng:
//                            + loc.getLongitude(),
//                    Toast.LENGTH_SHORT).show();
//            String longitude = "Longitude: " + loc.getLongitude();
//            Log.v(TAG, longitude);
//            String latitude = "Latitude: " + loc.getLatitude();
//            Log.v(TAG, latitude);
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


}
