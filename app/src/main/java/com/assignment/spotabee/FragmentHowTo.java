package com.assignment.spotabee;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.database.Description;

import java.util.List;

/**
 * Created by Ryun on 3/18/2018.
 */

public class FragmentHowTo extends Fragment {

    private String TAG = "How To Debug";
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        db = AppDatabase.getAppDatabase(getContext());
        List<Description> allDescriptions = db.descriptionDao()
                .getAllDescriptions();

        for(Description item:allDescriptions){
            if (item.getFlowerType() != null) {
                Log.v(TAG, item.getFlowerType());
            } else {
                Log.v(TAG, "This entry does not have a flower type");
            }
        }
        return inflater.inflate(R.layout.fragment_menu_howto, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("How To");
    }
}
