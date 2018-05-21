package com.assignment.spotabee;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;


import com.assignment.spotabee.fragments.FragmentHome;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.assignment.spotabee.TestHelpers.childAtPosition;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


public class TestClarifaiIntegration {


    @Rule
    public IntentsTestRule<MainActivity> mIntentsRule = new IntentsTestRule<>(
            MainActivity.class);

    @Before
    public void stubGalleryIntent() {

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

        Instrumentation.ActivityResult result = createImageSelectionIntentResultStub();

        // Stub the Intent.
        intending(hasAction(Intent.ACTION_PICK)).respondWith(result);

    }

    @Test
    public void testImageSelectionToClarifai() {

        try {
            // Click on the button that will trigger the stubbed intent.
            onView(withId(R.id.button_image_upload)).perform(click());

            Thread.sleep(30000);

            onView(withId(R.id.flowerField))
                    .check(matches(withText("buddleja")));

        } catch (Exception e){

        }
    }

    private Instrumentation.ActivityResult createImageSelectionIntentResultStub() {
        // Put the drawable in a bundle.
        Bundle bundle = new Bundle();
        bundle.putParcelable(FragmentHome.KEY_IMAGE_DATA, BitmapFactory.decodeResource(
                mIntentsRule.getActivity().getResources(), R.drawable.buddleja_high_res));

        // Create the Intent that will include the bundle.
        Intent resultData = new Intent();
        resultData.putExtras(bundle);

        // Create the ActivityResult with the Intent.
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    /**
     * A device's internet access must be manually disabled before running this test
     */
    @Test
    public void testNoInternetAccess(){
        // Click on the button that will trigger the stubbed intent.
        onView(withId(R.id.button_image_upload)).perform(click());

        onView(withClassName(Matchers.endsWith("FragmentHome")));

        isToastMessageDisplayed(R.string.internet_unavailable);
    }

    // Reference: https://stackoverflow.com/questions/28390574/checking-toast-message-in-android-espresso?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    public void isToastMessageDisplayed(int textId) {
        onView(withText(textId)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

}
