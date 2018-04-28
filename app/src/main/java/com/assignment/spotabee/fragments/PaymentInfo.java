package com.assignment.spotabee.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.assignment.spotabee.AmountPayed;
import com.assignment.spotabee.R;


public class PaymentInfo extends Fragment {
    private View rootView;
    private String amount;
    private String paymentDetails;
    private TextView txtHeading, txtId, txtStatus, txtAmount;
    private Button btnHome;
    private static final String TAG = "PaymentInfo Debug";


    public PaymentInfo() {
        // Required empty public constructor
    }


    public static PaymentInfo newInstance(String param1, String param2) {
        PaymentInfo fragment = new PaymentInfo();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paymentDetails = getArguments().getString("paymentInfo");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        amount = AmountPayed.getAmountPayed();
        rootView = inflater.inflate(R.layout.fragment_payment_info, container, false);
        txtAmount = rootView.findViewById(R.id.txtAmount);
        txtStatus = rootView.findViewById(R.id.txtStatus);
        setUpScreen();
        return rootView;
    }

    private void setUpScreen(){
        if(amount != null || paymentDetails != null){
            txtAmount.setText(amount);
            txtStatus.setText(paymentDetails);
            Log.d(TAG, paymentDetails);
            Log.d(TAG, amount);
        } else {
            Log.d(TAG, "DETAILS ARE NULL");
        }


    }

}
