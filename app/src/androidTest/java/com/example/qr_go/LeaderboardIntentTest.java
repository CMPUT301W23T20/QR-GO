package com.example.qr_go;


import static junit.framework.TestCase.assertTrue;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
     * Tests that the sort buttons in the leaderboard work
     * @throws Exception
     */
    @Test
    public void SortTest(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_leaderboard));
        solo.clickOnView(solo.getView(R.id.filter_button));
        solo.clickOnView(solo.getView(R.id.score_button));
    }
    /**
     * Ensures the Search function filters out the correct results based on the user's input
     * @throws Exception
     */
    @Test
    public void SearchTest(){
        // issue: cannot test search as SearchView cannot be cast
        // to EditText, so cannot enter the input in the search bar
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_leaderboard));

        // Click on search bar
        solo.clickOnView(solo.getView(R.id.searchView));
       // solo.enterText(solo.getView(R.id.score_text), "momo");
      //  solo.enterText(1, "momo");
        solo.enterText((EditText) solo.getView(R.id.searchView), "momo");
        ///solo.enterText(solo.getView(R.id.searchView, "momo"));
        solo.clearEditText((EditText) solo.getView(R.id.searchView)); //Clear the EditText

        assertTrue(solo.waitForText("momo", 1, 2000));

    }


}
