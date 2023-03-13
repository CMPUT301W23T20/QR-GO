package com.example.qr_go;

import static androidx.test.InstrumentationRegistry.getContext;
import static junit.framework.TestCase.assertTrue;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.provider.Settings;
import android.widget.EditText;
import android.provider.Settings.Secure;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qr_go.Activities.Profile.ThisProfileQRListViewActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for GreetingScreenTest fragment. All the UI tests are written here. Robotium test framework is used.
 */
public class GreetingScreenTest {
    private Solo solo;
    private FirebaseFirestore db;
    private String android_id;

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

        android_id = Secure.getString(getContext().getContentResolver(), Secure.ANDROID_ID);

        db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection("Player");

        collectionReference.document(android_id).delete();



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
     * Tests the Greeting Screen, Username cannot be empty test, Username has to be unique test, Contact information is invalid test, Successfully sign up with a unique username and email
     *
     */
    @Test
    public void GreetingScreenTest(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // Username cannot be empty test

        // Click on confirm Button
        solo.clickOnView(solo.getView(R.id.confirmButton));


        // Username has to be unique test

        // Click on username edit text
        solo.clickOnView(solo.getView(R.id.addUsernameText));
        // enter a non unique username
        solo.enterText((EditText) solo.getView(R.id.addUsernameText), "chrisTest");
        // Click on confirm Button
        solo.clickOnView(solo.getView(R.id.confirmButton));


        // Contact information is invalid test

        // Click on username edit text
        solo.clickOnView(solo.getView(R.id.addUsernameText));
        // enter a unique username
        solo.enterText((EditText) solo.getView(R.id.addUsernameText), "chrisEmail");
        // Click on email edit text
        solo.clickOnView(solo.getView(R.id.addContactText));
        // enter a invalid email
        solo.enterText((EditText) solo.getView(R.id.addContactText), "invalidEmail");
        // Click on confirm Button
        solo.clickOnView(solo.getView(R.id.confirmButton));


        // Successfully sign up with a unique username and email

        // Click on username edit text
        solo.clickOnView(solo.getView(R.id.addUsernameText));
        // enter a unique username
        solo.enterText((EditText) solo.getView(R.id.addUsernameText), "chrisUnique");
        // Click on email edit text
        solo.clickOnView(solo.getView(R.id.addContactText));
        // enter a invalid email
        solo.enterText((EditText) solo.getView(R.id.addContactText), "chrisUnique@ualberta.ca");
        // Click on confirm Button
        solo.clickOnView(solo.getView(R.id.confirmButton));


    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}

