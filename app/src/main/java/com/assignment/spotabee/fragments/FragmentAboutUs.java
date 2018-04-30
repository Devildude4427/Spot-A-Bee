package com.assignment.spotabee.fragments;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assignment.spotabee.R;

/**
 * About Us fragment. Controls all content that goes
 * on the 'About Us' page.
 */
public class FragmentAboutUs extends Fragment {

    /**
     * URL for the client's YouTube video that
     * describes their mission.
     */
    private static final String videoUrl
            = "https://www.youtube.com/watch?v=pbeSHt4B3hg";

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
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootView = inflater.inflate(R.layout.fragment_menu_aboutus,
                container, false);

        AppCompatButton goToYouTube = rootView.findViewById(R.id.goToYoutube);
        goToYouTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Uri videoUri =  Uri.parse(videoUrl);
                Intent youtubeVideo = new Intent(Intent.ACTION_VIEW);
                youtubeVideo.setData(videoUri);
                startActivity(youtubeVideo);
            }
        });
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
        getActivity().setTitle("About Us");
    }
}
