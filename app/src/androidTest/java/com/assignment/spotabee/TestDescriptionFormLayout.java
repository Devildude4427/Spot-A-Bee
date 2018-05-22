package com.assignment.spotabee;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.RestrictTo;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.telephony.TelephonyManager;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.lang.reflect.Method;



import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.assignment.spotabee.TestHelpers.childAtPosition;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

// Tests that description form displays the correct layout file if a user's location
// has been found for them.
@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestDescriptionFormLayout {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testLocationSearchIsNotDisplayedWhenLocationIsFound() {

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
                        1),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        onView(withId(R.id.numOfBees))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()))
                .check(matches(withHint(R.string.number_of_bees_spotted_on_the_flower)));


        onView(withId(R.id.flowerField))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()))
                .check(matches(withHint(R.string.flower_type)));

        onView(withId(R.id.descriptionField))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()))
                .check(matches(withHint(R.string.any_further_details)));

        onView(withId(R.id.button_no_image_upload))
                .perform(click());

        onView(withId(R.id.addressSpinner))
                .check((doesNotExist()));

        onView(withId(R.id.location_n_search))
                .check((doesNotExist()));

        onView(withId(R.id.locationField))
                .check((doesNotExist()));

    }


    @Before
    public void setUpNoInternetAccess(){

        WifiManager wifiManager = (WifiManager)mActivityTestRule.getActivity()
                .getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
    }

    /**
     * A device's internet access must be manually disabled before running this test
     */
    @Test
    public void testNoInternetLayout(){

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
                        1),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        onView(withId(R.id.button_no_image_upload))
                .perform(click());

        onView(withId(R.id.search_location))
                .check(matches(isDisplayed()));


        onView(withId(R.id.locationField))
                .check(matches(
                        isDisplayed()
                ));


    }



}
