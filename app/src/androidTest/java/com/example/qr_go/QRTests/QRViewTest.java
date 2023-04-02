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
import com.example.qr_go.Activities.QRView.QRPlayerListViewActivity;
import com.example.qr_go.Activities.QRView.QRViewActivity;
import com.example.qr_go.MainActivity;
import com.example.qr_go.R;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class QRViewTest {

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
     * Ensures clicking on the comment button takes you to the right activity
     * @throws Exception
     */
    @Test
    public void CommentButtonTest() {
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
    }

    /**
     * Ensures clicking on the player button takes you to the right activity
     * @throws Exception
     */
    @Test
    public void PlayerButtonTest() {
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
        solo.clickOnView(solo.getView(R.id.player_list_button));

        // assert we are in comment list
        solo.assertCurrentActivity("Wrong Activity", QRPlayerListViewActivity.class);
    }
}
