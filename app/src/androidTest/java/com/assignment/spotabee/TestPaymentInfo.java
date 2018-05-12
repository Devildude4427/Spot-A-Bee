package com.assignment.spotabee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;



import static junit.framework.Assert.assertEquals;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import android.content.SharedPreferences;

import com.assignment.spotabee.fragments.PaymentInfo;

public class TestPaymentInfo {

    private final String AMOUNT_TO_DISPLAY = "123";

    @Mock
    SharedPreferences mMockSharedPreferences;






    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Mock
    Context mMockContext;



    @Before
    public void initialise(){
//        mMockContext = mock(MainActivity.class);
        mActivityTestRule.launchActivity(new Intent());

        this.mMockSharedPreferences = Mockito.mock(SharedPreferences.class);
        this.mMockContext = Mockito.mock(Context.class);
        Mockito.when(mMockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mMockSharedPreferences);


//
//        mMockSharedPreferences = mMockContext.getSharedPreferences("pref", Context.MODE_PRIVATE);
//        mMockSharedPreferences.edit().putString("amount_payed", AMOUNT_TO_DISPLAY).apply();

        mActivityTestRule.getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new PaymentInfo())
                .commit();


//        mMockSharedPreferences.edit().putString("amount_payed", AMOUNT_TO_DISPLAY);
//        when(mMockSharedPreferences.getString("amount_payed", null))
//                .thenReturn(AMOUNT_TO_DISPLAY);

    }

    @Test
    public void testUIsetUp(){
        Mockito.when(mMockSharedPreferences.getString(anyString(), anyString())).thenReturn(AMOUNT_TO_DISPLAY);


        onView(withId(R.id.txtAmount))
                .check(matches(isDisplayed()))
                .check(matches(withText(AMOUNT_TO_DISPLAY)));
    }
}
