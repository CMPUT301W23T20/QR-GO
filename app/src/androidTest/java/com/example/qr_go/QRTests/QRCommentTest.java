package com.example.qr_go.QRTests;

import static junit.framework.TestCase.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qr_go.Activities.Profile.ThisProfileQRListViewActivity;
import com.example.qr_go.Activities.QRView.QRCommentViewActivity;
import com.example.qr_go.Activities.QRView.QRViewActivity;
import com.example.qr_go.MainActivity;
import com.example.qr_go.R;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class QRCommentTest {

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
     * Ensures adding and deleting comments work
     * @throws Exception
     */
    @Test
    public void AddDeleteQRCommentTest() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_profile));

        // Click on QR button
        solo.clickOnView(solo.getView(R.id.my_qr_codes_button));

        // assert we are in QR list
        solo.assertCurrentActivity("Wrong Activity", ThisProfileQRListViewActivity.class);

        // get name of QR in list
        RecyclerView qrList =(RecyclerView)solo.getView(R.id.qr_list);

        // make sure list isnt empty
        assert(qrList.getAdapter().getItemCount() > 0);

        View view = qrList.getChildAt(0);
        TextView qrNameList = (TextView)view.findViewById(R.id.name_text);

        // Click on list
        solo.clickInRecyclerView(0);

        // assert we are in QR list
        solo.assertCurrentActivity("Wrong Activity", QRViewActivity.class);

        // check if name matches
        TextView qrNameView = (TextView)view.findViewById(R.id.name_text);

        assert(qrNameList.getText().equals(qrNameView.getText()));

        // click on comments button
        solo.clickOnView(solo.getView(R.id.comment_list_button));

        // assert we are in comment list
        solo.assertCurrentActivity("Wrong Activity", QRCommentViewActivity.class);

        // Click on comment bar
        solo.clickOnView(solo.getView(R.id.comment_input));

        String testComment = "test comment" + Math.random();

        // Input comment
        solo.enterText((EditText) solo.getView(R.id.comment_input), testComment);
        // send comment
        solo.clickOnView(solo.getView(R.id.add_comment_button));

        assertTrue(solo.waitForText(testComment, 1, 2000));

        // Click on comment just created

        // get name of QR in list
        RecyclerView commentList =(RecyclerView)solo.getView(R.id.comment_list);

        solo.clickInRecyclerView(commentList.getAdapter().getItemCount() - 1);

        solo.clickOnButton("Yes");

    }

}
