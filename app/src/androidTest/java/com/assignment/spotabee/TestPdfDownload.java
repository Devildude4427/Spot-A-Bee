package com.assignment.spotabee;

import android.app.Notification;
import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.android.dx.cf.direct.ClassPathOpener;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.google.android.gms.common.api.CommonStatusCodes.TIMEOUT;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class TestPdfDownload {
    private String fileName = "Bee_Guide.pdf";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private final Context context = mActivityTestRule.getActivity();

    @Before
    public void init(){
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
                        6),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        onView(withId(R.id.downloadGuideBtn))
                .perform(click());

    }
//
//    @Test
//    public void testNotification(){
//        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        device.openNotification();
//        List<UiObject2> notifications = device.findObjects(By.clazz(Notification.class));
//        UiObject2 openDownloads = notifications.get(0);
//
//        for(UiObject2 uiObject2 : notifications){
//            if(uiObject2.getText().equals("Spot a Bee")){
//                openDownloads = uiObject2;
//            }
//        }
//
////        device.wait(Until.hasObject(By.text("Spot A Bee")), TIMEOUT);
////        UiObject2 title = device.findObject(By.text("Spot A Bee"));
//
//
//        assertEquals("Spot a Bee", openDownloads.getText());
//
//        openDownloads.click();
//
//    }

    @Test
    public void testDownloadExists(){
        try {

            Thread.sleep(15000);


            String downloadsDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

            File downloadsDirFolder = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            List<File> files = getListFiles(downloadsDirFolder);


            FilenameFilter filenameFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if(name.lastIndexOf('.')>0) {

                        // get last index for '.' char
                        int lastIndex = name.lastIndexOf('.');

                        // get extension
                        String str = name.substring(lastIndex);
                        String body = name.substring(0, lastIndex);

                        // match path name extension
                        if(name.equals("Bee_Guide.pdf")) {
                            return true;
                        }
                    }
                    return false;
                }
            };

            File[] allDownloads = downloadsDirFolder.listFiles(filenameFilter);
            int numOfMatches = files.size();
            boolean downloadSuccessful = numOfMatches > 0;

            assertEquals(true, downloadSuccessful);
        } catch (Exception e){

        }

    }


    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if(file.getName().endsWith(".pdf")){
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
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
