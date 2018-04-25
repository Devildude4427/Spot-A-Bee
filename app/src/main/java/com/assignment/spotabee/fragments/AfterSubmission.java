package com.assignment.spotabee.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assignment.spotabee.R;


public class AfterSubmission extends Fragment {
    private AppCompatTextView thankYouMessage;
    private AppCompatButton goHome;

    public AfterSubmission() {
        // Required empty public constructor
    }
    public static AfterSubmission newInstance(String param1, String param2) {
        AfterSubmission fragment = new AfterSubmission();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_after_submission, container, false);
    }


}
