package com.assignment.spotabee;


import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.fragments.FragmentDescriptionForm;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class LocationHelperTest {
    private final String TAG = "Test DF: ";
    Context appContext;
    AppDatabase database;

    @Mock
    LocationHelper locationHelper;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        locationHelper = mock(LocationHelper.class);

        appContext = InstrumentationRegistry.getTargetContext();
        mActivityTestRule.launchActivity(new Intent());


        mActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new LocationHelper())
                .commit();


    }

    @Test
    public void testLocationRetrieval(){

    }


}