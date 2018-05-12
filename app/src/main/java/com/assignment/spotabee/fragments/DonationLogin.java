package com.assignment.spotabee.fragments;

// Tutorial for PayPal Button: https://developer
// .paypal.com/docs/classic/mobile/ht_mpl-itemPayment-Android/

/**
 * Made by: C1769948 and
 */
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assignment.spotabee.Config.Config;
import com.assignment.spotabee.KeyChain;
import com.assignment.spotabee.R;

import android.content.Intent;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import java.math.BigDecimal;

//import static com.assignment.spotabee.Config.Config.PAYPAL_REQUEST_CODE;

/**
 * Donation Login fragment. Controls all content that goes
 * on the 'Donation' page.
 */
public class DonationLogin extends Fragment
        implements View.OnClickListener {

    /**
     * Fragment view. Used to get resources.
     */
    private View rootView;

    /**
     * TAG used in Log statements that can narrow down where the message
     * or error is coming from.
     */
    private static final String TAG = "DonationLoginDebug";

    /**
     * ID for PayPal button created dynamically.
     */
    private static final int PAYPAL_BUTTON_ID = 1;

    /**
     * Boolean that states whether or not the library
     * has been initialized.
     */
    private boolean paypalLibraryInitialize;

    /**
     * An editable text box that is the amount of money
     * for donation.
     */
    private EditText editAmount;

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

        rootView = inflater.inflate(R.layout.fragment_donation,
                container, false);

        //Start Paypal Service
        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);
        paypalLibraryInitialize = false;
        editAmount = rootView.findViewById(R.id.editAmount);
//        btnPayNow = (Button) rootView.findViewById(R.id.btnPayNow);

//        btnPayNow.setOnClickListener(new View.OnClickListener()
//
//        {
//            //This will set the payment process up once the payNow g
//            //Button is
//            public void onClick(View view) {
//                processPayment();
//            }
//        });

        initLibrary();
        showPayPalButton();
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
        getActivity().setTitle(getString(R.string.donate));
    }

    /**
     * The PayPal configuration data. Currently, it is set to only
     * be in a sandbox, or a closed environment.
     */
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            //Using Sandbox as it is a mock payment
            .clientId(KeyChain.getPaypalClientId());

    /**
     * On button click, starts the transaction.
     *
     * @param v The current view on the fragment.
     */
    @Override
    public void onClick(final View v) {
        if (v.getId() == PAYPAL_BUTTON_ID) {
            processPayment();
        }
    }

    /**
     * Gets the inputted dollar amount, and starts the
     * transaction.
     */
    private void processPayment() {
        String amount = editAmount.getText().toString();
        getActivity().getSharedPreferences("com.assignment.spotabee", Context.MODE_PRIVATE)
                .edit().putString("amount_payed", amount).apply();
        try {
            PayPalPayment payPalPayment = new PayPalPayment(
                    new BigDecimal(String.valueOf(amount)),
                    "USD", "Donate for Spot a Bee",
                    PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
            getActivity().startActivityForResult(intent, KeyChain.getPaypalRequestCode());
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(),
                    R.string.error_toast_message_pay_pal,
                    Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Shows the PayPal button when called.
     */
    private void showPayPalButton() {
        PayPal pp = PayPal.getInstance();
        CheckoutButton launchPayPalButton = pp.getCheckoutButton(getActivity(),
                PayPal.BUTTON_278x43, CheckoutButton.TEXT_PAY);
        launchPayPalButton.setOnClickListener(this);
        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.bottomMargin = 10;
        launchPayPalButton.setLayoutParams(params);
        launchPayPalButton.setId(PAYPAL_BUTTON_ID);
        ((RelativeLayout) rootView.findViewById(R.id.payPalButtonContainer))
                .addView(launchPayPalButton);
//      ((RelativeLayout) rootView.findViewById(R.id.payPalButtonContainer))
//              .setGravity(Gravity.CENTER_HORIZONTAL);
    }

    /**
     * Initializes the PayPal library for further usage.
     */
    public void initLibrary() {
        PayPal pp = PayPal.getInstance();
        if (pp == null) {
            // This main initialization call takes your Context,
            // AppID, and target server
            pp = PayPal.initWithAppID(getActivity(),
                    KeyChain.getPaypalClientId(), PayPal.ENV_NONE);
            pp.setLanguage("en_US");

            // Sets who pays any transaction fees. Value is:
            // FEEPAYER_SENDER, FEEPAYER_PRIMARYRECEIVER,
            // FEEPAYER_EACHRECEIVER, and FEEPAYER_SECONDARYONLY
            pp.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER);

            // true = transaction requires shipping
            pp.setShippingEnabled(true);
            paypalLibraryInitialize = true;
        }
    }
}
