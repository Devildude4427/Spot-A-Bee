package com.assignment.spotabee;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
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
import com.assignment.spotabee.fragments.FragmentHome;
import com.assignment.spotabee.fragments.PaymentInfo;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
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
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
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
    private final String invalidData = "invalid data";

    private SharedPreferences sp;


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void gotoDonationLogin(){

        mActivityTestRule.launchActivity(new Intent());

        // Navigate to Donation fragment
        mActivityTestRule.getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new DonationLogin())
                .commit();

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

    @Test
    public void refusalOfInvalidDonationAmount(){

        try {
            Intents.init();

            // Enter donation amount
            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.editAmount),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.RelativeLayout")),
                                            2),
                                    0),
                            isDisplayed()));
            appCompatEditText.perform(replaceText(invalidData), closeSoftKeyboard());


            // Attempt PayPal launch
            ViewInteraction linearLayout = onView(
                    allOf(childAtPosition(
                            childAtPosition(
                                    withId(R.id.payPalButtonContainer),
                                    0),
                            1),
                            isDisplayed()));
            linearLayout.perform(click());


            // Make sure we are still in description form after allowing time
            // for intent to potentially launch
            Thread.sleep(10000);
            onView(withClassName(Matchers.endsWith("DonationLogin")));
            Intents.release();
        } catch (Exception e){

        }

    }




}



