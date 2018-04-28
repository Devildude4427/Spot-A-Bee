package com.assignment.spotabee.fragments;

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

import org.json.JSONException;
import org.json.JSONObject;


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

        //This button will help to go back home after
        //Donation has completed
        btnHome = rootView.findViewById(R.id.btnHome);
        setUpScreen();
        return rootView;
    }

    //This method will manage the btnHome after the transaction to easily
    //give the choise to the user to comback home after the donation



    private void setUpScreen(){
        if(amount != null || paymentDetails != null){
            txtAmount.setText("You have donated " +"\n"+ "$:" +amount+ "\n" + "To SPOT A BEE");
            txtStatus.setText((CharSequence) txtId);
            Log.d(TAG, paymentDetails);
            Log.d(TAG, amount);
        } else {
            Log.d(TAG, "DETAILS ARE NULL");
        }


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
