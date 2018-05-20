package com.assignment.spotabee;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.assignment.spotabee.fragments.DonationLogin;
import com.assignment.spotabee.fragments.PaymentInfo;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import okhttp3.internal.Util;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.uiautomator.Until.findObject;
import static com.assignment.spotabee.TestHelpers.childAtPosition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class GoToPayPalTest {
    private final String TAG = "PayPalTest";
    private final String donationAmount = "17";
    private final String payPalAccountEmail = "zoilagarman3@gmail.com";
    private final String payPalAccountPasswrd = "12345abcde";

    private SharedPreferences sp;


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void gotoDonationLogin(){

        // Navigate to Donation fragment
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        7),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        // Enter donation amount
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editAmount),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText(donationAmount), closeSoftKeyboard());

    }

    @Test
    public void entirePayPalJourneyTest() {

        final UiDevice mDevice =
                UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        final int timeOut = 1000 * 60;


        // Go to PayPal
        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.payPalButtonContainer),
                                0),
                        1),
                        isDisplayed()));
        linearLayout.perform(click());


        // Make sure we are in PayPal's web view
        mDevice.wait(findObject(By.clazz(WebView.class)), timeOut);


        try {
            // Choose Pay with PayPal rather than card
            UiObject payWithPayPal = mDevice.findObject(new UiSelector()
                    .instance(0)
                    .className(Button.class));

            // Go to PayPal login
            payWithPayPal.clickAndWaitForNewWindow();


            // Login email
            UiObject email = mDevice.findObject(new UiSelector()
                    .instance(0)
                    .className(EditText.class));


            email.setText(payPalAccountEmail);

            // Login password
            UiObject password = mDevice.findObject(new UiSelector()
                    .instance(1)
                    .className(EditText.class));

            password.setText(payPalAccountPasswrd);

            // Click to login
            UiObject buttonLogin = mDevice.findObject(new UiSelector()
                    .instance(0)
                    .className(Button.class));


            buttonLogin.clickAndWaitForNewWindow();

            // Conform payment
            UiObject buttonPay = mDevice.findObject(new UiSelector()
                    .instance(0)
                    .className(Button.class));

            buttonPay.click();


            Thread.sleep(15000);


            // Test that payment details have been extracted from PayPal intent data and displayed
            // on PaymentDetails
            String textAmountFormatter = mActivityTestRule.getActivity().getString(R.string.test_donation);
            String formatted = String.format(textAmountFormatter, donationAmount);

            onView(withId(R.id.txtAmount))
                    .check(matches(isDisplayed()))
                    .check(matches(withText(formatted)));

        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

    }


    @LargeTest
    @Test
    public void handleBackPress() {

        final UiDevice mDevice =
                UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        final int timeOut = 1000 * 60;


        // Go to PayPal
        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.payPalButtonContainer),
                                0),
                        1),
                        isDisplayed()));
        linearLayout.perform(click());


        // Make sure we are in PayPal's web view
        mDevice.wait(findObject(By.clazz(WebView.class)), timeOut);


        try {
            // Choose Pay with PayPal rather than card
            UiObject payWithPayPal = mDevice.findObject(new UiSelector()
                    .instance(0)
                    .className(Button.class));

            // Go to PayPal login
            payWithPayPal.clickAndWaitForNewWindow();


            // Login email
            UiObject email = mDevice.findObject(new UiSelector()
                    .instance(0)
                    .className(EditText.class));


            email.setText(payPalAccountEmail);

            // Login password
            UiObject password = mDevice.findObject(new UiSelector()
                    .instance(1)
                    .className(EditText.class));

            password.setText(payPalAccountPasswrd);

            // Click to login
            UiObject buttonLogin = mDevice.findObject(new UiSelector()
                    .instance(0)
                    .className(Button.class));


            buttonLogin.clickAndWaitForNewWindow();


            pressBack();
            pressBack();


            Thread.sleep(15000);


            // Test that payment details have been extracted from PayPal intent data and displayed
            // on PaymentDetails
            String textAmountFormatter = mActivityTestRule.getActivity().getString(R.string.test_donation);

            onView(withId(R.id.editAmount))
                    .check(matches(isDisplayed()))
                    .check(matches(withText(donationAmount)));

        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

    }

    @Test
    public void goesToPayPal(){
        final UiDevice mDevice =
                UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        final int timeOut = 1000 * 60;

        Intents.init();


        // Go to PayPal
        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.payPalButtonContainer),
                                0),
                        1),
                        isDisplayed()));
        linearLayout.perform(click());


        // Make sure we are in PayPal's web view
        mDevice.wait(findObject(By.clazz(WebView.class)), timeOut);

        intended(hasComponent(PaymentActivity.class.getName()));
        Intents.release();

    }

    @SmallTest
    @Test
    public void testPaymentInfoSetUp(){

        sp = mActivityTestRule.getActivity().getSharedPreferences("com.assignment.spotabee", Context.MODE_PRIVATE);
        sp.edit().putString("amount_payed", donationAmount).apply();


        String textAmountFormatter = mActivityTestRule.getActivity().getString(R.string.test_donation);
        String formatted = String.format(textAmountFormatter, donationAmount);

        onView(withId(R.id.txtAmount))
                .check(matches(isDisplayed()))
                .check(matches(withText(formatted)));
    }

}



