package com.example.qr_go;

import static junit.framework.TestCase.assertTrue;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qr_go.Activities.Profile.ThisProfileQRListViewActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test profile fragment
 */
public class PlayerProfileTest {
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
     * Ensures the Search function filters out the correct results based on the user's input
     * @throws Exception
     */
    @Test
    public void QRButtonTest(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_profile));

        // Click on QR button
        solo.clickOnView(solo.getView(R.id.my_qr_codes_button));

        // assert we are in QR list
        solo.assertCurrentActivity("Wrong Activity", ThisProfileQRListViewActivity.class);

    }
}
