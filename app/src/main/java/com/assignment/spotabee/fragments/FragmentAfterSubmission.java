package com.assignment.spotabee.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.assignment.spotabee.R;
import com.assignment.spotabee.database.AppDatabase;


public class FragmentAfterSubmission extends Fragment {

    private View rootView;
    private RelativeLayout goHome;
    private AppDatabase db;
    private Context context;

    public FragmentAfterSubmission() {
    }


    public static FragmentAfterSubmission newInstance(String param1, String param2) {
        FragmentAfterSubmission fragment = new FragmentAfterSubmission();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_after_submission, container, false);
        goHome = rootView.findViewById(R.id.goHome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new FragmentHome())
                        .commit();
            }
        });
        db = AppDatabase.getAppDatabase(getContext());
        context = getActivity();
        checkForNotifications();

        return rootView;
    }

    public void checkForNotifications(){
        int currentNumOfSubmissions = db.databasenDao().getAllDescriptions().size();

        // Since there's no backend data to pull from, I've mocked this using the number of
        // submissions held in the database
        if(currentNumOfSubmissions % 10 == 0){
            Toast.makeText(getActivity(),
                    String.format(getString(R.string.incentive_every_ten_sub_message)
                            , currentNumOfSubmissions),
                    Toast.LENGTH_SHORT).show();
        }
    }

}
