package com.assignment.spotabee;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.assignment.spotabee.fragments.FragmentHowTo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;

import static android.support.test.espresso.matcher.ViewMatchers.withId;



@SmallTest
@RunWith(AndroidJUnit4.class)
public class HowToTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void goToHowTo(){
        mActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new FragmentHowTo())
                .commit();
    }

    @Test
    public void testImageContent(){

        onView(withId(R.id.good_flower))
                .check(matches(withContentDescription(R.drawable.buddleja_high_res + "")));

        onView(withId(R.id.bad_flower))
                .check(matches(withContentDescription(R.drawable.bad_buddleja + "")));

        onView(withId(R.id.bad_flower))
                .check(matches(withContentDescription(R.drawable.bad_buddleja + "")));


    }

}