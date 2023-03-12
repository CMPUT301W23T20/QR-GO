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

import com.example.qr_go.Activities.Scan.CameraActivity;
import com.example.qr_go.Activities.Scan.GetLocationActivity;
import com.robotium.solo.Solo;

@RunWith(AndroidJUnit4.class)
public class ScanInstrumentedTest {
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
     * Ensures the QR scanner returns the correct result for the QR code, and that it goes to CameraActivity after
     * @throws Exception
     */
    @Test
    public void QRScanTest(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_scan));


        solo.clickOnView(solo.getView(R.id.btn_scan)); //click on SCAN button

        //here I will scan a QR code that has the text "Hi"
        // i will scan this manually, then I will check to see if the results are correct
        assertTrue(solo.waitForText("Hi", 1, 15000));
        solo.clickOnView(solo.getView(android.R.id.button1)); //clicks on "OK" positive button

        // checks that it switches to CameraActivity
        assertTrue(solo.waitForActivity(CameraActivity.class));
        // Clicks on the button Yes that will open the camera
        solo.clickOnView(solo.getView(R.id.btncamera_id));
        // here will have to manually capture the picture and approve it, I've checked and I don't think Robotium
        // has a method that clicks the camera capture button
        //now checks if activity is switched to GetLocationActivity
        assertTrue(solo.waitForActivity(GetLocationActivity.class,15000));
        //click on the Get Location button
        solo.clickOnView(solo.getView(R.id.button_location));
        //checks to see if the location displayed on screen is correct
        // the emulator's location is set to "1600 Amphitheatre Pkwy", so I will check for that
        assertTrue(solo.waitForText("1600 Amphitheatre Pkwy",1,15000));
        //click Done button to return back to main activity
        solo.clickOnView(solo.getView(R.id.done_button));
        assertTrue(solo.waitForActivity(MainActivity.class));


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
