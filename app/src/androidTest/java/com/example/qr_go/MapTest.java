package com.example.qr_go;

import static junit.framework.TestCase.assertTrue;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.qr_go.Activities.MapsActivity;
import com.robotium.solo.Solo;

/**
 * This is the intent test for testing the MapFragment
 */
@RunWith(AndroidJUnit4.class)
public class MapTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Ensures that the Map shows up when clicked on the correct button
     * @throws Exception
     */
    @Test
    public void MapTest(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_map));

        // checks that it switches to CameraActivity
        assertTrue(solo.waitForActivity(MapsActivity.class));


    }

    /**
     * Close activity after each test
     * @throws Exception
     */

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
