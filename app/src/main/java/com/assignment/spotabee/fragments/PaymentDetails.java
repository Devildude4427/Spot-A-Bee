package com.assignment.spotabee.fragments;

/**
 * Made by: C1769948 and C1452589
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assignment.spotabee.R;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Payment Details fragment. Controls all content that goes
 * on the 'Payment Info' page.
 */
public class PaymentDetails extends Fragment {

    /**
     * Text Views for the page.
     */
    private AppCompatTextView txtId, txtAmount, txtStatus;

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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_payment_info,
                container, false);
//        txtId = (TextView) rootView.findViewById(R.id.txtId);
        txtAmount =  rootView.findViewById(R.id.txtAmount);
        txtStatus =  rootView.findViewById(R.id.txtStatus);
        //This will get the Intent related
        Intent intent = getActivity().getIntent();
        try {
            JSONObject jsonObject = new JSONObject(
                    intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"),
                    intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    /**
     * Show details from the site response.
     *
     * @param response Response from the site.
     * @param paymentAmount Amount being donated.
     */
    private void showDetails(final JSONObject response,
                             final String paymentAmount) {
        try {
            txtId.setText(response.getString("id"));
            txtStatus.setText(response.getString("state"));
            txtAmount.setText("$" + paymentAmount);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
