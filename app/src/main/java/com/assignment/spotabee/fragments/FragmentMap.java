package com.assignment.spotabee.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assignment.spotabee.R;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.database.Description;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.LocationListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Map fragment. Creates and controls all data
 * that goes into creating our map screen.
 */
public class FragmentMap extends Fragment
        implements OnMapReadyCallback {

    /**
     * TAG used in Log statements that can narrow down where the message
     * or error is coming from.
     */
    private static final String TAG = "MapDebug";

    /**
     * Gets all description rows from database.
     */
    private List<Description> allDescriptions;

    /**
     * Arraylist of all markers currently plotted.
     */
    private ArrayList<LatLng> allMarkers = new ArrayList<>();

    /**
     * Google map object that is used to create the background.
     */
    private GoogleMap googleMap;

    /**
     * MapView android item. Where the Google map is displayed.
     */
    private MapView mapView;

    /**
     * Manager for the location listener, handles it's outputs.
     */
    private LocationManager locationManager;

    /**
     * Checks to see if the device has moved to new locations.
     */
    private LocationListener locationListener;

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
    public View onCreateView(final @NonNull LayoutInflater inflater,
                             final  @Nullable ViewGroup container,
                             final  @Nullable Bundle savedInstanceState) {
        //returning our layout file
        View rootView = inflater.inflate(R.layout.fragment_menu_map_outdated,
                container, false);

        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        allDescriptions = db.databasenDao()
                .getAllDescriptions();

        locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new MyLocationListener();

        mapView = rootView.findViewById(R.id.map);
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
                    locationManager.requestLocationUpdates(LocationManager
                            .GPS_PROVIDER, 5000, 10, locationListener);
                } else {
                    googleMap.setMyLocationEnabled(false);
                }
                setMarkers(allDescriptions);
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
    public void onViewCreated(final @NonNull View view,
                              final  @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for
        // different fragments different titles
        getActivity().setTitle("Results");
    }

    /**
     * When the map is ready, start getting location data.
     *
     * @param googleMap A Google map object.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        try {
            if (ContextCompat.checkSelfPermission(getActivity()
                            .getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
            }
        } catch (Exception e) {
            Log.v(TAG, "Exception " + e);
        }

    }

    /**
     * Resize the bee image to make it into a suitable
     * replacement for the default map marker.
     *
     * @return The new image.
     */
    public BitmapDescriptor resizeMapIcons() {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier("bee",
                        "drawable", getActivity().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap,
                70, 90,  false);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
    }

    /**
     *  Creates map markers using all latlng coords from
     *  the database.
     *
     * @param descriptions List of all rows from the database.
     */
    public void setMarkers(final List<Description> descriptions) {
        if (descriptions.isEmpty()) {
                Log.d(TAG, "Description ArrayList is empty");
                return;
        }

        try {
            BitmapDescriptor icon = resizeMapIcons();
            for (Description location:descriptions) {
                LatLng newMarker = new LatLng(location.getLatitude(),
                        location.getLongitude());

                    googleMap.addMarker(new MarkerOptions()
                            .position(newMarker)
                            .icon(icon));

                allMarkers.add(newMarker);
            }
            // Builder calculates the area of the fragment_map_outdated
            // it needs to show on screen to include all markers
            LatLngBounds.Builder bld = new LatLngBounds.Builder();
            for (int i = 0; i < allMarkers.size(); i++) {
                LatLng ll = allMarkers.get(i);
                bld.include(ll);
            }
            LatLngBounds bounds = bld.build();
            googleMap.moveCamera(CameraUpdateFactory
                    .newLatLngBounds(bounds, 120));
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
            Toast.makeText(getContext(), "Location changed : Lat: "
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
}
