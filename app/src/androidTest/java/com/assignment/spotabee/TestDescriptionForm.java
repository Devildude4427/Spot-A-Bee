package com.assignment.spotabee;

/**
 * Tests UI  and  user journey for description form
 */

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.assignment.spotabee.MainActivity;
import com.assignment.spotabee.R;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.fragments.FragmentDescriptionForm;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringEndsWith.endsWith;


public class TestDescriptionForm {
    AppDatabase database;
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        mActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new FragmentDescriptionForm())
                .commit();

        // Grab database and clear it for consistency
        database = AppDatabase.getAppDatabase(mActivityTestRule.getActivity());
        database.descriptionDao().nukeTable();
    }

    @Test
    public void doUiComponentsExist() {

      

        onView(withId(R.id.identifyFlowerMessage))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.identify_a_flower)));

        // Edit Texts
        onView(withId(R.id.numOfBees))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()))
                .check(matches(withHint(R.string.number_of_bees_spotted_on_the_flower)));

        onView(withId(R.id.locationField))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()))
                .check(matches(withHint(R.string.location)));

        onView(withId(R.id.flowerField))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()))
                .check(matches(withHint(R.string.flower_type)));

        onView(withId(R.id.descriptionField))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()))
                .check(matches(withHint(R.string.any_further_details)));

        // Image Views
        onView(withId(R.id.flowerIdentify))
                .check(matches(isDisplayed()));


    }

    @Test
    public void doesSubmitWork(){
        onView(withId(R.id.submit))
                .perform(click());

        onView(withClassName(Matchers.endsWith("AfterSubmission")));
    }

    @Test
    public void doesSubmissionSave(){
        String testFlowerType = "Alliums";
        String testNumberOfBees = "2";
        String testDescriptiveDetails = "None";
        String testAddress = "Cardiff Queens Street";

        onView(withId(R.id.locationField))
                .perform(
                        typeText(testAddress),
                        closeSoftKeyboard()
                );



//        onData(anything()) // Attempt to simulate selecting a location provided by the geocoder
//                .inAdapterView(
//                        withId(R.id.addressSpinner)
//                ).atPosition(0)
//                .perform(click());



        onView(withId(R.id.search_location))
                .perform(click());

        onView(withId(R.id.flowerField))
                .perform(
                        typeText(testFlowerType),
                        closeSoftKeyboard()
                );


        onView(withId(R.id.numOfBees))
                .perform(
                        typeText(testNumberOfBees),
                        closeSoftKeyboard()
                );

        onView(withId(R.id.descriptionField))
                .perform(
                        typeText(testDescriptiveDetails),
                        closeSoftKeyboard()
                );

        onView(withId(R.id.submit))
                .perform(click());


        assertTrue(database.descriptionDao()
        .getAllDescriptions().size() == 1);
    }

}
