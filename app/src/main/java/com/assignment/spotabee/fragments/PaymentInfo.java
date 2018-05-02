package com.assignment.spotabee.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.assignment.spotabee.AmountPayed;
import com.assignment.spotabee.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Payment Info fragment. Controls all content that goes
 * on the 'Payment Info' page.
 */
public class PaymentInfo extends Fragment {

    /**
     * Completely sketchy and potentially dangerous
     * saved value of donation.
     */
    private String amount;

    /**
     * Also completely sketchy and potentially dangerous
     * saved transaction details.
     */
    private String paymentDetails;

    /**
     * Text Views for the page.
     */
    private AppCompatTextView txtId, txtStatus, txtAmount;

    /**
     * TAG used in Log statements that can narrow down where the message
     * or error is coming from.
     */
    private static final String TAG = "PaymentInfoDebug";

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
//        amount = AmountPayed.getAmountPayed();
        View rootView = inflater.inflate(
                R.layout.fragment_payment_info, container, false);

        try {
            amount = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
                    .getString("amount_payed", null);
        } catch (NullPointerException e){
            Toast.makeText(getActivity(),
                    "Error. Transaction has not been processed",
                    Toast.LENGTH_SHORT).show();

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, new FragmentHome())
                    .commit();
        }

        txtAmount = rootView.findViewById(R.id.txtAmount);
        txtStatus = rootView.findViewById(R.id.txtStatus);

        //This button will help to go back home after
        //Donation has completed
        RelativeLayout goHome = rootView.findViewById(R.id.goHome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new FragmentHome())
                        .commit();
            }
        });

        getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
                .edit()
                .putString("amount_payed", null)
                .apply();

        setUpScreen();
        return rootView;
    }

    /**
     * Once the view is created, it will
     * handle any other fragment methods.
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
    }

    /**
     * Method that rearranges the screen to thank
     * users for the donation.
     */
    private void setUpScreen() {
        if (amount != null || paymentDetails != null) {
            txtAmount.setText(String.format(
                    getString(R.string.you_have_donated_x), amount));
            txtStatus.setText((CharSequence) txtId);
        } else {
            Log.v(TAG, "DETAILS ARE NULL");
        }
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
