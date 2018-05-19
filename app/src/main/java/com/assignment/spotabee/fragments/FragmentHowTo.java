package com.assignment.spotabee.fragments;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.assignment.spotabee.R;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.database.Description;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * How To fragment. Controls all content that goes
 * on the 'How To' page.
 */
public class FragmentHowTo extends Fragment {
    private ImageView goodFlower;
    private ImageView badFlower;

    private View rootView;

    /**
     * TAG used in Log statements that can narrow down where the message
     * or error is coming from.
     */
    private static final String TAG = "HowToDebug";

    /**
     * On create of the fragment, loads the layout
     * of the page.
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
                             final @Nullable ViewGroup container,
                             final @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_menu_howto, container, false);
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        goodFlower = rootView.findViewById(R.id.good_flower);
        badFlower = rootView.findViewById(R.id.bad_flower);

        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        List<Description> allDescriptions = db.databaseDao()
                .getAllDescriptions();



        Picasso.with(getActivity()).load(R.drawable.buddleja_high_res)
                .resize(576, 346)
                .into(goodFlower);


        goodFlower.setContentDescription(R.drawable.buddleja_high_res + "");

        Picasso.with(getActivity()).load(R.drawable.bad_buddleja)
                .resize(570, 380)
                .into(badFlower);

        badFlower.setContentDescription(R.drawable.bad_buddleja + "");


        for (Description item:allDescriptions) {
            if (item.getFlowerType() != null) {
                Log.v(TAG, item.getFlowerType());
            } else {
                Log.v(TAG, "This entry does not have a flower type");
            }
        }

        return rootView;
    }

    /**
     * Once the view is created, it sets the title
     * and will handle any other fragment methods.
     *
     * @param view The view return from 'onCreateView'
     * @param savedInstanceState What instance state the
     *                           fragment is currently on.
     */
    @Override
    public void onViewCreated(final @NonNull View view,
                              final @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here
        // for different fragments different titles
        getActivity().setTitle("How To");
    }
}
