package com.assignment.spotabee;

import android.Manifest;
import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.database.Description;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Markers extends Fragment
        implements OnMapReadyCallback{



    //To test this, go to "MainActivity" and look for method "displaySelectedScreen"
    //Swap out "fragment = new Map();" for "fragment = new Markers();
    





    private static final String TAG = "markers_debug";
    private AppDatabase db;
    private List<Double> latitudes;
    private List<Double> longitudes;
    private List<Description> descriptions;
    private HashMap<String, LatLng> coOrdinates;
    private boolean mapIsReady;
    private GoogleMap googleMap;
    MapView mapView;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootView = inflater.inflate(R.layout.fragment_menu_map, container, false);

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

        this.coOrdinates = new HashMap<>();
        this.coOrdinates.put("Newport", new LatLng(51.5842, 2.9977));
        this.coOrdinates.put("Cardiff", new LatLng(51.4816, 3.1791));
        mapIsReady = false;

        db = Room.databaseBuilder(
                getActivity(),
                AppDatabase.class,
                "App Database"
        ).fallbackToDestructiveMigration().build();

        Log.d(TAG, "initialise starting");
        initialise();
        Log.d(TAG, "initialise finished");

        return rootView;
    }

    public void initialise(){

//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                descriptions = db.descriptionDao()
//                        .getAllDescriptions();
//
//                for (int i = 0; i < descriptions.size(); i++){
//                    Description currentDescription = descriptions.get(i);
//                    coOrdinates.put(currentDescription.getLocation(),
//                            new LatLng(currentDescription.getLatitude(),
//                                    currentDescription.getLongitude()));
//                }
//            }
//        });
//
        for(String key : coOrdinates.keySet()){
            Log.d(TAG, "IN CO-ORDINATES:" + coOrdinates.get(key).toString());
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        mapIsReady = true;
        initialise();

        readyMapLayout();
    }


public void setMarkers(int width, int height) {


    if(coOrdinates.isEmpty()){
            Log.d(TAG, "setMarkers: co-ordinates HashMap is empty.");
            return;
        }
// a comment to push
        final ArrayList<LatLng> arrayListLatLang = new ArrayList<>();
        for (String title : coOrdinates.keySet()){
            googleMap.addMarker(new MarkerOptions()
                    .position(coOrdinates.get(title))
                    .title(title));

            arrayListLatLang.add(coOrdinates.get(title));
        }

        LatLngBounds.Builder bld = new LatLngBounds.Builder();
        for (int i = 0; i < arrayListLatLang.size(); i++) {
            LatLng ll = arrayListLatLang.get(i);
            bld.include(ll);
        }
        LatLngBounds bounds = bld.build();
    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 70));
    }


    // Adapted from: https://stackoverflow.com/questions/7733813/how-can-you-tell-when-a-layout-has-been-drawn?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    public void readyMapLayout(){
        final View mapFragment = rootView.findViewById(R.id.map);
        ViewTreeObserver vto = mapFragment.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mapFragment.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width  = mapFragment.getMeasuredWidth();
                int height = mapFragment.getMeasuredHeight();
                setMarkers(width, height);

            }
        });
    }
}
