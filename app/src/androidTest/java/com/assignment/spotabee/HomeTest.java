package com.assignment.spotabee;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewFinder;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.Spinner;

import com.assignment.spotabee.MainActivity;
import com.assignment.spotabee.R;
import com.assignment.spotabee.customexceptions.ObsceneNumberException;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.fragments.FragmentDescriptionForm;
import com.assignment.spotabee.fragments.FragmentHome;

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


public class HomeTest {
    AppDatabase database;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){

        mActivityTestRule.launchActivity(new Intent());


        mActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new FragmentHome())
                .commit();


        database = AppDatabase.getAppDatabase(mActivityTestRule.getActivity());
        database.descriptionDao().nukeTable();

        InstrumentationRegistry.getTargetContext().deleteDatabase("AppDatabase");
    }



    @Test
    public void doesButtonGoToForm() {
        mActivityTestRule.getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new FragmentHome())
                .commit();

        onView(withId(R.id.button_no_image_upload))
                .perform(click());

        onView(withClassName(Matchers.endsWith("FragmentDescriptionForm")));

    }

//    @Test
//    public void doesButtonGoToCamera() {
//        mActivityTestRule.getActivity()
//                .getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.content_frame, new FragmentHome())
//                .commit();
//
//        onView(withId(R.id.button_camera))
//                .perform(click());
//
//        onView(withClassName(Matchers.endsWith("FragmentDescriptionForm")));
//
//    }
//
//    @Test
//    public void doesButtonGoToGallery() {
//        mActivityTestRule.getActivity()
//                .getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.content_frame, new FragmentHome())
//                .commit();
//
//        onView(withId(R.id.button_image_upload))
//                .perform(click());
//
//        onView(withClassName(Matchers.endsWith("FragmentDescriptionForm")));
//
//    }

}