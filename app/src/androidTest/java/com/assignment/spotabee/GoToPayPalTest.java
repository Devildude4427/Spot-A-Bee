package com.assignment.spotabee;


import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GoToPayPalTest {
    private final String TAG = "PayPalTest";
    private final String donationAmount = "17";
    private final String payPalAccountEmail = "zoilagarman3@gmail.com";
    private final String payPalAccountPasswrd = "12345abcde";
    final UiDevice mDevice =
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    final int timeOut = 1000 * 60;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void entirePayPalJourneyTest() {


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
        mDevice.wait(Until.findObject(By.clazz(WebView.class)), timeOut);

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



    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}



