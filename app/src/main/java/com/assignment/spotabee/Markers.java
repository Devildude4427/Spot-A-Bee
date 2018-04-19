package com.assignment.spotabee;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.database.Description;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.LocationListener;
import android.location.LocationManager;

import java.util.ArrayList;
import java.util.List;

public class Markers extends Fragment
        implements OnMapReadyCallback {

    private static final String TAG = "markers_debug";
    private AppDatabase db;
    private List<Description> descriptions;
    private ArrayList<LatLng> allMarkers = new ArrayList<>();
    private GoogleMap googleMap;
    private MapView mapView;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        View rootView = inflater.inflate(R.layout.fragment_menu_map, container, false);

        locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        Map.MyLocationListener listener = new Map().new MyLocationListener();
        locationListener = listener;

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
                    googleMap.setMyLocationEnabled(true);
                    locationManager.requestLocationUpdates(LocationManager
                            .GPS_PROVIDER, 5000, 10, locationListener);
                } else {
                    googleMap.setMyLocationEnabled(false);
                }
                setMarkers(descriptions);
            }
        });

        db = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                AppDatabase.class,
                "App Database"
        ).fallbackToDestructiveMigration().build();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                descriptions = db.descriptionDao().getAllDescriptions();
                descriptions.add(new Description(52.4816, -4.17909));
                descriptions.add(new Description(50.4816, -3.57909));
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Map");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        try {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);

//                locationListener = new Map.MyLocationListener();
//                locationManager.requestLocationUpdates(LocationManager
//                        .GPS_PROVIDER, 5000, 10, locationListener);

            } else {
                googleMap.setMyLocationEnabled(false);
            }
        } catch (Exception e) {
            Log.v(TAG, "Exception " + e);
        }

    }

    public void setMarkers(List<Description> descriptions) {
        if(descriptions.isEmpty()){
                Log.d(TAG, "Description ArrayList is empty");
                return;
        }

        try {
            for (Description location:descriptions){
                LatLng newMarker = new LatLng(location.getLatitude(), location.getLongitude());
                Log.v(TAG, "Marker" + newMarker);
                googleMap.addMarker(new MarkerOptions()
                        .position(newMarker));

                allMarkers.add(newMarker);
            }

            // Builder calculates the area of the map it needs to show on screen
            // to include all markers
            LatLngBounds.Builder bld = new LatLngBounds.Builder();
            for (int i = 0; i < allMarkers.size(); i++) {
                LatLng ll = allMarkers.get(i);
                bld.include(ll);
            }
            LatLngBounds bounds = bld.build();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,120));
        } catch (Exception e){
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
}
