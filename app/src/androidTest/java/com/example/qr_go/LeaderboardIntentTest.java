package com.example.qr_go;


import static junit.framework.TestCase.assertTrue;

import android.app.Activity;
import android.widget.EditText;

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
import com.example.qr_go.Adapters.LeaderboardAdapter;
import com.robotium.solo.Solo;

@RunWith(AndroidJUnit4.class)

public class LeaderboardIntentTest {

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
    public void SearchTest(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_leaderboard));

        // Click on search bar
        solo.clickOnView(solo.getView(R.id.searchView));
        solo.enterText((EditText) solo.getView(R.id.searchView), "Edm");
        solo.clearEditText((EditText) solo.getView(R.id.searchView)); //Clear the EditText
        /* True if there is a text: Edmonton on the screen, wait at least 2 seconds and
       find minimum one match. */
        assertTrue(solo.waitForText("Edm", 1, 2000));

    }
}
