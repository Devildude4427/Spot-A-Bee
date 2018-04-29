package com.assignment.spotabee.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.assignment.spotabee.R;

import org.json.JSONException;
import org.json.JSONObject;


public class PaymentDetails extends Fragment {
    private View rootView;
    private AppCompatTextView txtId, txtAmount, txtStatus;


    public PaymentDetails() {
        // Required empty public constructor
    }

    public static PaymentDetails newInstance(String param1, String param2) {
        PaymentDetails fragment = new PaymentDetails();
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
        rootView =  inflater.inflate(R.layout.fragment_payment_info, container, false);
//        txtId = (TextView) rootView.findViewById(R.id.txtId);
        txtAmount =  rootView.findViewById(R.id.txtAmount);
        txtStatus =  rootView.findViewById(R.id.txtStatus);


        //This will get the Intent related
        Intent intent = getActivity().getIntent();

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private void showDetails(JSONObject response, String paymentAmount){
        try {
            txtId.setText(response.getString("id"));
            txtStatus.setText(response.getString("state"));
            txtAmount.setText("$"+ paymentAmount);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
