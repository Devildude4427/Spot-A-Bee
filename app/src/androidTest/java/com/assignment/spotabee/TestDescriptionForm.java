package com.assignment.spotabee;

/**
 * Tests UI  and  user journey for description form
 */

import android.app.Activity;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewFinder;
import android.support.test.espresso.core.internal.deps.guava.collect.Iterables;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.widget.Spinner;

import com.assignment.spotabee.MainActivity;
import com.assignment.spotabee.R;
import com.assignment.spotabee.customexceptions.ObsceneNumberException;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.fragments.FragmentAfterSubmission;
import com.assignment.spotabee.fragments.FragmentDescriptionForm;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import android.test.InstrumentationTestCase;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
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



@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestDescriptionForm {
    private final String TAG = "Test DF: ";
    Context appContext;
    AppDatabase database;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        appContext = InstrumentationRegistry.getTargetContext();
        mActivityTestRule.launchActivity(new Intent());


        mActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new FragmentDescriptionForm())
                .commit();


        database = AppDatabase.getAppDatabase(mActivityTestRule.getActivity());
        database.databaseDao().nukeTable();

        InstrumentationRegistry.getTargetContext().deleteDatabase("AppDatabase");
    }



    @Test
    public void doUiComponentsExist() {



        onView(withId(R.id.identifyFlowerMessage))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.flower_sighting)));

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

        InstrumentationRegistry.getTargetContext().deleteDatabase("AppDatabase");


    }

    @Test
    public void doesSubmitWork(){
        onView(withId(R.id.submit))
                .perform(scrollTo(), click());

        onView(withClassName(Matchers.endsWith("AfterSubmission")));

        InstrumentationRegistry.getTargetContext().deleteDatabase("AppDatabase");
    }

    @Before
    public void clearDatabase(){
        database = AppDatabase.getAppDatabase(mActivityTestRule.getActivity());
        database.databaseDao().nukeTable();

        InstrumentationRegistry.getTargetContext().deleteDatabase("AppDatabase");
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

        onView(withId(R.id.search_location))
                .perform(click());


        onView(withId(R.id.search_location))
                .perform(click());

        onView(withId(R.id.flowerField))
                .perform(
                        scrollTo(),
                        typeText(testFlowerType),
                        closeSoftKeyboard()
                );


        onView(withId(R.id.numOfBees))
                .perform(
                        scrollTo(),
                        typeText(testNumberOfBees),
                        closeSoftKeyboard()
                );

        onView(withId(R.id.descriptionField))
                .perform(
                        scrollTo(),
                        typeText(testDescriptiveDetails),
                        closeSoftKeyboard()
                );


        onView(withId(R.id.submit))
                .perform(scrollTo(), click());



        assertEquals(1, database.databaseDao()
                .getAllDescriptions().size());

        InstrumentationRegistry.getTargetContext().deleteDatabase("AppDatabase");

    }

    @Before
    public void clearDatabaseAgain(){
        database = AppDatabase.getAppDatabase(mActivityTestRule.getActivity());
        database.databaseDao().nukeTable();

        InstrumentationRegistry.getTargetContext().deleteDatabase("AppDatabase");
    }

    @Test
    public void testObsceneNumberExceptionIsThrown(){
        clearDatabase();
        database.databaseDao().nukeTable();

        String testFlowerType = "Alliums";
        String testNumberOfBees = "70";
        String testDescriptiveDetails = "None";
        String testAddress = "Cardiff Queens Street";

        onView(withId(R.id.locationField))
                .perform(
                        scrollTo(),
                        typeText(testAddress),
                        closeSoftKeyboard()
                );

        onView(withId(R.id.search_location))
                .perform(scrollTo(), click());



        onView(withId(R.id.flowerField))
                .perform(
                        scrollTo(),
                        typeText(testFlowerType),
                        closeSoftKeyboard()
                );


        onView(withId(R.id.numOfBees))
                .perform(
                        scrollTo(),
                        typeText(testNumberOfBees),
                        closeSoftKeyboard()
                );

        onView(withId(R.id.descriptionField))
                .perform(
                        scrollTo(),
                        typeText(testDescriptiveDetails),
                        closeSoftKeyboard()
                );


        onView(withId(R.id.submit))
                .perform(scrollTo(), click());



        // Asserts that the submission does not save since the ObsceneNumberException
        // would have been caught

        assertEquals(0, database.databaseDao()
                .getAllDescriptions().size());

    }

}
