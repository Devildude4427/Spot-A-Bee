package com.assignment.spotabee;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;

import com.assignment.spotabee.fragments.DonationLogin;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.assignment.spotabee.TestHelpers.childAtPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


public class TestPayPalPaymentDisplay {
    private final String TAG = "PayPalTest";
    private final String donationAmount = "17";
    private final String payPalAccountEmail = "zoilagarman3@gmail.com";
    private final String payPalAccountPasswrd = "12345abcde";
    private final String invalidData = "invalid data";

    private SharedPreferences sp;

    @Rule
    public IntentsTestRule<MainActivity> mIntentsRule = new IntentsTestRule<>(
            MainActivity.class);

    @Before
    public void gotoDonationLogin(){

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

        Instrumentation.ActivityResult result = createEmptyPayPalIntentResultStub();

        // Stub the Intent.
        intending(hasAction(Intent.ACTION_PICK)).respondWith(result);

    }


    @Test
    public void testPaymentInfoSetUp() {
        try {

            onView(withId(R.id.payPalButtonContainer)).perform(click());


            onView(withId(R.id.txtAmount))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("You have donated $17 to Spot a Bee")));


        } catch (Exception e){

        }
    }


    private Instrumentation.ActivityResult createEmptyPayPalIntentResultStub() {
        // Put the drawable in a bundle.
        Bundle bundle = new Bundle();

        // Create the Intent that will include the bundle.
        Intent resultData = new Intent();
        resultData.putExtras(bundle);

        // Create the ActivityResult with the Intent.
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }
}
